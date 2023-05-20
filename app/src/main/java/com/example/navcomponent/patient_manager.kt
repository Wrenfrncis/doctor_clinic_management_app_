package com.example.navcomponent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class patient_manager : Fragment() {

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_patient_manager, container, false)


        database = FirebaseDatabase.getInstance().getReference("patients")

        val recycler = view.findViewById<RecyclerView>(R.id.pat_list21)


        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val patientList = mutableListOf<item_patmanager>()

                for (patientSnapshot in dataSnapshot.children) {
                    val patientName = patientSnapshot.child("name").getValue(String::class.java) ?: ""
                    val birthdate = patientSnapshot.child("birthdate").getValue(String::class.java) ?: ""
                    val diagnosis = patientSnapshot.child("diagnosis").getValue(String::class.java) ?: ""
                    val medication = patientSnapshot.child("medication").getValue(String::class.java) ?: ""

                    patientList.add(item_patmanager(patientName, birthdate, diagnosis, medication))
                }

                recycler.adapter = manager_item_adapter(patientList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }
}
