package com.example.apartmentbuddy.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.adapter.ListApartmentAdvRecyclerViewAdapter
import com.example.apartmentbuddy.databinding.FragmentApartmentBinding
import com.example.apartmentbuddy.model.EditClickListener
import com.example.apartmentbuddy.model.Advertisement
import com.example.apartmentbuddy.model.Apartment
import com.example.apartmentbuddy.model.FirebaseAuthUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class ApartmentFragment : Fragment(), EditClickListener {
    private lateinit var binding: FragmentApartmentBinding
    private lateinit var bottomNavValue: String
    var adapter: ListApartmentAdvRecyclerViewAdapter? = null
    lateinit var listener: EditClickListener

    private val db = FirebaseFirestore.getInstance()
    private val apartmentCollection = db.collection("apartments")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApartmentBinding.inflate(layoutInflater)
        bottomNavValue = arguments?.get("bottomNavValue").toString()
        listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.advRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        var apartmentList: MutableList<Apartment>? = null
        var documentSnapshot: MutableList<DocumentSnapshot> = mutableListOf()
        GlobalScope.launch(Dispatchers.IO) {
            when (bottomNavValue) {
                "home", "null" -> {
                    apartmentList =
                        mapApartmentDataToView(apartmentCollection.get().await().documents)
                }
                "myPosts" -> {
                    apartmentList =
                        mapApartmentDataToView(
                            apartmentCollection.whereEqualTo("uid", FirebaseAuthUser.getUserId())
                                .get().await().documents
                        )
                }
                "bookmark" -> {
                    apartmentCollection.get().await().documents.forEach {
                        val list = it.data?.get("bookmarkUserList") as MutableList<String>
                        if (list.map { string ->
                                string.replace("[", "").replace("]", "")
                            }.contains(FirebaseAuthUser.getUserId())) {
                            documentSnapshot.add(it)
                        }
                    }
                    apartmentList = mapApartmentDataToView(documentSnapshot)
                }
            }
            withContext(Dispatchers.Main) {
                recyclerView.adapter = apartmentList?.let {
                    ListApartmentAdvRecyclerViewAdapter(
                        it, bottomNavValue, listener
                    )
                }
            }
        }
    }

    private fun mapApartmentDataToView(documents: List<DocumentSnapshot>): MutableList<Apartment>? {
        val apartmentList = mutableListOf<Apartment>()
        for (document in documents) {
            val images: ArrayList<Uri> =
                document.get("photos").toString().replace("[", "").replace("]", "")
                    .split(",").map {
                        Uri.parse(it.trim())
                    } as ArrayList<Uri>

            apartmentList.add(
                Apartment(
                    document.id,
                    document.data?.get("uid").toString(),
                    images,
                    document.data?.get("description").toString(),
                    document.data?.get("type").toString(),
                    document.data?.get("contact").toString(),
                    document.data?.get("noOfBedrooms").toString().toFloat(),
                    document.data?.get("noOfBathrooms").toString().toFloat(),
                    document.data?.get("unitNumber").toString(),
                    document.data?.get("rent").toString().toFloat(),
                    document.data?.get("availability").toString(),
                    document.data?.get("bookmarkUserList") as MutableList<String>
                )
            )
        }
        return apartmentList
    }

    override fun onAdvertisementEditClick(advertisement: Advertisement) {
        //Navigate to myPosts on POST click
        val bundle = Bundle()
        bundle.putString("bottomNavValue", bottomNavValue)
        val fragment = PostApartmentFragment(advertisement)
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}