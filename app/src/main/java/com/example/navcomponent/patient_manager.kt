package com.example.navcomponent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class patient_manager : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_patient_manager, container, false)

        val carList2 = listOf(
            item_patmanager("John Doe", "01/01/1990", "Flu", "Ibuprofen"),
            item_patmanager("Jane Doe", "05/10/1985", "Migraine", "Aspirin"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
            item_patmanager("John Doe", "01/01/1990", "Flu", "Ibuprofen"),
            item_patmanager("Jane Doe", "05/10/1985", "Migraine", "Aspirin"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
            item_patmanager("Bob Smith", "12/25/1975", "High Blood Pressure", "Losartan"),
        )

        val recycler = view.findViewById<RecyclerView>(R.id.pat_list21)
        recycler.adapter = manager_item_adapter(carList2)

        return view
    }
}
