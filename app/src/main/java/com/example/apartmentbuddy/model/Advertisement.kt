package com.example.apartmentbuddy.model

import android.net.Uri
import java.util.ArrayList

open class Advertisement (
    val documentId: String,
    val uid: String,
    val photos: ArrayList<Uri>,
    val description: String,
    val type: String,
    val contact: String,
    val bookmarkUserList: MutableList<String>? = null
    )