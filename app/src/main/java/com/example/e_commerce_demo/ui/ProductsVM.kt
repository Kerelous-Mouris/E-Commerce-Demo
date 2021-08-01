package com.example.e_commerce_demo.ui

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_commerce_demo.Utils.ProductsClient
import com.example.e_commerce_demo.models.ProductModel

class ProductsVM :  ViewModel() {

    var productsList: MutableLiveData<List<ProductModel>> = MutableLiveData()
    var cartMutableList: MutableLiveData<List<ProductModel>> = MutableLiveData()
    var cartList: ArrayList<ProductModel> = ArrayList()
    var favMutableList: MutableLiveData<List<ProductModel>> = MutableLiveData()
    var favList: ArrayList<ProductModel> = ArrayList()

    lateinit var context: Context

    fun initialize(context: Context){
        this.context = context
    }

   fun getProductWithID(id:Int){
       ProductsClient.fetchProductWithID(id,
       ::onProductFetched,
       ::onError)
   }
    fun getFavProductWithID(id:Int){
       ProductsClient.fetchFavProductWithID(id,
       ::onFavProductFetched,
       ::onError)
   }

    private fun onProductFetched(productModel: ProductModel) {
            cartList.add(productModel)
            cartMutableList.value = cartList
    }
    private fun onFavProductFetched(productModel: ProductModel) {
            favList.add(productModel)
            favMutableList.value = favList
    }

    fun getProducts(){
        ProductsClient.fetchProducts(
            ::onProductsFetched,
            ::onError)
    }
    fun getProductsByCategory(category: String){
        ProductsClient.fetchProductsByCategory(category,
            ::onProductsFetched,
            ::onError)
    }

    private fun onError() {
        Toast.makeText(context,"Failed to fetch products!", Toast.LENGTH_SHORT).show()
    }

    fun onProductsFetched(mutableList: List<ProductModel>){
        this.productsList.value = mutableList
    }


}