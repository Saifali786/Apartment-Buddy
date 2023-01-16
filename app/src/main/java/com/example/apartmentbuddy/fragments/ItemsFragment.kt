package com.example.apartmentbuddy.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.adapter.ListItemAdvRecyclerViewAdapter
import com.example.apartmentbuddy.databinding.FragmentItemsBinding
import com.example.apartmentbuddy.model.EditClickListener
import com.example.apartmentbuddy.model.Advertisement
import com.example.apartmentbuddy.model.FirebaseAuthUser
import com.example.apartmentbuddy.model.Item
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ItemsFragment : Fragment(), EditClickListener {
    private lateinit var binding: FragmentItemsBinding
    private lateinit var bottomNavValue: String
    lateinit var listener: EditClickListener

    private val db = FirebaseFirestore.getInstance()
    private val itemCollection = db.collection("items")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemsBinding.inflate(layoutInflater)
        bottomNavValue = arguments?.get("bottomNavValue").toString()
        listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.advRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        var itemList: MutableList<Item>? = null
        var documentSnapshot: MutableList<DocumentSnapshot> = mutableListOf()
        GlobalScope.launch(Dispatchers.IO) {
            when (bottomNavValue) {
                "home", "null" -> {
                    itemList =
                        mapItemDataToView(itemCollection.get().await().documents)
                }
                "myPosts" -> {
                    itemList =
                        mapItemDataToView(
                            itemCollection.whereEqualTo("uid", FirebaseAuthUser.getUserId()).get()
                                .await().documents
                        )
                }
                "bookmark" -> {
                    itemCollection.get().await().documents.forEach {
                        val list = it.data?.get("bookmarkUserList") as MutableList<String>
                        if (list.map { string ->
                                string.replace("[", "").replace("]", "")
                            }.contains(FirebaseAuthUser.getUserId())) {
                            documentSnapshot.add(it)
                        }
                    }
                    itemList = mapItemDataToView(documentSnapshot)
                }
            }
            withContext(Dispatchers.Main) {
                recyclerView.adapter = itemList?.let {
                    ListItemAdvRecyclerViewAdapter(
                        it, bottomNavValue, listener
                    )
                }
            }
        }
    }

    private fun mapItemDataToView(documents: List<DocumentSnapshot>): MutableList<Item> {
        val itemList = mutableListOf<Item>()
        for (document in documents) {
            val images: ArrayList<Uri> =
                document.get("photos").toString().replace("[", "").replace("]", "")
                    .split(",").map {
                        Uri.parse(it.trim())
                    } as ArrayList<Uri>

            itemList.add(
                Item(
                    document.id,
                    document.data?.get("uid").toString(),
                    images,
                    document.data?.get("description").toString(),
                    document.data?.get("type").toString(),
                    document.data?.get("contact").toString(),
                    document.data?.get("title").toString(),
                    document.data?.get("condition").toString(),
                    document.data?.get("price").toString().toFloat(),
                    document.data?.get("category").toString(),
                    document.data?.get("address").toString(),
                    document.data?.get("bookmarkUserList").toString()
                        .split(",") as MutableList<String>
                )
            )
        }
        return itemList
    }

    override fun onAdvertisementEditClick(advertisement: Advertisement) {
        //Navigate to myPosts on POST click
        val bundle = Bundle()
        bundle.putString("bottomNavValue", bottomNavValue)
        val fragment = PostItemFragment(advertisement)
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}