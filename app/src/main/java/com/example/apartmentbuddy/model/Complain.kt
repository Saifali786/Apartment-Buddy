package com.example.apartmentbuddy.model

import android.net.Uri

data class Complain(
    val userId: String? = null,
    val selectedImages: ArrayList<Uri>? = null,
    val description: String? = null,
    val subject: String? = null,
    val date: String? = null,
    val category: String? = null,
    val unitnumber: String? = null,
    val firstname: String? = null,
    val status: String? = null,
    val ticketid: String? = null,
    val documentid: String? = null
)