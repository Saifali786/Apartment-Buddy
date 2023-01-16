package com.example.apartmentbuddy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.model.Appointment

class ConfirmAppointment : Fragment() {
    private val appointment = Appointment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_confirm_appointment, container, false)
        val selected_date = ConfirmAppointmentArgs.fromBundle(requireArguments()).date
        val selected_time = ConfirmAppointmentArgs.fromBundle(requireArguments()).time
        val userId = ConfirmAppointmentArgs.fromBundle(requireArguments()).userId

        val myToolbar: Toolbar = view.findViewById(R.id.toolbar) as Toolbar
        myToolbar.inflateMenu(R.menu.appointment_new)
        myToolbar.title = "New Appointment"
        myToolbar.setTitleTextAppearance(this.context,R.style.CustomActionBarStyle)
        myToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        myToolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigate(ConfirmAppointmentDirections.actionConfirmAppointmentToAppointmentHome())
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

        var location : String ? = null
        var appointmentId : String ? = null
        var appointmentName :String ? = null
        var appointmentNotes : String ?= null
        var back : Button = view.findViewById(R.id.confirm_appointment_back)
        val showAppointmentId : TextView = view.findViewById(R.id.confirm_appointment_id)
        val showAppointmentName : TextView = view.findViewById(R.id.confirm_appointment_name)
        val showAppointmentDate : TextView = view.findViewById(R.id.confirm_appointment_date)
        val showAppointmentTime : TextView = view.findViewById(R.id.confirm_appointment_time)
        val showAppointmentNotes : TextView = view.findViewById(R.id.confirm_appointment_notes)
        val showAppointmentLocation : TextView = view.findViewById(R.id.confirm_appointment_location)
        appointment.getAppointment(userId, selected_date, selected_time){
            location = it.get("location").toString()
            appointmentName = it.get("name").toString()
            appointmentId = it.get("appointmentId").toString()
            showAppointmentId.text = appointmentId.toString()
            showAppointmentName.text = appointmentName.toString()
            showAppointmentDate.text = selected_date.toString()
            showAppointmentTime.text = selected_time.toString()
            showAppointmentLocation.text = location.toString()
            appointmentNotes = it.get("notes").toString()
            var notesTitle : TextView = view.findViewById(R.id.appointment_notes_title)
            if(appointmentNotes!!.isEmpty()){
                notesTitle.isVisible = false
            }
            else{
                showAppointmentNotes.text = appointmentNotes
            }

        }
        back.setOnClickListener(){
            view.findNavController().navigate(ConfirmAppointmentDirections.actionConfirmAppointmentToAppointmentHome())
        }
        return view
    }
}