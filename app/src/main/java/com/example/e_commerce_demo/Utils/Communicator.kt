package com.example.e_commerce_demo.Utils

import com.example.e_commerce_demo.models.ProductModel

interface Communicator {

    fun passDataCom(category:String, currentDestination: String)

    fun passProduct(productModel: ProductModel, currentDestination: String)

    fun goToSignIn(currentDestination: String)

    fun goToSignUp(currentDestination: String)

    fun showChip(show: Boolean)

    fun goToForget()
}