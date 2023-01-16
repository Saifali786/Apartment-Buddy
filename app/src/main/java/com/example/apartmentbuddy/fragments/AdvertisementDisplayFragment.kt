package com.example.apartmentbuddy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.adapter.AdvertisementViewPagerAdapter
import com.example.apartmentbuddy.databinding.FragmentAdvertisementHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator

class AdvertisementDisplayFragment : Fragment() {
    private lateinit var binding: FragmentAdvertisementHomeBinding
    private val tabOptionsArray = arrayOf("Apartments", "Items")
    private lateinit var fab: FloatingActionButton
    private lateinit var bottomNavValue: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdvertisementHomeBinding.inflate(layoutInflater)
        bottomNavValue = arguments?.get("bottomNavValue").toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = binding.advViewPager
        viewPager.adapter =
            AdvertisementViewPagerAdapter(parentFragmentManager, lifecycle, bottomNavValue)

        val tabLayout = binding.advTabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabOptionsArray.get(position)
        }.attach()

        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            val tabPosition = tabLayout.selectedTabPosition
            if (tabPosition == 0) {
                val bundle = Bundle()
                bundle.putString("bottomNavValue", bottomNavValue)
                val fragment = PostApartmentFragment(null)
                fragment.arguments = bundle

                parentFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, fragment)?.commit()
            } else if (tabPosition == 1) {
                val bundle = Bundle()
                bundle.putString("bottomNavValue", bottomNavValue)
                val fragment = PostItemFragment(null)
                fragment.arguments = bundle

                parentFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, fragment)?.commit()
            }
        }
    }
}