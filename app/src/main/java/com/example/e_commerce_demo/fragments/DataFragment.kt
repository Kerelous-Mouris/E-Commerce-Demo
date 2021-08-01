package com.example.e_commerce_demo.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_demo.R
import com.example.e_commerce_demo.Utils.Communicator
import com.example.e_commerce_demo.adapters.ProductsAdapter
import com.example.e_commerce_demo.databinding.FragmentDataBinding
import com.example.e_commerce_demo.models.ProductModel
import com.example.e_commerce_demo.ui.MainActivity
import com.example.e_commerce_demo.ui.ProductsVM
import kotlinx.android.synthetic.main.fragment_data.*
import kotlinx.coroutines.*

class DataFragment : Fragment(R.layout.fragment_data) {



    private var category:String? = ""
    lateinit var   mContext: Context
    lateinit var productsViewModel: ProductsVM
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>? = null
    private var dataBinding: FragmentDataBinding? = null
    lateinit var comm: Communicator
    lateinit var owner: ViewModelStoreOwner


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = FragmentDataBinding.inflate(inflater,container,false)



        owner = this

        category = arguments?.getString("category")


        this.chooseProducts(category)

        return binding.root
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chooseProducts(category)


    }

    fun chooseProducts(category: String?){
        if (category.isNullOrEmpty()){
            runBlocking {
                getProducts()
            }
        }
        else{
            runBlocking {
                getProductsByCategory(category)
            }
        }
    }

    suspend fun getProducts(){
        GlobalScope.launch(Dispatchers.IO){
        productsViewModel = ViewModelProvider(owner).get(ProductsVM::class.java)
        productsViewModel.initialize(mContext)
        productsViewModel.getProducts()
            withContext(Dispatchers.Main){

                productsViewModel.productsList.observe(viewLifecycleOwner,
                    { t ->
                        (activity as MainActivity).hideProgressDialog()
                        layoutManager = LinearLayoutManager(activity)
                        adapter = ProductsAdapter(requireActivity(),t as MutableList<ProductModel>?)
                        products_rv.adapter = adapter
                        products_rv.layoutManager = layoutManager

                    })
            }
        }
    }

    suspend fun getProductsByCategory(category:String?) {
        GlobalScope.launch(Dispatchers.IO) {
        productsViewModel = ViewModelProvider(owner).get(ProductsVM::class.java)
        productsViewModel.initialize(mContext)
        if (category != null) {
            productsViewModel.getProductsByCategory(category)
        }
            withContext(Dispatchers.Main){
                productsViewModel.productsList.observe(viewLifecycleOwner,
                    { t ->
                        (activity as MainActivity).hideProgressDialog()
                        layoutManager = LinearLayoutManager(activity)
                        adapter = ProductsAdapter(requireActivity(),t as MutableList<ProductModel>?)
                        products_rv.adapter = adapter
                        products_rv.layoutManager = layoutManager

                    })
            }
        }
    }

    override fun onDestroyView() {
        dataBinding = null
        super.onDestroyView()
    }
}




