package com.example.apartmentbuddy.model

object PendingAppointmentList {
    private val pendingAppointmentList = mutableListOf<AppointmentData>()

    // Method to add data in Appointment list
    fun add(pendingAppointment: AppointmentData) {
        if(!pendingAppointmentList.contains(pendingAppointment)){
            pendingAppointmentList.add(pendingAppointment)
        }
    }

    fun contains(appointmentID : String): Boolean{
        if(pendingAppointmentList.contains(AppointmentData(appointmentID))){
            return true
        }
        return false
    }

    // Method to fetch the data in Appointment List
    fun getAllAppointment(): List<AppointmentData> {
        return pendingAppointmentList
    }
}