package com.example.e_commerce_demo.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart (
    val userID:String = "",
    val productIDs: ArrayList<Int> = arrayListOf<Int>(),
    val quantity:ArrayList<Int> = arrayListOf<Int>()
        ):Parcelable