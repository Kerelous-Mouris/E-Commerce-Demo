package com.example.e_commerce_demo.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commerce_demo.R
import com.example.e_commerce_demo.Utils.Communicator
import com.example.e_commerce_demo.databinding.FragmentLoginBinding
import com.example.e_commerce_demo.firestore.FireStoreClass
import com.example.e_commerce_demo.ui.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginFragment : Fragment(R.layout.fragment_login) {


    lateinit var comm: Communicator
    private var loginBinding: FragmentLoginBinding? = null
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
        val binding = FragmentLoginBinding.inflate(inflater,container,false)
        loginBinding = binding
        comm = activity as Communicator
        binding.signInButton.setOnClickListener{
            when{
                TextUtils.isEmpty(binding.loginEmail.text.toString().trim {it <= ' '}) ->{
                    (activity as MainActivity)
                        .showErrorSnackBar("Please enter your email", true)
                }

                TextUtils.isEmpty(binding.loginPass.text.toString().trim{it <= ' '}) ->{
                    (activity as MainActivity)
                        .showErrorSnackBar("Please enter your password", true)
                }
                else ->{
                    (activity as MainActivity).showProgressDialog("Logging in")

                    val email: String = binding.loginEmail.text.toString().trim{it <= ' '}
                    val password: String = binding.loginPass.text.toString().trim{it <= ' '}

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if (task.isSuccessful){
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    FireStoreClass().getUserDetails(activity as MainActivity)
                                    FireStoreClass().getCartList((activity as MainActivity))
                                    FireStoreClass().getFavorites((activity as MainActivity))

                                    (activity as MainActivity)
                                        .showErrorSnackBar("Logged in successfully", false)

//                                    comm.passDataCom("")
//                                    (activity as MainActivity).navController.graph.remove((activity as MainActivity).navController.currentDestination!!)
                                    comm.showChip(true)

                                }
                                else{
                                    (activity as MainActivity).hideProgressDialog()
                                    (activity as MainActivity)
                                        .showErrorSnackBar(task.exception!!.message.toString(), true)
                                }

                            }
                        )

                }

            }
        }

        binding.forgetPassTxt.setOnClickListener {
//            (activity as MainActivity).navController.graph.remove((activity as MainActivity).navController.currentDestination!!)
            comm.goToForget()
        }

        binding.loginToRegisterTxt.setOnClickListener {
//            (activity as MainActivity).navController.graph.remove((activity as MainActivity).navController.currentDestination!!)
            comm.goToSignUp("login")
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loginBinding = null
    }
}