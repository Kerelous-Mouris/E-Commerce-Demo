package com.example.e_commerce_demo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commerce_demo.R
import com.example.e_commerce_demo.Utils.Communicator
import com.example.e_commerce_demo.Utils.Constants
import com.example.e_commerce_demo.databinding.FragmentCategoriesBinding


class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private lateinit var comm: Communicator
    private var categoriesBinding: FragmentCategoriesBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = FragmentCategoriesBinding.inflate(inflater,container,false)
        categoriesBinding = binding

        comm = requireActivity() as Communicator
        binding.electronics.setOnClickListener{
            comm.passDataCom("electronics",Constants.CATEGORIES)
        }

        binding.jewelery.setOnClickListener{
            comm.passDataCom("jewelery",Constants.CATEGORIES)
        }

        binding.menClothing.setOnClickListener{
            comm.passDataCom("men's clothing",Constants.CATEGORIES)
        }

        binding.womenClothing.setOnClickListener{
            comm.passDataCom("women's clothing",Constants.CATEGORIES)
        }

        return binding.root
    }

    override fun onDestroyView() {
        categoriesBinding = null
        super.onDestroyView()
    }

}