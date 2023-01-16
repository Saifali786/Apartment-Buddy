package com.example.apartmentbuddy.model

import android.net.Uri
import java.util.*

class Apartment(
    documentId: String,
    uid: String,
    photos: ArrayList<Uri>,
    description: String,
    type: String,
    contact: String,
    val noOfBedrooms: Float,
    val noOfBathrooms: Float,
    val unitNumber: String,
    val rent: Float,
    val availability: String,
    bookmarkUserList: MutableList<String>
) : Advertisement(documentId, uid, photos, description, type, contact, bookmarkUserList)
