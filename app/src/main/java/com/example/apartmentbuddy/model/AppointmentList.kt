package com.example.apartmentbuddy.model

object AppointmentList {
        private val appointmentList = mutableListOf<AppointmentData>()

    // Method to add data in Appointment list
        fun add(appointment: AppointmentData) {
            if(!appointmentList.contains(appointment)){
                appointmentList.add(appointment)
            }
        }

        fun remove(){
            appointmentList.removeAll(appointmentList)
        }

        // Method to fetch the data in Appointment List
        fun getAllAppointment(): List<AppointmentData> {
            return appointmentList
        }
}

