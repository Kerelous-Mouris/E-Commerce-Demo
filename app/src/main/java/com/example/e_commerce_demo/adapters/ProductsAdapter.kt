package com.example.e_commerce_demo.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.e_commerce_demo.R
import com.example.e_commerce_demo.adapters.ProductsAdapter.*
import com.example.e_commerce_demo.Utils.Communicator
import com.example.e_commerce_demo.models.ProductModel
import kotlinx.android.synthetic.main.product_item.view.*

class ProductsAdapter(val activity: FragmentActivity?,var productsList: MutableList<ProductModel>?):
    RecyclerView.Adapter<ProductsViewHolder>() {

    val comm = activity as Communicator



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {

        val inflater = LayoutInflater.from(parent!!.context)
        val view = inflater.inflate(R.layout.product_item,parent,false)
        return ProductsViewHolder(view).listen{position, type ->
            val item = productsList!![position]
            comm.passProduct(item,"data")
            Toast.makeText(activity!!.applicationContext,"${item.title} is pressed",Toast.LENGTH_SHORT).show()
        }



    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        if (productsList!!.isNotEmpty()){
            var product = productsList!![position]
            holder.onBind(product)
        }
        else
            Log.i("Adapter: ", "list is empty")
    }



    override fun getItemCount(): Int {
        if(productsList!!.isNotEmpty())
            return productsList!!.size
        else
            return 0
    }



    class ProductsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun onBind(product: ProductModel){
            itemView.price_txt.text =  "Price: ".plus(product.price.toString())
            itemView.category_txt.text = product.category
            itemView.name_txt.text = product.title

            Glide.with(itemView)
                .load(product.imageURL)
                .transform(CenterCrop())
                .into(itemView.image)

        }


    }

    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(adapterPosition, itemViewType)
        }
        return this
    }





}