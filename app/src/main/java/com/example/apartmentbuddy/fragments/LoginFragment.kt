package com.example.apartmentbuddy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.databinding.FragmentLogin2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private var _loginBinding: FragmentLogin2Binding? = null
    private val loginBinding get() = _loginBinding!!
    private lateinit var auth: FirebaseAuth
    private var navigate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _loginBinding = FragmentLogin2Binding.inflate(inflater, container, false)
        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginBinding.txtviewNewUser.setOnClickListener {
            findNavController()
                .navigate(R.id.action_login_to_registration)
        }
        loginBinding.btnLoginApp.setOnClickListener {
            performLogin()
        }
        loginBinding.txtViewForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgetPassword)
        }
    }


    private fun performLogin() {
        val email = loginBinding.txtEmail
        val password = loginBinding.txtPassword

        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(requireActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        val emailInput = loginBinding.txtEmail.text.toString()
        val passwordInput = loginBinding.txtPassword.text.toString()
        navigate = emailInput;


        auth.signInWithEmailAndPassword(emailInput, passwordInput)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user!!.email == "admin@dal.ca") {
                        findNavController().navigate(R.id.action_login_to_homeAdmin)
                    } else {
                        findNavController().navigate(R.id.action_login_to_home2)
                    }
                    Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        requireActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireActivity(),
                    "Error Occured ${it.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _loginBinding = null;
    }
}