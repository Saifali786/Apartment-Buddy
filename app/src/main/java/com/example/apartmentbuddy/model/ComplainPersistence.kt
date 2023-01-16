package com.example.apartmentbuddy.model

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class ComplainPersistence {
    private val db = FirebaseFirestore.getInstance()

    fun showComplain(user_id: String, function: (Boolean) -> Unit) {
        db.collection("complain").whereEqualTo("userId", user_id).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data.get("name")
                var ticketid : String = document.data.get("ticketid").toString()
                var category : String = document.data.get("category").toString()
                var subject : String = document.data.get("subject").toString()
                var description : String = document.data.get("description").toString()
                var status : String = document.data.get("status").toString()
                ComplainList.add(ComplainListData(ticketid, category, subject, description,status))
                function(true)
            }

        }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                function(false)
            }
    }
}