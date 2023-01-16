package com.example.apartmentbuddy.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.adapter.AppointmentAdapter
import com.example.apartmentbuddy.databinding.FragmentAppointmentBinding
import com.example.apartmentbuddy.model.AppointmentData
import com.google.firebase.firestore.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Appointment : Fragment() {

    private var _binding: FragmentAppointmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var nameList: MutableList<AppointmentData> = mutableListOf();
    private lateinit var sampleAdapter: AppointmentAdapter
    private val db = FirebaseFirestore.getInstance()
    private val appointmentCollection = db.collection("appointment")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        val myToolbar: Toolbar = binding.toolbar
        myToolbar.inflateMenu(R.menu.appointment_new)
        myToolbar.title = "Appointment"
        myToolbar.setTitleTextAppearance(this.context, R.style.CustomActionBarStyle)
        myToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        myToolbar.setNavigationOnClickListener { view ->
            findNavController().navigate(R.id.action_global_homeAdmin)
        }
        myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_home -> {
                    findNavController().navigate(R.id.action_global_homeAdmin)
                    true
                }
                else -> false
            }
        }
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventChangeListener()
        /**  Reference : https://medium.com/@ezatpanah/recyclerview-in-android-with-example-in-depth-guide-94462a6b573b  */
        sampleAdapter = AppointmentAdapter(nameList)
        binding.apply {
            rvMain.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = sampleAdapter;
            }
        }

    }
    //This method retrieves the name, date,status of all user appointments from firestore database and stores it in the list
    @RequiresApi(Build.VERSION_CODES.O)
    private fun EventChangeListener() {
        appointmentCollection.addSnapshotListener(object : EventListener<QuerySnapshot> {
            //References: https://www.programiz.com/kotlin-programming/examples/current-date-time
            val current_date = LocalDate.now()
            val formatter_date = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val formatted_date = current_date.format(formatter_date)
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                //References: https://www.youtube.com/watch?v=Az4gXQAP-a4
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val date = dc.document.getString("date")
                        if (date.toString() > formatted_date.toString()) {
                            val name: String? = dc.document.getString("name")
                            val date: String? = dc.document.getString("date")
                            val time: String? = dc.document.getString("time")
                            nameList.add(AppointmentData(name = name, date = date, time = time));
                        }
                    }
                    sampleAdapter.notifyDataSetChanged()
                }
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}