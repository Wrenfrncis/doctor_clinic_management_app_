package com.example.navcomponent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController



class login : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_login,container,false)



        view.findViewById<Button>(R.id.btnlogin).setOnClickListener{
            view.findNavController().navigate(R.id.action_login_to_mainFragment)
        }
        view.findViewById<TextView>(R.id.signupactiv).setOnClickListener{
            view.findNavController().navigate(R.id.action_login_to_register)
        }

        return view
    }


}