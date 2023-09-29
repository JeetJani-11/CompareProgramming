package com.example.compareprogramming.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.compareprogramming.R
import com.example.compareprogramming.databinding.FragmentSignUpBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val database =
        FirebaseDatabase.getInstance("https://comp-1111-default-rtdb.firebaseio.com/")
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FirebaseApp.initializeApp(this.requireContext())
        auth = Firebase.auth

        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Sign Up"
        binding.CreateAccount.setOnClickListener {
            auth.createUserWithEmailAndPassword(
                binding.EmailEdittext.text.toString(),
                binding.PasswordEdittext.text.toString()
            ).addOnCompleteListener(this.requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val ref = database.getReference("Users")
                    ref.child(user!!.uid).child("cfid")
                        .setValue(binding.CfidEdittext.text.toString())
                    ref.child(user.uid).child("uname")
                        .setValue(binding.UsernameEdittext.text.toString())
                    findNavController().navigate(R.id.action_signUp_to_home22)
                } else {
                    Toast.makeText(
                        this.requireContext(),
                        task.exception?.message,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }


        }

    }


}
