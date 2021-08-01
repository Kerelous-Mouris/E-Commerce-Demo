package com.example.e_commerce_demo.models

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commerce_demo.R
import com.example.e_commerce_demo.Utils.Communicator
import com.example.e_commerce_demo.databinding.FragmentSignUpBinding
import com.example.e_commerce_demo.firestore.FireStoreClass
import com.example.e_commerce_demo.ui.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser



class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var comm: Communicator
    private var signUpBinding: FragmentSignUpBinding? = null
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

        val binding = FragmentSignUpBinding.inflate(inflater,container,false)
        comm = requireActivity() as Communicator
//        val fireStoreClass = FireStoreClass()
        signUpBinding = binding
        binding.signUpButton.setOnClickListener{
            when{
                TextUtils.isEmpty(binding.registerFName.text.toString().trim {it <= ' '}) ->{
                    (activity as MainActivity)
                        .showErrorSnackBar("Please enter your first name",
                            true)
                }
                TextUtils.isEmpty(binding.registerLName.text.toString().trim {it <= ' '}) ->{
                    (activity as MainActivity)
                        .showErrorSnackBar("please enter your last name",
                            true)
                }
                TextUtils.isEmpty(binding.registerEmail.text.toString().trim {it <= ' '}) ->{
                    (activity as MainActivity)
                        .showErrorSnackBar("please enter your email",
                            true)
                }
                TextUtils.isEmpty(binding.registerPass.text.toString().trim{it <= ' '}) ->{
                    (activity as MainActivity)
                        .showErrorSnackBar("please enter your password",
                            true)
                }
                TextUtils.isEmpty(binding.registerPassConfirm.text.toString().trim{it <= ' '})   ->{
                    (activity as MainActivity)
                        .showErrorSnackBar("please enter your password",
                            true)
                }
                (binding.registerPassConfirm.text.toString() != binding.registerPass.text.toString()) ->{
                    (activity as MainActivity)
                        .showErrorSnackBar("Your confirm password doesn't match your password",
                            true)
                }
                else ->{

                    (activity as MainActivity).showProgressDialog("Registering now")

                    val email: String = binding.registerEmail.text.toString().trim{it <= ' '}
                    val password: String = binding.registerPass.text.toString().trim{it <= ' '}

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                              if (task.isSuccessful){
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    val user = User(
                                        firebaseUser.uid,
                                        binding.registerFName.text.toString().trim{it <= ' '},
                                        binding.registerLName.text.toString().trim{it <= ' '},
                                        binding.registerEmail.text.toString().trim{it <= ' '}
                                    )

                                    FireStoreClass().registerUser(requireActivity() as MainActivity,user)

                                    (activity as MainActivity)
                                        .showErrorSnackBar("You are registered successfully",
                                            false)

                                    comm.goToSignIn("signup")

                                }
                                else{
                                  (activity as MainActivity).hideProgressDialog()
                                    (activity as MainActivity)
                                        .showErrorSnackBar(task.exception!!.message.toString(),
                                            true)

                                }

                            }
                        )

                }

            }
        }
        binding.registerToLoginTxt.setOnClickListener {

            comm.goToSignIn("signup")
        }

        return binding.root
    }

    override fun onDestroyView() {
        signUpBinding = null
        super.onDestroyView()
    }
}