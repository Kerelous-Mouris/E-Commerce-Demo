package com.example.e_commerce_demo.models
import com.google.gson.annotations.SerializedName


data class ProductModel (
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title:String,
    @SerializedName("price") val price: Double,
    @SerializedName("category")val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("image") val imageURL:String,
    @SerializedName("quantity") val quantity:Int = 1
    ){
    constructor(id: Int, title: String,price: Double,category: String,imageURL: String):this(id, title, price, category, "description", imageURL)
}