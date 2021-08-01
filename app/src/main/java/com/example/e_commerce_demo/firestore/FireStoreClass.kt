package com.example.e_commerce_demo.firestore

import android.util.Log
import android.widget.Toast
import com.example.e_commerce_demo.ui.MainActivity
import com.example.e_commerce_demo.Utils.Constants
import com.example.e_commerce_demo.models.Cart
import com.example.e_commerce_demo.models.Favorites
import com.example.e_commerce_demo.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: MainActivity, userInfo: User){
        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener{
                e ->
                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName,"Error while registering user",
                    e)

            }
    }

    fun updateUserData(activity: MainActivity, selectedGender: String){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update("gender",selectedGender)
            .addOnSuccessListener {
                showUpdateMessage(activity,"gender")
            }
            .addOnFailureListener {
                Toast.makeText(activity,"Couldn't update user gender", Toast.LENGTH_SHORT).show()
            }


    }

    fun updateUserMobile(activity: MainActivity, mobile: String){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update("mobile",mobile)
            .addOnSuccessListener {
                Log.i("mobile","updated successfully")
                showUpdateMessage(activity,"mobile")
            }
            .addOnFailureListener {
                Toast.makeText(activity,"Couldn't update user mobile number", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showUpdateMessage(activity: MainActivity, data: String) {
        when(data){
            "gender"->{
                activity.showErrorSnackBar("user gender updated successfully", false)
            }
            "mobile"->{
                activity.showErrorSnackBar("User mobile number updated successfully", false)
            }
        }

    }

    fun addToFavorites(activity: MainActivity, productFav: Favorites){
        mFireStore.collection(Constants.FAVORITES)
            .document(getCurrentUserID())
            .set(productFav, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(
                    activity,
                    "Added to Favorites",
                    Toast.LENGTH_SHORT
                ).show()
            }


    }

    fun getFavorites(activity: MainActivity){
        mFireStore.collection(Constants.FAVORITES)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {
                document->
                val fav = document.toObject(Favorites::class.java)
                if (fav != null)
                    activity.userFav(fav)
            }

    }

    fun uploadImageUri(activity: MainActivity, imageUri: String){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update("image",imageUri)
            .addOnSuccessListener {
                Toast.makeText(activity,"Image uri uploaded successfully",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity,"Failed to upload",Toast.LENGTH_SHORT).show()
            }
    }

    fun addToCart(activity: MainActivity, cart: Cart){
        mFireStore.collection(Constants.CART)
            .document(getCurrentUserID())
            .set(cart, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(
                    activity,
                    "Added to cart successfully",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    fun getCartList(activity: MainActivity){
        mFireStore.collection(Constants.CART)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {
                document->
                val cartList: Cart? = document.toObject(Cart::class.java)
                if (cartList != null)
                    activity.userCart(cartList)
            }
    }

    fun updateCartList(activity: MainActivity, cart: Cart){
        mFireStore.collection(Constants.CART)
            .document(getCurrentUserID())
            .update("productIDs",cart.productIDs,
                    "quantity",cart.quantity)
            .addOnSuccessListener {
                activity.userCart(cart)
                activity.showErrorSnackBar("cart updated successfully",false)

            }.addOnFailureListener {
                activity.showErrorSnackBar("couldn't update cart",true)
            }


    }


    fun getCurrentUserID():String{

        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }

        return currentUserID

    }


    fun getUserDetails(activity: MainActivity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document->
//                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)
                activity.userData(user)
            }
            .addOnFailureListener {
                Toast.makeText(activity,
                "Failed to get user data",
                Toast.LENGTH_SHORT).show()
            }
    }

    fun updateFavorite(activity: MainActivity, fav: Favorites) {
        mFireStore.collection(Constants.FAVORITES)
            .document(getCurrentUserID())
            .update("favoritesIDs", fav.favoritesIDs)
            .addOnSuccessListener {
                Toast.makeText(
                    activity,
                    "Edited successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    activity,
                    "Couldn't edit Favorites",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


}