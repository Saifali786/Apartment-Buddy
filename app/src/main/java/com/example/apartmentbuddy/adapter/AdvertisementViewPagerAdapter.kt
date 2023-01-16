package com.example.apartmentbuddy.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.apartmentbuddy.fragments.AdvertisementDisplayFragment
import com.example.apartmentbuddy.fragments.ApartmentFragment
import com.example.apartmentbuddy.fragments.ItemsFragment

private const val TOTAL_TABS = 2

class AdvertisementViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var bottomNavValue: String
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return TOTAL_TABS
    }

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putString("bottomNavValue", bottomNavValue)

        when (position) {
            0 -> {
                val fragment = ApartmentFragment()
                fragment.arguments = bundle
                return fragment
            }
            1 -> {
                val fragment = ItemsFragment()
                fragment.arguments = bundle
                return fragment
            }
            else -> {
                val fragment = ApartmentFragment()
                fragment.arguments = bundle
                return fragment
            }
        }
    }
}