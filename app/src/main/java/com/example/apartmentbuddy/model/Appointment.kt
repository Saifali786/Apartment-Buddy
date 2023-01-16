package com.example.apartmentbuddy.model

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import com.example.apartmentbuddy.fragments.*
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Model Class of Appointment which is implement IAppointment interface,
 * where all the business logic, helping function and database interaction rests.
 */
class Appointment : IAppointment{
    // Initializing the instance of database
    private val db = FirebaseFirestore.getInstance()

    // Prints the time user selected from the Time picker
    fun  printValidTime(hourOfDay: Int, minute : Int) : String {
        var hour = hourOfDay
        var am_pm = ""
        // AM_PM decider logic
        /*
        Reference : https://www.geeksforgeeks.org/timepicker-in-kotlin/
         */
        when { hour == 0 -> { hour += 12
            am_pm = "AM"
        }
            hour == 12 -> am_pm = "PM"
            hour > 12 -> { hour -= 12
                am_pm = "PM"
            }
            else -> am_pm = "AM"
        }
        val hourDay = if (hour < 10) "0" + hour else hour
        val min = if (minute < 10) "0" + minute else minute
        // display format of time
        return "$hourDay : $min $am_pm"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    // Reference : https://www.programiz.com/kotlin-programming/examples/current-date-time
    // Formats the current Date and time in "yyyy-MM-dd HH:mm:ss"
    fun buildTimeStamp() : String? {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        val formatted = current.format(formatter)
        return formatted
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun confirmAppointment(date: String, time: String, context: Context?, view: View, user_id: String, user_name: String, notes : String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm")
        builder.setMessage("Do you want proceed with this appointment on $date at $time?")

        builder.setPositiveButton("Yes"){dialogInterface, which ->
            view.findNavController().navigate(AppointmentNotesDirections.actionAppointmentNotesToConfirmAppointment(date,time,user_id,notes))
            addNewAppointment(date, time, user_id, user_name, context, notes)
        }
        builder.setNeutralButton("Cancel"){dialogInterface , which ->
            Toast.makeText(context,"Appointment cancelled",Toast.LENGTH_LONG).show()
            view.findNavController().navigate(AppointmentNotesDirections.actionAppointmentNotesToAppointmentHome())
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun confirmToCancelAppointment(appointmentId: String,date: String, time: String, context: Context?, view: View){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Cancel Appointment")
        builder.setMessage("Do you want cancel this appointment on $date at $time?")

        builder.setPositiveButton("Yes"){dialogInterface, which ->
            cancelAppointment(appointmentId)
            AppointmentList.remove()
            Toast.makeText(context,"Appointment Cancelled",Toast.LENGTH_LONG).show()
            view.findNavController().navigate(ShowAppointmentDetailDirections.actionShowAppointmentDetailToAppointmentHome())
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    // Populates the database with new appointment details
    // Reference : https://firebase.google.com/docs/firestore/manage-data/add-data
    @RequiresApi(Build.VERSION_CODES.O)
    override fun addNewAppointment(date: String, time: String, userId: String, userName: String, context: Context?, notes : String) : Boolean{
        val appointmentData = AppointmentData(name = userName, date = date, time = time, userId = userId, location = "Office 2", timestamp = buildTimeStamp(), notes = notes)
        db.collection("appointment").add(appointmentData)
            .addOnSuccessListener {
                Toast.makeText(context, "Appointment Booked", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener { ex ->
                Toast.makeText(
                    context, "Host down due to" + ex.message, Toast.LENGTH_LONG
                ).show()
            }
        return true
    }

    // Fetches the database using User Email
    override fun showAppointment(user_id: String, function : (Boolean) -> Unit ) {
        db.collection("appointment").whereEqualTo("userId", user_id).get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val data = document.data.get("name")
                    var name : String = document.data.get("name").toString()
                    var date : String = document.data.get("date").toString()
                    var time : String = document.data.get("time").toString()
                    var user_id : String = document.data.get("user_id").toString()
                    var location : String = document.data.get("location").toString()
                    var timestamp : String = document.data.get("timestamp").toString()
                    var notes : String = document.data.get("notes").toString()
                    var appointment_id : String = document.id
                    AppointmentList.add(AppointmentData(appointment_id,name, date, time, user_id, location, timestamp, notes))
                    function(true)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                function(false)
            }
    }

    // Fetches specific appointment
    fun getAppointment(user_id: String, date: String, time: String, function: (HashMap<String, String>) -> Unit){
        val currentAppointment : HashMap<String,String> = HashMap()
        db.collection("appointment").whereEqualTo("userId", user_id)
            .whereEqualTo("date",date).whereEqualTo("time", time)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var name : String = document.data.get("name").toString()
                    var location : String = document.data.get("location").toString()
                    var appointmentId : String = document.id.toString()
                    var notes : String = document.data.get("notes").toString()

                    currentAppointment.put("appointmentId", appointmentId)
                    currentAppointment.put("location", location)
                    currentAppointment.put("name", name)
                    currentAppointment.put("notes", notes)
                    function(currentAppointment)
                }
            }
            .addOnFailureListener(){
                Log.e(TAG,"${error("Error")}")
            }
    }

    // Fetches all the pending appointment using userId from the database
    @RequiresApi(Build.VERSION_CODES.O)
    override fun pendingAppointment(user_id : String, function: (Boolean) -> Unit) {
        db.collection("appointment").whereEqualTo("userId", user_id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val date = document.data.get("date")
                    if(isPending(date.toString())){
                        var name : String = document.data.get("name").toString()
                        var date : String = document.data.get("date").toString()
                        var time : String = document.data.get("time").toString()
                        var user_id : String = document.data.get("user_id").toString()
                        var location : String = document.data.get("location").toString()
                        var timestamp : String = document.data.get("timestamp").toString()
                        var appointment_id : String = document.id
                        PendingAppointmentList.add(AppointmentData(appointment_id, name, date, time, user_id, location, timestamp))
                        function(true)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                function(false)
            }
    }

    // Deletes the appointment from the database
    // Reference : https://firebase.google.com/docs/firestore/manage-data/delete-data
    override fun cancelAppointment(appointmentId : String){
        db.collection("appointment").document(appointmentId)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    // Logic to check if particular appointment is pending or not
    @RequiresApi(Build.VERSION_CODES.O)
    fun isPending(selectedDate : String) : Boolean{
        val currentDate = LocalDate.now()
        val formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = currentDate.format(formatterDate)
        if(selectedDate > formattedDate.toString()){
            return true
        }
        return false
    }

    // Fetches the username of the user from his unique ID
    fun getUserName(uid : String, function: (String) -> Unit){
        db.collection("users").whereEqualTo("user_id", uid)
            .get()
            .addOnSuccessListener{ documents ->
                for(document in documents){
                    val username = document.data.get("name").toString()
                    function(username)
                }
            }
            .addOnFailureListener{
                    e -> Log.w(TAG, "Error fetching username", e)
            }
    }
}