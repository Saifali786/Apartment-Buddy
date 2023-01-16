package com.example.apartmentbuddy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.apartmentbuddy.R

class ConfirmationComplain : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val views = inflater.inflate(R.layout.fragment_confirmation_message, container, false)
        val buttons: Button =views.findViewById(R.id.ticket_back)
        buttons.setOnClickListener {
            findNavController().navigate(R.id.action_confirmation_complain_to_fragment_complain_home)
        }
        return views
    }

}