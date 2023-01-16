package com.example.apartmentbuddy.fragments

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ItemController {
    private val db = FirebaseFirestore.getInstance()
    private val apartmentCollection = db.collection("items")

    fun addUserToBookmarkList(documentId: String, userId: String) {
        apartmentCollection.document(documentId).update(
            "bookmarkUserList", FieldValue.arrayUnion(userId)
        ).addOnCompleteListener { bookmark ->
            if (bookmark.isSuccessful) {
                Log.e("Bookmark", "$userId bookmarked $documentId")
            }
        }
    }

    fun removeUserFromBookmarkList(documentId: String, userId: String) {
        apartmentCollection.document(documentId).update(
            "bookmarkUserList", FieldValue.arrayRemove(userId)
        ).addOnCompleteListener { removeBookmark ->
            if (removeBookmark.isSuccessful) {
                Log.e("Bookmark Remove", "$userId removed bookmark $documentId")
            }
        }
    }
}