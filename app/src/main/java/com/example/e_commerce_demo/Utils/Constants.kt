package com.example.e_commerce_demo.Utils

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap

object Constants {
    const val READ_STORAGE_PERMISSION_CODE: Int = 2
    const val PICK_IMAGE_REQUEST_CODE: Int = 1
    const val USERS: String = "users"
    const val CART: String = "cart"
    const val FAVORITES: String = "favorites"
    const val CATEGORIES : String = "categories"
    const val DATA: String = "data"
    const val USER_PROFILE_IMAGE:String = "User_Profile_Image"

    fun getFileExtension(activity: Activity, uri:Uri?): String?{
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}