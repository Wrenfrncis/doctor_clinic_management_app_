package com.example.navcomponent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

import com.google.firebase.database.DatabaseReference



import android.content.Intent

import androidx.activity.result.ActivityResultLauncher

import com.google.firebase.storage.StorageReference

import android.widget.*

class appointment : Fragment() {
    var formatDate = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US)
    private lateinit var nameEditText: EditText
    private lateinit var birthdateEditText: EditText




    private var selectedGender: String? = null
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_appointment, container, false)

        database = FirebaseDatabase.getInstance().reference

        val maleRadioButton = view.findViewById<RadioButton>(R.id.radioButton)
        val femaleRadioButton = view.findViewById<RadioButton>(R.id.female)
        val otherRadioButton = view.findViewById<RadioButton>(R.id.other)
        val addButton = view.findViewById<Button>(R.id.submit_pat)
        var birth=""
        var app_time=""
        val birthdateEditText2 = view.findViewById<Button>(R.id.birthdate)
        birthdateEditText2.setOnClickListener {
            birth=showDateTimePickerDialog2()
//        app_time=showDateTimePickerDialog()

        }
        val appoi = view.findViewById<Button>(R.id.calenbutton)
        appoi.setOnClickListener {
            showDateTimePickerDialog { formattedDateTime ->
                app_time = formattedDateTime
            }
        }
        // Assign the EditText views to the class-level properties
        nameEditText = view.findViewById(R.id.loginusername)
//      birthdateEditText = view.findViewById(R.id.birthdate)


        maleRadioButton.setOnClickListener { radioButtonhandler(maleRadioButton) }
        femaleRadioButton.setOnClickListener { radioButtonhandler(femaleRadioButton) }
        otherRadioButton.setOnClickListener { radioButtonhandler(otherRadioButton) }



        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val birthdate = birth
            val time_app = app_time


            if (name.isNotEmpty()   && selectedGender != null) {
                val patient =
                    appointment1(name, birthdate, selectedGender!!, time_app)
                val patientId = database.push().key
                if (patientId != null) {
                    database.child("appointments").child(patientId).setValue(patient).addOnSuccessListener {
                        Toast.makeText(context, "appointment added successfully", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack() // Navigate back
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to add appointment", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Please fill all fields and select gender", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun radioButtonhandler(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.radioButton ->
                    if (checked) {
                        selectedGender = "Male"
                    }
                R.id.female ->
                    if (checked) {
                        selectedGender = "Female"
                    }
                R.id.other ->
                    if (checked) {
                        selectedGender = "Other"
                    }
            }
        }
    }
    private fun showDateTimePickerDialog2(): String {

        val currentDateTime = Calendar.getInstance()


        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

                currentDateTime.set(Calendar.YEAR, year)
                currentDateTime.set(Calendar.MONTH, month)
                currentDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)



            },
            currentDateTime.get(Calendar.YEAR),
            currentDateTime.get(Calendar.MONTH),
            currentDateTime.get(Calendar.DAY_OF_MONTH)
        )


        datePickerDialog.show()


        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormatter.format(currentDateTime.time)
    }







    private fun showDateTimePickerDialog(callback: (String) -> Unit) {

        val currentDateTime = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

                currentDateTime.set(Calendar.YEAR, year)
                currentDateTime.set(Calendar.MONTH, month)
                currentDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->

                        currentDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        currentDateTime.set(Calendar.MINUTE, minute)


                        val formattedDateTime = formatDate.format(currentDateTime.time)


                        callback(formattedDateTime)
                    },
                    currentDateTime.get(Calendar.HOUR_OF_DAY),
                    currentDateTime.get(Calendar.MINUTE),
                    false
                )


                timePickerDialog.show()
            },
            currentDateTime.get(Calendar.YEAR),
            currentDateTime.get(Calendar.MONTH),
            currentDateTime.get(Calendar.DAY_OF_MONTH)
        )


        datePickerDialog.show()
    }



    data class appointment1(
        val name: String,
        val birthdate: String,
        val gender: String,
        val datetime: String,
    )
}
