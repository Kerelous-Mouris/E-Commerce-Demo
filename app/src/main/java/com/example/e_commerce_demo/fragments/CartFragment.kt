package com.example.e_commerce_demo.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_demo.R
import com.example.e_commerce_demo.adapters.CartAdapter
import com.example.e_commerce_demo.databinding.FragmentCartBinding
import com.example.e_commerce_demo.firestore.FireStoreClass
import com.example.e_commerce_demo.models.ProductModel
import com.example.e_commerce_demo.ui.MainActivity
import com.example.e_commerce_demo.ui.ProductsVM
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : Fragment(R.layout.fragment_cart) {


    lateinit var myProductsVM: ProductsVM
    lateinit var mContext: Context
    var layoutManager: RecyclerView.LayoutManager? = null
    var quantity:ArrayList<Int> = arrayListOf()
    var ids:ArrayList<Int>? = null

    var adapter: RecyclerView.Adapter<CartAdapter.CartViewHolder>? = null
    private var cartBinding: FragmentCartBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentCartBinding.inflate(inflater,container,false)
        cartBinding = binding

        (activity as MainActivity).showProgressDialog("Loading")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        FireStoreClass().getCartList((activity as MainActivity))


        layoutManager = LinearLayoutManager(activity)
        if ((activity as MainActivity).userCart != null){
            ids = (activity as MainActivity).userCart!!.productIDs
            quantity = (activity as MainActivity).userCart!!.quantity
            myProductsVM = ViewModelProvider(this).get(ProductsVM::class.java)
            myProductsVM.initialize(mContext)
            for (i in ids!!){
                myProductsVM.getProductWithID(i)
                Log.i("id",i.toString())
                myProductsVM.cartMutableList.observe(viewLifecycleOwner, Observer {
                        it->
                    (activity as MainActivity).hideProgressDialog()
                    adapter =  CartAdapter((activity as MainActivity),it as MutableList<ProductModel>, quantity)
                    Toast.makeText(mContext,"noticed change",Toast.LENGTH_SHORT).show()
                    cart_rv.adapter = adapter
                    cart_rv.layoutManager = layoutManager
                })

            }


        }
    }


    override fun onDestroyView() {
        cartBinding = null
        super.onDestroyView()
    }




}