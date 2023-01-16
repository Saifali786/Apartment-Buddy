package com.example.apartmentbuddy.model

object ComplainList {

    private val complainList = mutableListOf<ComplainListData>()

    fun add(complain: ComplainListData) {
        if(!complainList.contains(complain)){
            complainList.add(complain)
        }
    }

    fun getAllComplains(): List<ComplainListData> {
        return complainList

    }
}