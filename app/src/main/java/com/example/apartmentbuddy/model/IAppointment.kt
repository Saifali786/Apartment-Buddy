package com.example.apartmentbuddy.model

import android.content.Context

interface IAppointment {
    fun pendingAppointment(user_id : String, function: (Boolean) -> Unit)
    fun cancelAppointment(appointmentId : String)
    fun showAppointment(user_id: String, function : (Boolean) -> Unit)
    fun addNewAppointment(date: String, time: String, userId: String, userName: String, context: Context?, notes : String) : Boolean
}