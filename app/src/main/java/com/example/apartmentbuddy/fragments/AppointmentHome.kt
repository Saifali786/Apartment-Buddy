package com.example.apartmentbuddy.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.model.FirebaseAuthUser

class AppointmentHome : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            FirebaseAuthUser.getUserId()
            val view = inflater.inflate(R.layout.fragment_appointment_home, container, false)
            val myToolbar: Toolbar = view.findViewById(R.id.toolbar) as Toolbar
            myToolbar.inflateMenu(R.menu.appointment_new)
            myToolbar.title = "Appointment Booking"
            myToolbar.setTitleTextAppearance(this.context, R.style.CustomActionBarStyle)
            myToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            myToolbar.setNavigationOnClickListener { view ->
                findNavController().navigate(R.id.action_global_home22)
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

            // Accessing Save button from Fragment 2
            val back: Button = view.findViewById(R.id.appointment_back)
            back.setOnClickListener {
                findNavController().navigate(R.id.action_global_home22)
            }
            val new_appointment: CardView = view.findViewById(R.id.newAppointment)
            new_appointment.setOnClickListener {
                view.findNavController()
                    .navigate(AppointmentHomeDirections.actionAppointmentHomeToNewAppointment())
            }

            val view_appointment: CardView = view.findViewById(R.id.ViewAppointment)
            view_appointment.setOnClickListener {
                view.findNavController()
                    .navigate(AppointmentHomeDirections.actionAppointmentHomeToShowAppointment())
            }

            val cancel_appointment: CardView = view.findViewById(R.id.CancelAppointment)
            cancel_appointment.setOnClickListener {
                view.findNavController()
                    .navigate(AppointmentHomeDirections.actionAppointmentHomeToCancelAppointment())
            }

        return view
    }
}