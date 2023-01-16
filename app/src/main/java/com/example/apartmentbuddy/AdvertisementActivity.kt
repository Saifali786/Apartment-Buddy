package com.example.apartmentbuddy

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.apartmentbuddy.databinding.ActivityAdvertisementBinding
import com.example.apartmentbuddy.fragments.*

class AdvertisementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdvertisementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvertisementBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val myToolbar: Toolbar = binding.toolbar
        myToolbar.title = "ADVERTISEMENTS"
        myToolbar.setTitleTextAppearance(this, R.style.CustomActionBarStyle)
        myToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)

        myToolbar.setNavigationOnClickListener { view ->
            finish()
        }

        //Display Advertisement Home Page
        replaceFragment(AdvertisementDisplayFragment())

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.adv_home -> {
                    val bundle = Bundle()
                    bundle.putString("bottomNavValue", "home")
                    val fragment = AdvertisementDisplayFragment()
                    fragment.arguments = bundle
                    replaceFragment(fragment)
                }
                R.id.adv_myPosts -> {
                    val bundle = Bundle()
                    bundle.putString("bottomNavValue", "myPosts")
                    val fragment = AdvertisementDisplayFragment()
                    fragment.arguments = bundle
                    replaceFragment(fragment)
                }
                R.id.adv_bookmark -> {
                    val bundle = Bundle()
                    bundle.putString("bottomNavValue", "bookmark")
                    val fragment = AdvertisementDisplayFragment()
                    fragment.arguments = bundle
                    replaceFragment(fragment)
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (null != fragment) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(binding.fragmentContainer.id, fragment)
            transaction.commit()
        }
    }
}