package com.example.e_commerce_demo.models

data class Favorites (
    val userID:String = "",
    val favoritesIDs:ArrayList<Int> = arrayListOf()
)