package com.example.navcomponent

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.navcomponent.databinding.FragmentMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import android.view.animation.AnimationUtils
import androidx.navigation.NavOptions
import java.text.SimpleDateFormat
import java.util.*



class MainFragment : Fragment() {
    private fun isAdminUser(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.email == "admindoctor@gmail.com"
    }

    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: FragmentMainBinding

    // Define NavOptions for fragment transitions
    private val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()

    fun nav_to_add_patient() {
        findNavController().navigate(R.id.action_mainFragment_to_secondfragment, null, navOptions)
    }

    fun nav_to_create_appo() {
        findNavController().navigate(R.id.action_mainFragment_to_appointment, null, navOptions)
    }

    fun nav_to_patientmanager() {
        findNavController().navigate(R.id.action_mainFragment_to_patient_manager, null, navOptions)
    }

    fun nav_to_edit_patient() {
        findNavController().navigate(R.id.action_mainFragment_to_edit, null, navOptions)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databaseReference = FirebaseDatabase.getInstance().getReference("appointments")
        binding = FragmentMainBinding.inflate(inflater)
        binding.fragment = this

        val recycler = binding.patList21
        fetchAppointments(recycler)

        if (isAdminUser()) {
            binding.addPatient1.visibility = View.VISIBLE
            binding.patmanager.visibility = View.VISIBLE
            binding.editpageid.visibility = View.VISIBLE
            recycler.visibility = View.VISIBLE // showRecyclerView for admin users
        } else {
            binding.addPatient1.visibility = View.GONE
            binding.patmanager.visibility = View.GONE
            binding.editpageid.visibility = View.GONE
            recycler.visibility = View.GONE // hidRECYCLERVIEW for non-admin users
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        binding.patList21.startAnimation(animation)
    }

    private fun fetchAppointments(recycler: RecyclerView) {
        val appointmentsList = mutableListOf<ItemPat>()

        databaseReference.orderByChild("datetime").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                appointmentsList.clear()
                for (data in snapshot.children) {
                    val name = data.child("name").getValue(String::class.java)
                    val datetime = data.child("datetime").getValue(String::class.java)
                    val parts = datetime?.split(" ")
                    val date = parts?.getOrNull(0)
                    val time = if (parts?.size ?: 0 >= 2) parts?.getOrNull(1) else null

                    if (name != null && datetime != null) {
                        val inputFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a")
                        val dateTimeObj = inputFormat.parse(datetime)
                        val today = Date()
                        val formatter = SimpleDateFormat("dd/MM/yyyy")
                        val formattedDate = formatter.format(today)
                        if (date == formattedDate) {
                            val outputFormat = SimpleDateFormat("hh:mm a")
                            val formattedTime = time?.let { outputFormat.format(dateTimeObj) }
                            val appointment = ItemPat(name, " $formattedTime")
                            appointmentsList.add(appointment)
                        }
                    }
                }
                recycler.adapter = patadapter(appointmentsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainFragment", "Error fetching appointments", error.toException())
            }
        })
    }
}
