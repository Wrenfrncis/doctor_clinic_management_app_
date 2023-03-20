package com.example.navcomponent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class appointment : Fragment() {

    var formatDate = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_appointment, container, false)

        // Set up the click listener for the button
        val calenButton = view.findViewById<Button>(R.id.calenbutton)
        calenButton.setOnClickListener {
            showDateTimePickerDialog()
        }

        return view
    }

    private fun showDateTimePickerDialog() {
        // Get the current date and time as a calendar instance
        val currentDateTime = Calendar.getInstance()

        // Create a date picker dialog and set its initial date to the current date
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                // Update the calendar instance with the selected date
                currentDateTime.set(Calendar.YEAR, year)
                currentDateTime.set(Calendar.MONTH, month)
                currentDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Create a time picker dialog and set its initial time to the current time
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        // Update the calendar instance with the selected time
                        currentDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        currentDateTime.set(Calendar.MINUTE, minute)

                        // Check if the selected time falls within the range of 9am to 3pm
                        if (hourOfDay in 9..14) {
                            // Convert the selected date and time to a string in the desired format
                            val formattedDateTime = formatDate.format(currentDateTime.time)

                            // Set the text view to display the selected date and time
                            val dateTimeTextView = requireView().findViewById<TextView>(R.id.textView4)
                            dateTimeTextView.text = formattedDateTime
                        } else {
                            // Display an error message if the selected time is outside the allowed range

                            Toast.makeText(context,
                                "Please select a time between 9am and 3pm",
                                Toast.LENGTH_LONG)
                                .show()


                        }
                    },
                    currentDateTime.get(Calendar.HOUR_OF_DAY),
                    currentDateTime.get(Calendar.MINUTE),
                    false
                )

                // Show the time picker dialog
                timePickerDialog.show()
            },
            currentDateTime.get(Calendar.YEAR),
            currentDateTime.get(Calendar.MONTH),
            currentDateTime.get(Calendar.DAY_OF_MONTH)
        )

        // Show the date picker dialog
        datePickerDialog.show()
    }
}
