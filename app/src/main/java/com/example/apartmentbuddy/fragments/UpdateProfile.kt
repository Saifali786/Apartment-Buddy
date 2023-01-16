package com.example.apartmentbuddy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.databinding.FragmentUpdateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateProfile : Fragment() {
    private var _updateProfileBinding: FragmentUpdateProfileBinding? = null
    private val updateProfileBinding get() = _updateProfileBinding!!
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _updateProfileBinding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return updateProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = Firebase.auth.currentUser
        user?.let {
            val currentUserID = user.uid
            loadProfileData(currentUserID, user.email.toString())
            updateProfileBinding.btnProfileSubmit.setOnClickListener {
                updateProfileData()
            }
            updateProfileBinding.btnProfileBack.setOnClickListener {
                if (user!!.email == "admin@dal.ca")
                    findNavController().navigate(R.id.action_updateProfile_to_homeAdmin)
                else
                    findNavController().navigate(R.id.action_updateProfile_to_home2)
            }
        }
    }

    private fun loadProfileData(UserId: String, UserEmail: String) {
        var users_coll = db.collection("users")
        users_coll.whereEqualTo("user_id", UserId)
            .get().addOnSuccessListener {
                if (it.isEmpty()) {
                    Toast.makeText(requireActivity(), "No Collections found", Toast.LENGTH_SHORT)
                        .show()
                }
                for (doc in it) {
                    updateProfileBinding.txtProfileHeaderName.setText(doc.data["name"].toString())
                    updateProfileBinding.txtProfileEmail.setText(UserEmail)
                    updateProfileBinding.txtProfileUsername.setText(doc.data["name"].toString())
                    updateProfileBinding.txtProfileApartmentNumber.setText(doc.data["apartment"].toString())
                    updateProfileBinding.txtProfileNumber.setText(doc.data["contact"].toString())
                    updateProfileBinding.txtHiddenRole.setText(doc.data["role"].toString())
                }
            }
    }

    private fun updateProfileData() {
        val user = Firebase.auth.currentUser
        //update email
        if (updateProfileBinding.txtProfileEmail.text.isBlank()) {
            user!!.updateEmail(updateProfileBinding.txtProfileEmail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        requireActivity(),
                        "Error Occured ${it.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addOnFailureListener;
                }
        }
        if (updateProfileBinding.txtProfilePassword.text.isNotEmpty()) {
            user!!.updatePassword(updateProfileBinding.txtProfilePassword.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        requireActivity(),

                        "Error Occured ${it.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addOnFailureListener;

                }
        }
        if (updateProfileBinding.txtProfileNumber.text.isNotEmpty() && updateProfileBinding.txtProfileUsername.text.toString()
                .isNotEmpty() &&
            updateProfileBinding.txtProfileApartmentNumber.text.isNotEmpty()
        ) {

            val userMap = hashMapOf(
                "name" to updateProfileBinding.txtProfileUsername.text.toString(),
                "apartment" to updateProfileBinding.txtProfileApartmentNumber.text.toString(),
                "contact" to updateProfileBinding.txtProfileNumber.text.toString(),
                "role" to updateProfileBinding.txtHiddenRole.text.toString(),
                "user_id" to user!!.uid.toString()

            )


            db.collection("users").document(user!!.uid.toString()).set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireActivity(),
                        "Profile Details Updated Successfully",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    findNavController().navigate(R.id.action_updateProfile_to_home2)
                }
                .addOnFailureListener {
                    Toast.makeText(
                        requireActivity(),
                        "Error Occured ${it.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addOnFailureListener;
                }
        } else {
            Toast.makeText(
                requireActivity(),
                "Please fill all fields",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _updateProfileBinding = null;
    }
}
