package com.example.e_commerce_demo.fragments

import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.ViewModelProvider


import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.e_commerce_demo.R
import com.example.e_commerce_demo.Utils.Communicator
import com.example.e_commerce_demo.databinding.FragmentItemBinding
import com.example.e_commerce_demo.firestore.FireStoreClass
import com.example.e_commerce_demo.models.Cart
import com.example.e_commerce_demo.models.Favorites
import com.example.e_commerce_demo.models.ProductModel
import com.example.e_commerce_demo.ui.MainActivity
import com.example.e_commerce_demo.ui.ProductsVM
import kotlinx.android.synthetic.main.fragment_item.*
import java.math.RoundingMode
import java.text.DecimalFormat


class ItemFragment : Fragment(R.layout.fragment_item) {


    private var itemBinding: FragmentItemBinding? = null
    var imageUrl: String? = null
    var description: String? = null
    var price: String? = null
    var id: Int? = null
    var title: String? = null
    var category: String? = null
    private var popup_showing = false
    var  popupWindow: PopupWindow? = null
    lateinit var cartList: ArrayList<Int>
    var favList: ArrayList<Int> = arrayListOf()
    lateinit var productsVM: ProductsVM
    lateinit var communicator: Communicator
    lateinit var cart:Cart
    var quantityList:ArrayList<Int> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        productsVM = ViewModelProvider(this).get(ProductsVM::class.java)

        val binding = FragmentItemBinding.inflate(inflater,container,false)
        itemBinding = binding

        cartList = ArrayList()


        communicator = activity as Communicator

        imageUrl = arguments?.getString("image")
        category = arguments?.getString("category")
        title = arguments?.getString("title")
        description = arguments?.getString("description")
        price = arguments?.getString("price")
        id = arguments?.getString("id")!!.toInt()


        Glide.with(binding.itemImage)
            .load(imageUrl)
            .transform(CenterCrop())
            .into(binding.itemImage)
        binding.itemCategory.text = "Category: ".plus(category)
        binding.itemDescription.text = description
        binding.itemPrice.text = price
        binding.itemTitle.text = title

        binding.addFab.setOnClickListener { view ->

            val myView = LayoutInflater.from(requireActivity()).inflate(R.layout.popup_view,null)

            // Initialize a new instance of popup window
            if (!popup_showing){
            popupWindow = PopupWindow(
                myView, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
            )}

            // Set an elevation for the popup window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow!!.elevation = 10.0F
            }


