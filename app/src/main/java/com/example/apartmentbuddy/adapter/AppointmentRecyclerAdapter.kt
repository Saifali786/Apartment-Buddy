package com.example.apartmentbuddy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.fragments.CancelAppointmentDirections

import com.example.apartmentbuddy.fragments.ShowAppointmentDirections
import com.example.apartmentbuddy.model.AppointmentData


class AppointmentRecyclerAdapter(private val appointment: List<AppointmentData>,var currentOperation : String) : RecyclerView.Adapter<AppointmentRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_view_each_appointment, parent, false)
        return ViewHolder(view, currentOperation);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.appointmentId.text = appointment[position].appointmentID.toString()
        holder.appointmentDate.text = appointment[position].date
        holder.appointmentTime.text = appointment[position].time
        holder.appointmentLocation.text = appointment[position].location
        holder.appointmentName = appointment[position].name.toString()
        holder.appointmentUserId = appointment[position].userId.toString()
    }

    override fun getItemCount(): Int {
        return appointment.size
    }

    inner class ViewHolder(itemView: View, currentOperation: String) :
        RecyclerView.ViewHolder(itemView) {
        var appointmentDate: TextView
        var appointmentTime: TextView
        var appointmentLocation: TextView
        var appointmentId: TextView
        var thisAppointment: CardView
        lateinit var appointmentName: String
        lateinit var appointmentUserId: String

        init {
            appointmentId = itemView.findViewById(R.id.show_appointment_count)
            appointmentDate = itemView.findViewById(R.id.show_appointment_date)
            appointmentTime = itemView.findViewById(R.id.show_appointment_time)
            appointmentLocation = itemView.findViewById(R.id.show_appointment_location)
            thisAppointment = itemView.findViewById(R.id.each_appointment)
            thisAppointment.setOnClickListener{
                if(currentOperation.equals("SHOW")){
                    itemView.findNavController().navigate(ShowAppointmentDirections.actionShowAppointmentToShowAppointmentDetail(
                        appointmentId.text.toString(),
                        appointmentName,
                        appointmentUserId,
                        appointmentDate.text.toString(),
                        appointmentTime.text.toString(),
                        appointmentLocation.text.toString(),
                        currentOperation
                    ))
                }
                else {
                    itemView.findNavController()
                        .navigate(
                            CancelAppointmentDirections.actionCancelAppointmentToShowAppointmentDetail(
                                appointmentId.text.toString(),
                                appointmentName,
                                appointmentUserId,
                                appointmentDate.text.toString(),
                                appointmentTime.text.toString(),
                                appointmentLocation.text.toString(),
                                currentOperation
                            )
                        )
                }
            }
        }
    }
}

