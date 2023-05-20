package com.example.navcomponent
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.util.Log

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class secondfragment : Fragment() {
    var formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }

    private lateinit var nameEditText: EditText
    private lateinit var birthdateEditText: EditText
    private lateinit var diagnosisEditText: EditText
    private lateinit var medicationEditText: EditText


    private var selectedGender: String? = null
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


    private var currentPhotoPath: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storage = FirebaseStorage.getInstance().getReference("patient_photos")

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri = Uri.fromFile(File(currentPhotoPath))
                val patientId = database.push().key
                if (patientId != null) {
                    uploadImage(imageUri, patientId)
                }
            }
        }


    }





    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }



    private fun uploadImage(imageUri: Uri, patientId: String) {
        val fileName = UUID.randomUUID().toString()
        val imageRef = storage.child(patientId).child(fileName)

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                val imageUrl = imageRef.path
                savePatientDetails(patientId, imageUrl)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }
    private fun savePatientDetails(patientId: String, imageUrl: String) {
        val name = nameEditText.text.toString()
        val birthdate = birthdateEditText.text.toString()
        val diagnosis = diagnosisEditText.text.toString()
        val medication = medicationEditText.text.toString()


        if (name.isNotEmpty() && birthdate.isNotEmpty() && diagnosis.isNotEmpty() && medication.isNotEmpty() && selectedGender != null) {
            val patient = Patient(name, birthdate, selectedGender!!, diagnosis, medication, imageUrl)
            database.child("patients").child(patientId).setValue(patient).addOnSuccessListener {
                Toast.makeText(context, "Patient added successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack() // Navigate back
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to add patient", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Please fill all fields and select gender", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_secondfragment, container, false)

        database = FirebaseDatabase.getInstance().reference

        val maleRadioButton = view.findViewById<RadioButton>(R.id.radioButton)
        val femaleRadioButton = view.findViewById<RadioButton>(R.id.female)
        val otherRadioButton = view.findViewById<RadioButton>(R.id.other)
        val addButton = view.findViewById<Button>(R.id.submit_pat)
         var birth=""
        val birthdateEditText2 = view.findViewById<Button>(R.id.birthdate)
        birthdateEditText2.setOnClickListener {
            birth=showDateTimePickerDialog2()
        }

        nameEditText = view.findViewById(R.id.loginusername)
//      birthdateEditText = view.findViewById(R.id.birthdate)
        diagnosisEditText = view.findViewById(R.id.diagnose)
        medicationEditText = view.findViewById(R.id.meds)

        maleRadioButton.setOnClickListener { radioButtonhandler(maleRadioButton) }
        femaleRadioButton.setOnClickListener { radioButtonhandler(femaleRadioButton) }
        otherRadioButton.setOnClickListener { radioButtonhandler(otherRadioButton) }



        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val birthdate = birth
            val diagnosis = diagnosisEditText.text.toString()
            val medication = medicationEditText.text.toString()

            if (name.isNotEmpty()  && diagnosis.isNotEmpty() && medication.isNotEmpty() && selectedGender != null) {
                val patient = Patient(name, birthdate, selectedGender!!, diagnosis, medication)
                val patientId = database.push().key
                if (patientId != null) {
                    database.child("patients").child(patientId).setValue(patient).addOnSuccessListener {
                        Toast.makeText(context, "Patient added successfully", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack() // Navigate back
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed to add patient", Toast.LENGTH_SHORT).show()
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
        // Get the current date and time as a calendar instance
        val currentDateTime = Calendar.getInstance()

        // Create a date picker dialog and set its initial date to the current date
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
    data class Patient(
        val name: String,
        val birthdate: String,
        val gender: String,
        val diagnosis: String,
        val medication: String,
        val imageUrl: String? = null
    )
}
