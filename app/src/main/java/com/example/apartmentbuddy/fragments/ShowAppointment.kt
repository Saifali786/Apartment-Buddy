package com.example.apartmentbuddy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.adapter.AppointmentRecyclerAdapter
import com.example.apartmentbuddy.model.Appointment
import com.example.apartmentbuddy.model.AppointmentList
import com.example.apartmentbuddy.model.FirebaseAuthUser

class ShowAppointment : Fragment() {
    private val appointment  = Appointment()
    private val user_id : String = FirebaseAuthUser.getUserEmail().toString()
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: AppointmentRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_show_appointment, container, false)

        val myToolbar: Toolbar = view.findViewById(R.id.toolbar) as Toolbar
        myToolbar.inflateMenu(R.menu.appointment_new)
        myToolbar.title = "View Appointments"
        myToolbar.setTitleTextAppearance(this.context,R.style.CustomActionBarStyle)
        myToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        myToolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigate(ShowAppointmentDirections.actionShowAppointmentToAppointmentHome())
        }
        myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_home -> {
                    findNavController().navigate(R.id.action_global_home22)
                    true
                }
                else -> false
            }
        }
        recyclerView = view.findViewById(R.id.appointment_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerAdapter = AppointmentRecyclerAdapter(AppointmentList.getAllAppointment(),"SHOW")
        recyclerView.adapter = recyclerAdapter
        appointment.showAppointment(user_id) { success ->
            recyclerAdapter.notifyDataSetChanged()

        }
        val back: Button = view.findViewById(R.id.appointment_back)
        back.setOnClickListener {
            view.findNavController().navigate(ShowAppointmentDirections.actionShowAppointmentToAppointmentHome())
        }
        return view
    }
}