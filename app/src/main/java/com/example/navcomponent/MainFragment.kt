package com.example.navcomponent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.navcomponent.databinding.FragmentMainBinding


class MainFragment : Fragment() {

fun nav_to_add_patient(){
    findNavController().navigate(R.id.action_mainFragment_to_secondfragment)
}
fun nav_to_create_appo(){
    findNavController().navigate(R.id.action_mainFragment_to_appointment)
}
fun nav_to_patientmanager(){
    findNavController().navigate(R.id.action_mainFragment_to_edit)
}
fun nav_to_edit_patient(){
    findNavController().navigate(R.id.action_mainFragment_to_patient_manager)
}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.fragment=this

        val view=inflater.inflate(R.layout.fragment_main,container,false)



        // Adding the carList to the RecyclerView adapter
        val recycler = binding.patList21
        val carList = listOf(
            ItemPat("abc1", "12:00 am"),
            ItemPat("Citroen", "11:00 am"),
            ItemPat("Ford", "10:00 am"),
            ItemPat("Skoda", "2:00 pm"),
            ItemPat("Ford", "3:00 pm"),
            ItemPat("Opel", "4:00 pm"),
            ItemPat("Hyundai", "5:00 pm"),
            ItemPat("Ford", "6:00 pm"),
            ItemPat("Peugeot", "7:00 pm"),
            ItemPat("Fiat", "8:00 pm"),
            ItemPat("Volkswagen", "9:00 pm"),
            ItemPat("Skoda", "10:00 pm"),
            ItemPat("Volkswagen", "11:00 pm"),
        )
        recycler.adapter = patadapter(carList)


//        create the same type carlist with data name,birthdate,diagnoses,prescription




        return binding.root
    }

}