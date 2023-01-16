package com.example.apartmentbuddy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.navigation.fragment.findNavController
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.databinding.FragmentRegistration2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Registration : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var selectedRadioButton: RadioButton
    private lateinit var radioGroup: RadioGroup
    private lateinit var progressBar: ProgressBar
    private var db = Firebase.firestore
    private var _registrationBinding: FragmentRegistration2Binding? = null
    private val registrationBinding get() = _registrationBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _registrationBinding = FragmentRegistration2Binding.inflate(inflater, container, false)
        return registrationBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registrationBinding.btnRegister.setOnClickListener {
            performSignup()
        }
        registrationBinding.btnAlreadyLoggedIn.setOnClickListener {
            findNavController().navigate(R.id.action_registration_to_login)
        }
    }

    private fun performSignup() {
        radioGroup = registrationBinding.radioGroup
        val selectedRadioButtonId: Int = radioGroup.checkedRadioButtonId
        selectedRadioButton = requireView().findViewById(selectedRadioButtonId)
        val role: String = selectedRadioButton.text.toString()
        if (registrationBinding.txtEmail.text.isEmpty() || registrationBinding.txtPassword.text.isEmpty() || registrationBinding.txtUsername.text.isEmpty() || registrationBinding.txtAprtnumber.text.isEmpty() || registrationBinding.txtContactNumber.text.isEmpty()) {
            Toast.makeText(requireActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (registrationBinding.txtPassword.text.length < 6) {
            Toast.makeText(
                requireActivity(),
                "Password should be more than 6 characters",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (role == "Tenant") {
            var users_coll = db.collection("user_tenants")
            users_coll.whereEqualTo("Email", registrationBinding.txtEmail.text.toString())
                .get().addOnSuccessListener {
                    if (it.isEmpty()) {
                        Toast.makeText(
                            requireActivity(),
                            "Tenant is not registered with CAPREIT",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        auth.createUserWithEmailAndPassword(
                            registrationBinding.txtEmail.text.toString(),
                            registrationBinding.txtPassword.text.toString()
                        )
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    saveUserInformation()
                                    findNavController().navigate(R.id.action_registration_to_login)
                                    Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast.makeText(
                                        requireActivity(),
                                        "Authentication Failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    requireActivity(),
                                    "Error OCcured ${it.localizedMessage}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
        } else {
            auth.createUserWithEmailAndPassword(
                registrationBinding.txtEmail.text.toString(),
                registrationBinding.txtPassword.text.toString()
            )
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        saveUserInformation()
                        findNavController().navigate(R.id.action_registration_to_login)
                        Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Authentication Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        requireActivity(),
                        "Error OCcured ${it.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }


    }

    private fun saveUserInformation() {
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, apartment no, contact no
            val uid = user.uid
            val name_fragment = registrationBinding.txtUsername
            val apartment_no = registrationBinding.txtAprtnumber
            val contact_no = registrationBinding.txtContactNumber
            radioGroup = registrationBinding.radioGroup
            val selectedRadioButtonId: Int = radioGroup.checkedRadioButtonId

            if (selectedRadioButtonId != -1) {
                selectedRadioButton = requireView()!!.findViewById(selectedRadioButtonId)
                val role: String = selectedRadioButton.text.toString()


                val name_input = name_fragment.text.toString()
                val apartmentno_input = apartment_no.text.toString()
                val contactno_input = contact_no.text.toString()
                val role_input = role

                val userMap = hashMapOf(
                    "name" to name_input,
                    "apartment" to apartmentno_input,
                    "contact" to contactno_input,
                    "role" to role_input,
                    "user_id" to uid

                )
                db.collection("users").document(uid).set(userMap)
                    .addOnSuccessListener {
                        Toast.makeText(requireActivity(), "Successfully added", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            requireActivity(),
                            "Error Occured ${it.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _registrationBinding = null;
    }
}