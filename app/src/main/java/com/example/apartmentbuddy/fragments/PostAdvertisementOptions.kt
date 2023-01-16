package com.example.apartmentbuddy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.apartmentbuddy.databinding.FragmentPostAdvertisementOptionsBinding

class PostAdvertisementOptions : Fragment() {
    private lateinit var binding: FragmentPostAdvertisementOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostAdvertisementOptionsBinding.inflate(layoutInflater)
        return binding.root
    }
}