            // If API level 23 or higher then execute the code
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.TOP
                popupWindow!!.enterTransition = slideIn

                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.RIGHT
                popupWindow!!.exitTransition = slideOut

            }


            // Get the widgets reference from custom view
            val popupTitle = myView.findViewById<TextView>(R.id.popup_title)
            val popupImage = myView.findViewById<ImageView>(R.id.popup_image)
            val popupElegant = myView.findViewById<ElegantNumberButton>(R.id.popup_elegant_btn)
            val popupFav = myView.findViewById<Button>(R.id.popup_fav)
            val popupAdd = myView.findViewById<Button>(R.id.popup_add)
            val popupQuantity = myView.findViewById<TextView>(R.id.quantity_txt)
            val popupPrice = myView.findViewById<TextView>(R.id.totalPrice_txt)
            popupTitle.text = title
            Glide.with(popupImage)
                .load(imageUrl)
                .transform(CenterCrop())
                .into(popupImage)
            popupElegant.setRange(1,20)
            popupElegant.number = "1"
            popupQuantity.text = "Quantity: ".plus(popupElegant.number)
            popupPrice.text = "Price: ".plus(price!!.toDouble() * popupElegant.number.toInt())

            popupElegant.setOnValueChangeListener{ elegantNumberButton: ElegantNumberButton, i: Int, i1: Int ->
                popupQuantity.text = "Quantity: ".plus(popupElegant.number.toString())
                popupPrice.text = "Price: ".plus(getNoMoreThanTwoDigits((price!!.toDouble() * popupElegant.number.toString().toInt())).toString())
            }


            // Set click listener for popup window's text view
            popupFav.setOnClickListener{
                // Change the text color of popup window's text view
                popupFav.setBackgroundColor(getColor(requireContext(), R.color.purple_700))
                val product = ProductModel(id!!,title!!, price!!.toDouble(),category!!,description!!,imageUrl!!,popupElegant.number.toInt())
                if ((activity as MainActivity).userFavorites != null){
                    if ((activity as MainActivity).userFavorites!!.favoritesIDs.isNullOrEmpty()){
                        favList.add(product.id)
                        val productFav = Favorites(FireStoreClass().getCurrentUserID(),favList)
                        FireStoreClass().addToFavorites((activity as MainActivity), productFav)
                        FireStoreClass().getFavorites(activity as MainActivity)


                    }
                    else{
                        (activity as MainActivity).userFavorites!!.favoritesIDs.add(product.id)
                        FireStoreClass().addToFavorites((activity as MainActivity), (activity as MainActivity).userFavorites!!)
                        FireStoreClass().getFavorites(activity as MainActivity)


                    }
                }else{
                    favList.add(product.id)
                    val productFav = Favorites(FireStoreClass().getCurrentUserID(),favList)
                    FireStoreClass().addToFavorites((activity as MainActivity), productFav)
                    FireStoreClass().getFavorites(activity as MainActivity)



                }

            }

            // Set a click listener for popup's button widget
            popupAdd.setOnClickListener{
                // Dismiss the popup window

                val product = ProductModel(id!!,title!!, price!!.toDouble(),category!!,description!!,imageUrl!!,popupElegant.number.toInt())
                if ((activity as MainActivity).userCart != null){
                if ((activity as MainActivity).userCart!!.productIDs.isNullOrEmpty()){
                    cartList.add(product.id)
                    quantityList.add(popupElegant.number.toInt())
                    cart = Cart(FireStoreClass().getCurrentUserID(),cartList,quantityList)
                    FireStoreClass().addToCart((activity as MainActivity),cart )
                    FireStoreClass().getCartList((activity as MainActivity))

                }
                else{
                    (activity as MainActivity).userCart!!.productIDs.add(product.id)
                    (activity as MainActivity).userCart!!.quantity.add(popupElegant.number.toInt())
                    FireStoreClass().addToCart(activity as MainActivity, (activity as MainActivity).userCart!!)
                    FireStoreClass().getCartList((activity as MainActivity))

                }
                }else{
                    cartList.add(product.id)
                    quantityList.add(popupElegant.number.toInt())
                    cart = Cart(FireStoreClass().getCurrentUserID(),cartList,quantityList)
                    FireStoreClass().addToCart((activity as MainActivity),cart)
                    FireStoreClass().getCartList((activity as MainActivity))

                }
                popupWindow!!.dismiss()
            }

            // Set a dismiss listener for popup window
            popupWindow!!.setOnDismissListener {
                popup_showing = false
                Toast.makeText(requireContext(),"Popup closed", Toast.LENGTH_SHORT).show()
            }


            // Finally, show the popup window on app
            TransitionManager.beginDelayedTransition(fragment_root)

            if(!popup_showing) {
                Log.i("is Showing: " , "${popup_showing}")
                popup_showing = true
                popupWindow!!.showAtLocation(
                    fragment_root, // Location to display popup window
                    Gravity.CENTER, // Exact position of layout to display popup
                    0, // X offset
                    0 // Y offset
                )
            }
            else{

                Toast.makeText(requireContext(),"Window is showing", Toast.LENGTH_SHORT).show()
            }
        }






        binding.backFab.setOnClickListener{
                view->
                if(!popup_showing) {
                    (activity as MainActivity).navController.popBackStack(R.id.dataFragment,false)
                }
                else
                    popupWindow!!.dismiss()


        }

        return binding.root
    }

    fun getNoMoreThanTwoDigits(number: Double): String {
        val format = DecimalFormat("0.##")
        //The rule of discarding decimals is not retained, RoundingMode.FLOOR means discarding directly.
        format.roundingMode = RoundingMode.FLOOR
        return format.format(number)
    }


}