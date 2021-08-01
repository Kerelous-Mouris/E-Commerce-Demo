package com.example.e_commerce_demo.Utils

import android.util.Log
import com.example.e_commerce_demo.models.ProductModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductsClient {

    val services: ApiServices
    private const val BASE_URL = "http://fakestoreapi.com/"

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        services = retrofit.create(ApiServices::class.java)
    }

    fun fetchProducts(onSuccess: (productsList: List<ProductModel>) -> Unit,
                      onError: () -> Unit)
    {
        services.getAllProducts().enqueue(object : Callback<List<ProductModel>>{
            override fun onResponse(
                call: Call<List<ProductModel>>,
                response: Response<List<ProductModel>>
            ) {
                onSuccess.invoke(response.body()!!)
            }

            override fun onFailure(call: Call<List<ProductModel>>, t: Throwable) {
                Log.i("Error of Fetching is: ", t.toString())
                onError.invoke()
            }


        })
    }
    fun fetchProductsByCategory(
        category:String,
        onSuccess: (productsList: List<ProductModel>) -> Unit,
        onError: () -> Unit)
    {
        services.getProductsByCategory(category).enqueue(object : Callback<List<ProductModel>>{
            override fun onResponse(
                call: Call<List<ProductModel>>,
                response: Response<List<ProductModel>>
            ) {
                onSuccess.invoke(response.body()!!)
            }

            override fun onFailure(call: Call<List<ProductModel>>, t: Throwable) {
                onError.invoke()
            }
        })
    }

    fun fetchProductWithID(id:Int,
                           onProductFetched:(product: ProductModel)->Unit,
                           onError: () -> Unit){
        services.getProductWithID(id).enqueue(object :Callback<ProductModel>{
            override fun onResponse(call: Call<ProductModel>, response: Response<ProductModel>) {
                onProductFetched.invoke(response.body()!!)
            }

            override fun onFailure(call: Call<ProductModel>, t: Throwable) {
                onError.invoke()
            }

        })
    }
    fun fetchFavProductWithID(id:Int,
                              onFavProductFetched:(product: ProductModel)->Unit,
                              onError: () -> Unit){
        services.getProductWithID(id).enqueue(object :Callback<ProductModel>{
            override fun onResponse(call: Call<ProductModel>, response: Response<ProductModel>) {
                onFavProductFetched.invoke(response.body()!!)
            }

            override fun onFailure(call: Call<ProductModel>, t: Throwable) {
                onError.invoke()
            }

        })
    }

}