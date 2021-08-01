package com.example.e_commerce_demo.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commerce_demo.R
import com.example.e_commerce_demo.Utils.Communicator
import com.example.e_commerce_demo.databinding.FragmentForgetPasswordBinding
import com.example.e_commerce_demo.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordFragment : Fragment(R.layout.fragment_forget_password) {

    private var forgetPasswordBinding: FragmentForgetPasswordBinding? = null
    lateinit var mContext: Context
    lateinit var comm: Communicator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = FragmentForgetPasswordBinding.inflate(inflater,container,false)
        forgetPasswordBinding = binding
        comm = activity as Communicator

        binding.resetBtn.setOnClickListener {
            val email: String = binding.forgetPassEmailTxt.text.toString().trim{it <= ' '}
            if (email.isEmpty()){
                (activity as MainActivity).showErrorSnackBar("Please enter your email to reset password", true)
            }else{
                (activity as MainActivity).showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {task->
                        (activity as MainActivity).hideProgressDialog()
                        if (task.isSuccessful){
                            (activity as MainActivity)
                                .showErrorSnackBar(getString(R.string.reset_email_sent), false)
                            comm.goToSignIn("forget")
                        }else{
                            (activity as MainActivity).showErrorSnackBar(task.exception!!.message.toString(),true)
                        }

                    }
            }
        }

        binding.returnToSignIn.setOnClickListener {
            comm.goToSignIn("forget")
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        forgetPasswordBinding = null
    }
}