package com.example.apartmentbuddy.fragments

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.adapter.CarouselAdapter
import com.example.apartmentbuddy.databinding.FragmentPostApartmentBinding
import com.example.apartmentbuddy.model.Advertisement
import com.example.apartmentbuddy.model.Apartment
import com.example.apartmentbuddy.model.FirebaseAuthUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class PostApartmentFragment(private val advertisementItem: Advertisement?) : Fragment() {
    private lateinit var bottomNavValue: String

    private lateinit var binding: FragmentPostApartmentBinding
    private lateinit var postApartmentButton: Button
    private lateinit var bathroomsEditText: EditText
    private lateinit var bedroomsEditText: EditText
    private lateinit var apartmentEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var rentEditText: EditText
    private lateinit var availabilityEditText: EditText
    private lateinit var contactEditText: EditText
    private lateinit var imageUploadButton: ImageButton

    private val db = FirebaseFirestore.getInstance()
    private val apartmentCollection = db.collection("apartments")
    private var documentId: String? = null

    private var selectedImages = ArrayList<Uri>()
    private var adapter = CarouselAdapter(selectedImages)

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
            postApartmentButton.isEnabled = false
            for (uri in uris) {
                if (uri != null) {
                    uploadImageToFirebase(uri)
                }
            }
            postApartmentButton.isEnabled = true
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostApartmentBinding.inflate(layoutInflater)
        binding.carouselRecyclerview.adapter = adapter
        binding.carouselRecyclerview.apply {
            setInfinite(true)
        }
        bottomNavValue = arguments?.get("bottomNavValue").toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postApartmentButton = view.findViewById(R.id.submit)
        bathroomsEditText = view.findViewById(R.id.bathrooms)
        bedroomsEditText = view.findViewById(R.id.bedrooms)
        apartmentEditText = view.findViewById(R.id.apartment)
        descriptionEditText = view.findViewById(R.id.description)
        rentEditText = view.findViewById(R.id.rent)
        availabilityEditText = view.findViewById(R.id.availability)
        contactEditText = view.findViewById(R.id.contact)
        imageUploadButton = view.findViewById(R.id.addImages)

        // credit to https://stackoverflow.com/a/62139866 for calendar with edit text solution click disable for following two lines
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        availabilityEditText.inputType = InputType.TYPE_NULL;
        availabilityEditText.keyListener = null;

        availabilityEditText.setOnClickListener {
            val datePickDialog = DatePickerDialog(
                requireActivity(),
                { view, pickYear, pickMonth, pickDay ->
                    availabilityEditText.setText("$pickYear/${pickMonth + 1}/$pickDay")
                }, year, month, day
            )
            datePickDialog.show()
        }

        //If the user is editing the existing post
        if (null != advertisementItem && advertisementItem.documentId.isNotBlank()) {
            val advertisement: Apartment = advertisementItem as Apartment
            documentId = advertisement.documentId
            bedroomsEditText.setText(advertisement.noOfBedrooms.toString())
            bathroomsEditText.setText(advertisement.noOfBathrooms.toString())
            apartmentEditText.setText(advertisement.unitNumber)
            descriptionEditText.setText(advertisement.description)
            rentEditText.setText(advertisement.rent.toString())
            availabilityEditText.setText(advertisement.availability)
            contactEditText.setText(advertisement.contact)
            selectedImages = advertisement.photos
            binding.carouselRecyclerview.adapter = CarouselAdapter(selectedImages)
            binding.carouselRecyclerview.apply {
                setInfinite(true)
            }
        }


        postApartmentButton.setOnClickListener {
            if(checkValidation()) {
                val bedrooms = bedroomsEditText.text.toString().trim().toFloat()
                val bathrooms = bathroomsEditText.text.toString().trim().toFloat()
                val apartment = apartmentEditText.text.toString().trim()
                val description = descriptionEditText.text.toString().trim()
                val rent = rentEditText.text.toString().trim().toFloat()
                val availability = availabilityEditText.text.toString().trim()
                val contact = contactEditText.text.toString().trim()
                val userId = FirebaseAuthUser.getUserId().toString()
                val ad = Apartment(
                    "",
                    userId,
                    selectedImages,
                    description,
                    "apartment",
                    contact,
                    bedrooms,
                    bathrooms,
                    apartment,
                    rent,
                    availability,
                    mutableListOf()
                )
                if (documentId == null) {
                    apartmentCollection.document().set(ad)
                        .addOnSuccessListener { void: Void? ->
                            Toast.makeText(
                                activity, "Successfully posted!", Toast.LENGTH_LONG
                            ).show()

                            val bundle = Bundle()
                            bundle.putString("bottomNavValue", bottomNavValue)
                            val fragment = AdvertisementDisplayFragment()
                            fragment.arguments = bundle
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .commit()
                        }.addOnFailureListener { error ->
                            Toast.makeText(
                                activity, error.message.toString(), Toast.LENGTH_LONG
                            ).show()
                        }
                } else {
                    documentId?.let { id ->

                        apartmentCollection.document(id).set(ad, SetOptions.merge())
                            .addOnSuccessListener { void: Void? ->
                                Toast.makeText(
                                    activity, "Listing updated!", Toast.LENGTH_LONG
                                ).show()

                                val bundle = Bundle()
                                bundle.putString("bottomNavValue", bottomNavValue)
                                val fragment = AdvertisementDisplayFragment()
                                fragment.arguments = bundle
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, fragment)
                                    .commit()
                            }.addOnFailureListener { error ->
                                Toast.makeText(
                                    activity, error.message.toString(), Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                }


            }
        }

        binding.cancelButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("bottomNavValue", bottomNavValue)
            val fragment = AdvertisementDisplayFragment()
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        imageUploadButton.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun checkValidation(): Boolean {
        bedroomsEditText.error=null
        bathroomsEditText.error =null
        apartmentEditText.error =null
        descriptionEditText.error =null
        rentEditText.error =null
        availabilityEditText.error =null
        contactEditText.error =null

        var isValid = true
        if (bedroomsEditText.text.toString().trim().isNullOrBlank()) {
            bedroomsEditText.error = "Required Field!"
            isValid = false
        }
        if (bathroomsEditText.text.toString().trim().isNullOrBlank()) {
            bathroomsEditText.error = "Required Field!"
            isValid = false
        }
        if (apartmentEditText.text.toString().trim().isNullOrBlank()) {
            apartmentEditText.error = "Required Field!"
            isValid = false
        }
        if (descriptionEditText.text.toString().trim().isNullOrBlank()) {
            descriptionEditText.error = "Required Field!"
            isValid = false
        }
        if (rentEditText.text.toString().trim().isNullOrBlank()) {
            rentEditText.error = "Required Field!"
            isValid = false
        }
        if (availabilityEditText.text.toString().trim().isNullOrBlank()) {
            availabilityEditText.error = "Required Field!"
            isValid = false
        }
        if (contactEditText.text.toString().trim().length != 10 ) {
            contactEditText.error = "Field should contain 10 digits"
            isValid = false
        }
        return isValid
    }

    private fun uploadImageToFirebase(fileUri: Uri) {
        if (fileUri != null) {
            val fileName = UUID.randomUUID().toString() + ".jpg"

            val refStorage = FirebaseStorage.getInstance().reference.child("apartments/$fileName")

            // credits to https://heartbeat.comet.ml/working-with-firebase-storage-in-android-part-1-a789f9eea037 for the following snippet lines 142-165
            refStorage.putFile(fileUri)
                .addOnProgressListener {
                    // notify the user about current progress
                    val completePercent = (it.bytesTransferred / it.totalByteCount) * 100
                    if (completePercent.toInt() % 10 == 0) {
                        Toast.makeText(
                            requireContext(),
                            "Uploading : ${completePercent}% done",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnSuccessListener {
                    refStorage.downloadUrl.addOnSuccessListener { uri ->
                        selectedImages.add(uri)
                        adapter = CarouselAdapter(selectedImages)
                        binding.carouselRecyclerview.adapter = adapter
                        binding.carouselRecyclerview.apply {
                            setInfinite(true)
                        }
                        Toast.makeText(requireContext(), "Image uploaded!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .addOnFailureListener { ex ->
                    Toast.makeText(
                        activity, "Posting failed due to " + ex.message, Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}