package com.example.compareprogramming.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.compareprogramming.R
import com.example.compareprogramming.databinding.FragmentStartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StartFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentStartBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            Login.setOnClickListener { findNavController().navigate(R.id.action_start2_to_login) }
            Signup.setOnClickListener { findNavController().navigate(R.id.action_start2_to_signUp) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater , container ,false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Welcome !"
        auth = Firebase.auth
        if(auth.currentUser != null){
            findNavController().navigate(R.id.action_start2_to_home2)
        }
        return binding.root
    }


}