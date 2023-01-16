package com.example.apartmentbuddy.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.model.Appointment

class ShowAppointmentDetail : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show_appointment_detail, container, false)

        val appointmentId : String = ShowAppointmentDetailArgs.fromBundle(requireArguments()).appointmentId
        val appointmentName : String = ShowAppointmentDetailArgs.fromBundle(requireArguments()).appointmentName
        val appointmentDate : String = ShowAppointmentDetailArgs.fromBundle(requireArguments()).appointmentDate
        val appointmentTime : String = ShowAppointmentDetailArgs.fromBundle(requireArguments()).appointmentTime
        val appointmentLocation : String = ShowAppointmentDetailArgs.fromBundle(requireArguments()).appointmentLocation
        val previousOperation : String = ShowAppointmentDetailArgs.fromBundle(requireArguments()).previousOperation

        val appointment = Appointment()

        val back : Button = view.findViewById(R.id.appointment_back)
        val cancel : Button = view.findViewById(R.id.appointment_cancel)
        val showAppointmentId : TextView = view.findViewById(R.id.appointment_id)
        val showAppointmentName : TextView = view.findViewById(R.id.appointment_name)
        val showAppointmentDate : TextView = view.findViewById(R.id.appointment_date)
        val showAppointmentTime : TextView = view.findViewById(R.id.appointment_time)
        val showAppointmentLocation : TextView = view.findViewById(R.id.appointment_location)

        if(previousOperation.equals("SHOW")){
            showAppointmentId.text = appointmentId
            showAppointmentName.text = appointmentName
            showAppointmentDate.text = appointmentDate
            showAppointmentTime.text = appointmentTime
            showAppointmentLocation.text = appointmentLocation
            if(appointment.isPending(appointmentDate)){
                cancel.isVisible = true
                cancel.setOnClickListener {
                    this.view?.let { it1 ->
                        appointment.confirmToCancelAppointment(appointmentId,appointmentDate,appointmentTime,this.context,
                            it1
                        )
                    }
                }
            }
            else
            {
                cancel.isVisible = false
            }
            back.setOnClickListener{
                view.findNavController().navigate(ShowAppointmentDetailDirections.actionShowAppointmentDetailToShowAppointment())
            }
        }
        else{
            showAppointmentId.text = appointmentId
            showAppointmentName.text = appointmentName
            showAppointmentDate.text = appointmentDate
            showAppointmentTime.text = appointmentTime
            showAppointmentLocation.text = appointmentLocation

            cancel.isVisible = true
            cancel.setOnClickListener {
                this.view?.let { it1 ->
                    appointment.confirmToCancelAppointment(appointmentId,appointmentDate,appointmentTime,this.context,
                        it1
                    )
                }
            }

            back.setOnClickListener{
                view.findNavController().navigate(ShowAppointmentDetailDirections.actionShowAppointmentDetailToShowAppointment())
            }
            back.setOnClickListener {
                view.findNavController().navigate(ShowAppointmentDetailDirections.actionShowAppointmentDetailToCancelAppointment())
            }
        }
        return view
    }
}
