package com.example.e_commerce_demo.ui

import com.example.e_commerce_demo.models.ProductModel
import com.google.gson.annotations.SerializedName

data class ProductsResponse (
    @SerializedName("") val results: MutableList<ProductModel>
        )