package com.example.e_commerce_demo.Utils


import com.example.e_commerce_demo.models.ProductModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServices {

    @GET("/products")
    fun getAllProducts(): Call<List<ProductModel>>

    @GET("/products/categories")
    fun getCategories(): Call<List<String>>

    @GET("/products/category/{category}")
    fun getProductsByCategory(@Path("category") category:String) : Call<List<ProductModel>>
    @GET("products/{id}")
    fun getProductWithID(@Path("id")id:Int):Call<ProductModel>
}