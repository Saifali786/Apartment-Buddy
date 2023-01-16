package com.example.apartmentbuddy.fragments

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.model.Appointment

class NewAppointment : Fragment() {

    private var mHour: Int = 0
    private var mMinute: Int = 0
    private var dayMonth: Int = 0
    private var monthToday: Int = 0
    private var selected_date: String = ""
    private var selected_time: String = ""
    private val appointment = Appointment()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new_appointment, container, false)

        val btnTimePicker: Button = view.findViewById(R.id.btn_time)
        val txtTime: EditText = view.findViewById(R.id.appointment_time)

        val myToolbar: Toolbar = view.findViewById(R.id.toolbar) as Toolbar
        myToolbar.inflateMenu(R.menu.appointment_new)
        myToolbar.title = "New Appointment"
        myToolbar.setTitleTextAppearance(this.context, R.style.CustomActionBarStyle)
        myToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        myToolbar.setNavigationOnClickListener { view ->
            view.findNavController()
                .navigate(NewAppointmentDirections.actionNewAppointmentToAppointmentHome())
        }
        myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_home -> {
                    findNavController().navigate(R.id.action_global_home22)
                    true
                }
                else -> false
            }
        }

        val calendarView: CalendarView = view.findViewById(R.id.appointment_date)

        var lastSelectedCalendar = Calendar.getInstance();

        calendarView.minDate = lastSelectedCalendar.timeInMillis - 1000
        calendarView.maxDate = System.currentTimeMillis() + 1209600000
        calendarView.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            monthToday = month + 1
            dayMonth = dayOfMonth
            selected_date = "$dayOfMonth/$month/$year"
            val checkCalendar = Calendar.getInstance()
            checkCalendar[year, month] = dayOfMonth
            if (checkCalendar.equals(lastSelectedCalendar)) return@OnDateChangeListener
            lastSelectedCalendar = checkCalendar

        })


        btnTimePicker.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val datetime: Calendar = Calendar.getInstance()
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);
            val timePickerDialog = TimePickerDialog(
                view.context,
                // Reference : https://www.geeksforgeeks.org/timepicker-in-kotlin/
                { view, hourOfDay, minute ->
                    if (dayMonth > datetime.get(Calendar.DATE) || monthToday - 1 > datetime.get(
                            Calendar.MONTH
                        )
                    ) {
                        selected_time = appointment.printValidTime(hourOfDay, minute)
                        txtTime.setText(selected_time)
                    } else {
                        if (hourOfDay >= datetime.get(Calendar.HOUR_OF_DAY)) {
                            if (hourOfDay == datetime.get(Calendar.HOUR_OF_DAY) && minute <= datetime.get(
                                    Calendar.MINUTE
                                )
                            ) {
                                Toast.makeText(
                                    context,
                                    "Invalid time selection, please select future time",
                                    Toast.LENGTH_SHORT
                                ).show();
                            } else {
                                selected_time = appointment.printValidTime(hourOfDay, minute)
                                txtTime.setText(selected_time)
                            }
                        } else {
                            //select current before
                            Toast.makeText(
                                context,
                                "Invalid time selection, please select future time",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                },
                mHour,
                mMinute,
                false
            )
            timePickerDialog.show()
        }

        val proceed: Button = view.findViewById(R.id.new_appointment_proceed)

        proceed.setOnClickListener {
            if (selected_date.isNotEmpty() && selected_time.isNotEmpty()) {
                view.findNavController().navigate(
                    NewAppointmentDirections.actionNewAppointmentToAppointmentNotes(
                        selected_date,
                        selected_time
                    )
                )
            } else {
                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_LONG).show()
            }

        }

        val back: Button = view.findViewById(R.id.appointment_back)

        back.setOnClickListener {
            view.findNavController()
                .navigate(NewAppointmentDirections.actionNewAppointmentToAppointmentHome())
        }
        return view
    }
}
