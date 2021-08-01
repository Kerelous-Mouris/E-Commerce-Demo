package com.example.e_commerce_demo.Utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.example.e_commerce_demo.R
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(imageUri: Uri, imageView: ImageView){
        try {
            Glide.with(context)
                .load(imageUri)
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(imageView)
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

}