package com.example.apartmentbuddy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.adapter.ComplainAdapter
import com.example.apartmentbuddy.model.ComplainList
import com.example.apartmentbuddy.model.ComplainListData
import com.example.apartmentbuddy.model.ComplainPersistence
import com.example.apartmentbuddy.model.FirebaseAuthUser

class ComplainListView : Fragment() {
    private lateinit var list: MutableList<ComplainListData>
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: ComplainAdapter
    private val complains = ComplainPersistence()
    private val user_id = FirebaseAuthUser.getUserEmail()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_complain_list_view, container, false)
        recyclerView = view.findViewById(R.id.complain_list_view)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        list = mutableListOf()
        recyclerAdapter = ComplainAdapter(ComplainList.getAllComplains())
        recyclerView.adapter = recyclerAdapter
        if (user_id != null) {
            complains.showComplain(user_id) { success ->
                recyclerAdapter.notifyDataSetChanged()

            }
        }

        val back: Button = view.findViewById(R.id.complainlist_back)
        back.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_complainListView_to_fragment_complain_home)
        }
        return view
    }
}