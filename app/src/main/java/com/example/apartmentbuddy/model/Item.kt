package com.example.apartmentbuddy.model

import android.net.Uri
import java.util.ArrayList

class Item(
    documentId: String,
    uid: String,
    photos: ArrayList<Uri>,
    description: String,
    type: String,
    contact: String,
    val title: String,
    val condition: String,
    val price: Float,
    val category: String,
    val address: String,
    bookmarkUserList: MutableList<String>
) : Advertisement(documentId, uid, photos, description, type, contact, bookmarkUserList)
