package com.example.apartmentbuddy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.databinding.FragmentForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ForgetPassword : Fragment() {
    private  var _forgetBinding: FragmentForgetPasswordBinding?=null
    private val forgetBinding get()=_forgetBinding!!
    private lateinit var auth: FirebaseAuth
    private var db=Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth= Firebase.auth
        _forgetBinding= FragmentForgetPasswordBinding.inflate(inflater,container,false)
        return forgetBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forgetBinding.btnSubmitForgetPassword.setOnClickListener{
            auth.sendPasswordResetEmail(forgetBinding.txtEmailForgetPwd.text.toString())
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        findNavController().navigate(R.id.action_forgetPassword_to_login)
                        Toast.makeText(requireActivity(),"Success", Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(requireActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireActivity(), "Error OCcured ${it.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
        }

        forgetBinding.btnGoLogin.setOnClickListener{
            findNavController().navigate(R.id.action_forgetPassword_to_login)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _forgetBinding=null;
    }




}