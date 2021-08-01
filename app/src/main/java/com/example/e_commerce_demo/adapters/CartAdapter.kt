package com.example.e_commerce_demo.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.e_commerce_demo.ui.MainActivity
import com.example.e_commerce_demo.R
import com.example.e_commerce_demo.firestore.FireStoreClass
import com.example.e_commerce_demo.models.Cart
import com.example.e_commerce_demo.models.ProductModel
import kotlinx.android.synthetic.main.cart_item.view.*

class CartAdapter(val activity: MainActivity, var cartList: MutableList<ProductModel>, private var quantity:MutableList<Int> ): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var cartListIDs: ArrayList<Int> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent!!.context)
        val view = inflater.inflate(R.layout.cart_item,parent,false)
        return CartViewHolder(view).listen{ activity, position, number ->
            if (number > 0){
                quantity[position] = number
                cartListIDs.removeAll(cartListIDs)
                cartList.forEach{
                    cartListIDs.add(it.id)
                }
                val cart = Cart(FireStoreClass().getCurrentUserID(),cartListIDs,
                    quantity as ArrayList<Int>
                )
                FireStoreClass().updateCartList(activity,cart)
                notifyDataSetChanged()
            }
            else if (number == 0){
                Log.i("position", position.toString())
                Log.i("No. elements", cartList.size.toString())
                cartList.remove(cartList[position])
                Log.i("No. elements", cartList.size.toString())
                cartListIDs.removeAll(cartListIDs)
                cartList.forEach{
                    cartListIDs.add(it.id)
                }
                Log.i("No. elements of ids", cartListIDs.size.toString())
                quantity.removeAt(position)
                val cart = Cart(FireStoreClass().getCurrentUserID(),cartListIDs,
                    quantity as ArrayList<Int>
                )
                FireStoreClass().updateCartList(activity,cart)
                notifyDataSetChanged()
            }
        }.listen {
            activity, position ->
            Log.i("No. elements", cartList.size.toString())
            cartList.remove(cartList[position])
            Log.i("No. elements after", cartList.size.toString())
            cartListIDs.removeAll(cartListIDs)
            cartList.forEach{
                cartListIDs.add(it.id)
            }
            Log.i("No. elements of ids", cartListIDs.size.toString())

            quantity.removeAt(position)
            val cart = Cart(FireStoreClass().getCurrentUserID(),cartListIDs,
                quantity as ArrayList<Int>
            )
            FireStoreClass().updateCartList(activity,cart)
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        if (cartList!!.isNotEmpty()){
            var product = cartList!![position]
            holder.onBind(product,quantity,position)
        }
    }

    override fun getItemCount(): Int = cartList!!.size

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(product: ProductModel, quantity:MutableList<Int>, position: Int){
                itemView.cart_elegant.setRange(0,20)
                itemView.cart_price.text = "Price: ".plus((product.price * quantity[position]).toString())
                itemView.cart_elegant.number = quantity[position].toString()
                Glide.with(itemView.cart_image)
                    .load(product.imageURL)
                    .transform(CenterCrop())
                    .into(itemView.cart_image)
                itemView.cart_title.text = product.title
                itemView.cart_quantity.text = itemView.cart_elegant.number
            }


    }
    fun <T : RecyclerView.ViewHolder> T.listen(event: (activity: MainActivity, position: Int, number: Int) -> Unit): T {
        itemView.cart_elegant.setOnValueChangeListener{elegantNumberButton: ElegantNumberButton, i: Int, i1: Int ->
            event.invoke(activity,adapterPosition,i1)
        }
        return this
    }
    fun <T : RecyclerView.ViewHolder> T.listen(event: (activity: MainActivity, position: Int) -> Unit): T {
        itemView.cart_remove.setOnClickListener{
            event.invoke(activity,adapterPosition)
        }
        return this
    }


}