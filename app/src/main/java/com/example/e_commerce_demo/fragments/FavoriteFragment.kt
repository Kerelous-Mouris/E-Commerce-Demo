package com.example.e_commerce_demo.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_demo.R
import com.example.e_commerce_demo.adapters.FavAdapter
import com.example.e_commerce_demo.databinding.FragmentFavoriteBinding
import com.example.e_commerce_demo.firestore.FireStoreClass
import com.example.e_commerce_demo.models.ProductModel
import com.example.e_commerce_demo.ui.MainActivity
import com.example.e_commerce_demo.ui.ProductsVM
import kotlinx.android.synthetic.main.fragment_favorite.*


class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private var favoriteBinding: FragmentFavoriteBinding? = null
    lateinit var mContext: Context
    lateinit var myProductsVM: ProductsVM
    var layoutManager: RecyclerView.LayoutManager? = null
    var ids:ArrayList<Int>? = arrayListOf()
    var adapter: RecyclerView.Adapter<FavAdapter.FavViewHolder>? = null
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
        val binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        favoriteBinding = binding

        owner = this

        binding.favBack.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_favoriteFragment_to_profileFragment)
        }

        (activity as MainActivity).showProgressDialog("Loading")


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FireStoreClass().getFavorites((activity as MainActivity))

        layoutManager = LinearLayoutManager(activity)
        if ((activity as MainActivity).userFavorites != null){
            ids = (activity as MainActivity).userFavorites!!.favoritesIDs
            myProductsVM = ViewModelProvider(this).get(ProductsVM::class.java)
            myProductsVM.initialize(mContext)
            for (i in ids!!){
                myProductsVM.getFavProductWithID(i)
                Log.i("id",i.toString())
                myProductsVM.favMutableList.observe(viewLifecycleOwner,  {
                    (activity as MainActivity).hideProgressDialog()
                    adapter =  FavAdapter((activity as MainActivity),it as MutableList<ProductModel>)
                    fav_rv.adapter = adapter
                    fav_rv.layoutManager = layoutManager
                })
            }
        }

    }
}