package com.example.navcomponent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
class edit : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var nameEditText: EditText
    private lateinit var diagnoseEditText: EditText
    private lateinit var prescriptionEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var nameSpinner: Spinner
    private lateinit var patientNames: MutableList<String>
    private fun updatePatientInfo() {
        val enteredName = nameSpinner.selectedItem.toString()

        if (enteredName.isNotEmpty()) {
            val query: Query = database.orderByChild("name").equalTo(enteredName)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (patientSnapshot in dataSnapshot.children) {
                            val patientId = patientSnapshot.key
                            val updatedDiagnose = diagnoseEditText.text.toString().trim()
                            val updatedPrescription = prescriptionEditText.text.toString().trim()

                            if (patientId != null) {
                                database.child(patientId).child("diagnosis").setValue(updatedDiagnose)
                                database.child(patientId).child("medication").setValue(updatedPrescription)

                                Toast.makeText(requireContext(), "Patient information updated successfully.", Toast.LENGTH_SHORT).show()
                                Toast.makeText(requireContext(), "Patient information updated successfully.", Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            } else {
                                Toast.makeText(requireContext(), "Error: Patient ID is null.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "No patient found with the entered name.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(requireContext(), "Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "Please select a patient name.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        database = FirebaseDatabase.getInstance().getReference("patients")


        nameSpinner = view.findViewById(R.id.name_spinner)
        patientNames = mutableListOf()


        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (patientSnapshot in snapshot.children) {
                    val patientName = patientSnapshot.child("name").getValue(String::class.java)
                    if (patientName != null) {
                        patientNames.add(patientName)
                    }
                }

                // Populate the Spinner with the patient names
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, patientNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                nameSpinner.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })


        diagnoseEditText = view.findViewById(R.id.diagnose)
        prescriptionEditText = view.findViewById(R.id.editTextTextMultiLine)
        updateButton = view.findViewById(R.id.update)


        updateButton.setOnClickListener {
            updatePatientInfo()
        }

        return view
    }


}