package com.example.navcomponent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button
import android.widget.TextView

class login : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        auth = FirebaseAuth.getInstance()

        val emailEditText = view.findViewById<EditText>(R.id.loginusername)
        val passwordEditText = view.findViewById<EditText>(R.id.loginp)
        val loginButton = view.findViewById<Button>(R.id.btnlogin)
        val signUpTextView = view.findViewById<TextView>(R.id.signupactiv)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password, it)
            } else {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        signUpTextView.setOnClickListener {
            view.findNavController().navigate(R.id.action_login_to_register)
        }

        return view
    }

    private fun loginUser(email: String, password: String, view: View) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                    view.findNavController().navigate(R.id.action_login_to_mainFragment)
                } else {
                    Toast.makeText(requireContext(), "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
