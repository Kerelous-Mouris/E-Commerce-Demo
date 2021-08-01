package com.example.e_commerce_demo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.e_commerce_demo.ui.MainActivity
import com.example.e_commerce_demo.R
import com.example.e_commerce_demo.firestore.FireStoreClass
import com.example.e_commerce_demo.models.Favorites
import com.example.e_commerce_demo.models.ProductModel
import kotlinx.android.synthetic.main.favorite_item.view.*

class FavAdapter(val activity: MainActivity, var favList: MutableList<ProductModel>?): RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    var favListIDs: ArrayList<Int> = arrayListOf()

    class FavViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun onBind(fav: ProductModel) {
            itemView.fav_title.text = fav.title
            itemView.fav_category.text = fav.category
            Glide.with(itemView.context)
                .load(fav.imageURL)
                .transform(CenterCrop())
                .into(itemView.fav_image)
        }
    }

    fun <T : RecyclerView.ViewHolder> T.listen(event: (activity: MainActivity, position: Int) -> Unit): T {
        itemView.fav_delete.setOnClickListener {
            event.invoke(activity, adapterPosition)
        }
        return this
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.favorite_item,parent,false)
        return FavViewHolder(view).listen{
            activity, position ->
            favListIDs.removeAll(favListIDs)
            favList!!.remove(favList!![position])
            favList!!.forEach {
                favListIDs.add(it.id)
            }

            val fav = Favorites(FireStoreClass().getCurrentUserID(),favListIDs)
            FireStoreClass().updateFavorite(activity,fav)
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        if (favList!!.isNotEmpty()){
            var fav = favList!![position]
            holder.onBind(fav)
        }
    }

    override fun getItemCount(): Int= favList!!.size
}