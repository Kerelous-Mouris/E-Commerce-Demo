package com.example.e_commerce_demo.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.e_commerce_demo.R
import com.example.e_commerce_demo.Utils.Constants
import com.example.e_commerce_demo.Utils.GlideLoader
import com.example.e_commerce_demo.databinding.FragmentProfileBinding
import com.example.e_commerce_demo.firestore.FireStoreClass
import com.example.e_commerce_demo.models.User
import com.example.e_commerce_demo.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class ProfileFragment : Fragment(R.layout.fragment_profile) {


    private var profileBinding: FragmentProfileBinding? = null
    private var user: User? = null
    var edits : Int= 0
    lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentProfileBinding.inflate(inflater,container,false)
        profileBinding = binding

        (activity as MainActivity).showProgressDialog("Loading")
        val user = (activity as MainActivity).currentUser
        if (!user!!.gender.isNullOrEmpty()){
            when(user!!.gender){
                "male"->{
                    binding.profileRbMale.isChecked = true
                }
                "female"->{
                    binding.profileRbFemale.isChecked = true
                }
            }
        }

        if (user!!.mobile != null || !user!!.mobile.equals(0)){
            binding.profileMobile.setText(user!!.mobile)
        }

        if (!user!!.image.isNullOrEmpty()){
            GlideLoader(requireContext())
                .loadUserPicture(Uri.parse(user!!.image)
                    ,profileBinding!!.profileImg)

        }

        (activity as MainActivity).hideProgressDialog()
        binding.profileSaveUpdate.setOnClickListener {
            (activity as MainActivity).showProgressDialog("Saving edits")
            when(binding.profileRbMale.isChecked){
                    true ->{
                        if (user!!.gender == "female")
                            FireStoreClass().updateUserData((activity as MainActivity),"male")
                        else
                            edits = 1

                    }
                    false ->{
                        if (user!!.gender == "male")
                            FireStoreClass().updateUserData((activity as MainActivity),"female")
                        else
                            edits = 1
                    }
            }
            if (user!!.mobile == binding.profileMobile.text.toString()) {
                if (edits == 1)
                    edits = 2
                else
                    edits = 1
            }
            else
                FireStoreClass().updateUserMobile((activity as MainActivity), binding.profileMobile.text.toString())
            if (edits == 2){
                (activity as MainActivity).showErrorSnackBar("There is nothing to update", true)
                edits = 0
            }
            (activity as MainActivity).hideProgressDialog()
        }

        binding.profileImg.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            ){
//                Constants.showImageChooser((activity as MainActivity))
                val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )

                startActivityForResult(galleryIntent, Constants.PICK_IMAGE_REQUEST_CODE)
            }
            else{
                ActivityCompat.requestPermissions(
                    (activity as MainActivity),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        binding.profileFavorites.setOnClickListener {
//            (activity as MainActivity).showProgressDialog("Loading")
            (activity as MainActivity).navController.navigate(R.id.action_profileFragment_to_favoriteFragment)
        }

        binding.profileSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            (activity as MainActivity).chipNav.setMenuResource(R.menu.empty_menu)
            (activity as MainActivity).navController.navigate(R.id.action_profile_to_start)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if ((activity as MainActivity).currentUser != null){
            user = (activity as MainActivity).currentUser
            profileBinding!!.profileEmail.text = user!!.email
            profileBinding!!.profileUserName.text = user!!.firstName.plus(" ").plus(user!!.lastName)
        }
        else{
            Toast.makeText(requireContext(),"user is null", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        profileBinding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                (activity as MainActivity).showErrorSnackBar("The storage permission is granted", false)
//                Constants.showImageChooser(activity as MainActivity)
                val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )

                startActivityForResult(galleryIntent, Constants.PICK_IMAGE_REQUEST_CODE)
            }
            else{
                Toast.makeText(
                    activity,
                    "Permission denied",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                if (data != null){
                    try {
                        val selectedImageFileUri = data.data!!

                        Log.i("image Uri", "we reached here and here is the uir:   ".plus(selectedImageFileUri.toString()))
//                        profileBinding!!.profileImg.setImageURI(selectedImageFileUri)
                        GlideLoader(requireContext()).loadUserPicture(selectedImageFileUri,profileBinding!!.profileImg)

                        FireStoreClass().uploadImageUri(activity as MainActivity, selectedImageFileUri.toString())

                    }catch (e : IOException){
                        e.printStackTrace()
                        Toast.makeText(requireContext(),
                        "Failed to get Image",
                        Toast.LENGTH_SHORT).show()
                    }
                }
                else
                    Toast.makeText(requireContext(),"data is null", Toast.LENGTH_LONG).show()

            }
            else{
                Toast.makeText(requireContext(),"request code doesn't match", Toast.LENGTH_LONG).show()
            }
        }
        else{
            Toast.makeText(requireContext(),"result code doesn't match", Toast.LENGTH_LONG).show()
        }



    }
}