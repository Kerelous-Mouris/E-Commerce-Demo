package com.example.e_commerce_demo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commerce_demo.Utils.Communicator
import com.example.e_commerce_demo.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    lateinit var comm: Communicator
    private var startBinding: FragmentStartBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentStartBinding.inflate(inflater,container,false)
        startBinding = binding
        comm = requireActivity() as Communicator
        binding.startSignIn.setOnClickListener {
            comm.goToSignIn("start")
        }
        binding.startSignUp.setOnClickListener {
            comm.goToSignUp("start")
        }
        return binding.root
    }


}