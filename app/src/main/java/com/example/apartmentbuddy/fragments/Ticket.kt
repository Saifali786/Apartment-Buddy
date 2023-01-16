package com.example.apartmentbuddy.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toolbar
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.adapter.TicketAdapter
import com.example.apartmentbuddy.model.Complain
import com.google.firebase.firestore.*

open class Ticket : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var list: MutableList<Complain>
    lateinit var adapter: TicketAdapter
    private val db = FirebaseFirestore.getInstance()
    private val ticketCollection = db.collection("complain")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ticket, container, false)

        val myToolbar: Toolbar = view.findViewById(R.id.toolbar) as Toolbar
        myToolbar.inflateMenu(R.menu.tickets)
        myToolbar.title = "Tickets"
        myToolbar.setTitleTextAppearance(this.context, R.style.CustomActionBarStyle)
        myToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)

        myToolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigate(R.id.action_global_homeAdmin2)
        }

        myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.pending_tickets -> {
                    view.findNavController()
                        .navigate(R.id.action_ticket_to_pendingTickets)
                    true
                }
                R.id.action_home -> {
                    view.findNavController()
                        .navigate(R.id.action_global_homeAdmin2)
                    true

                }
                else -> false
            }
        }

        return view
    }
    //References : https://www.youtube.com/watch?v=Az4gXQAP-a4
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvMain)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        val language = resources.getStringArray(R.array.status_options)
        val statusAdapter = ArrayAdapter(requireContext(), R.layout.list_item, language)
        list = mutableListOf()
        adapter = TicketAdapter(requireContext(), list, statusAdapter)
        recyclerView.adapter = adapter
        EventChangeListener()


    }
    //References : https://www.youtube.com/watch?v=Az4gXQAP-a4
    //This method fetches all the user tickets from firestore
    private fun EventChangeListener() {
        ticketCollection.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val name: String? = dc.document.getString("firstname")
                        val status: String? = dc.document.getString("status")
                        val date: String? = dc.document.getString("date")
                        list.add(
                            Complain(
                                firstname = name,
                                status = status,
                                date = date,
                                documentid = dc.document.id
                            )
                        );
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }
}