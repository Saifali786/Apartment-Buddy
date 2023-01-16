package com.example.apartmentbuddy.model
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppointmentData(
    @DocumentId val appointmentID: String ? = null,
    val name:String ? = null,
    val date: String ? = null,
    val time:String ? = null,
    val userId: String? = null,
    var location: String? = null,
    val timestamp: String? = null,
    val notes : String ? = null
) : Parcelable
