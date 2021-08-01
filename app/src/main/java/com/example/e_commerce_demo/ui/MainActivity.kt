package com.example.e_commerce_demo.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import com.example.e_commerce_demo.*
import com.example.e_commerce_demo.Utils.Communicator
import com.example.e_commerce_demo.firestore.FireStoreClass
import com.example.e_commerce_demo.fragments.DataFragment
import com.example.e_commerce_demo.fragments.ItemFragment
import com.example.e_commerce_demo.fragments.LoginFragment
import com.example.e_commerce_demo.models.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), Communicator {


    lateinit var navController: NavController
    lateinit var transaction:FragmentTransaction
    lateinit var dataFragment: DataFragment
    lateinit var itemFragment: ItemFragment
    lateinit var logInFragment: LoginFragment
    lateinit var signUpFragment: SignUpFragment
    var currentUser: User? = null
    var userCart: Cart? = null
    var userFavorites: Favorites? = null
    lateinit var navHostFragment: NavHostFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment



        navController = navHostFragment.navController
        navController.restoreState(savedInstanceState)
        chipNav.setOnItemSelectedListener { id ->
            when (id) {
                R.id.menuHome -> {
                    when ( navController.currentDestination){
                        navController.graph[R.id.itemFragment]->{
                                    navController.navigate(R.id.action_itemFragment_to_dataFragment)
                        }
                        navController.graph[R.id.dataFragment]->{}
                        navController.graph[R.id.categoriesFragment]->{
                            navController.navigate(R.id.action_categoriesFragment_to_dataFragment)
                        }
                        navController.graph[R.id.cartFragment]->{
                            navController.navigate(R.id.action_cartFragment_to_dataFragment)
                        }
                        
                        navController.graph[R.id.profileFragment]->{
                            navController.navigate(R.id.action_profileFragment_to_dataFragment)
                        }

                    }

                }
                R.id.categories -> navController.navigate(R.id.categoriesFragment)
                R.id.cart -> navController.navigate(R.id.cartFragment)

                R.id.profile -> navController.navigate(R.id.profileFragment)
            }
        }



    }




    override fun passDataCom(category: String, currentDestination: String) {
        val bundle = Bundle()
        bundle.putString("category", category)

        when (currentDestination){
            "login"->{
                navController.navigate(R.id.action_loginFragment_to_dataFragment,bundle)
            }
            "categories"->{
                navController.navigate(R.id.action_categoriesFragment_to_dataFragment,bundle)
            }
            "item"->{
                navController.navigate(R.id.action_itemFragment_to_dataFragment,bundle)
            }
        }
    }

    override fun passProduct(productModel: ProductModel, currentDestination: String) {
        val bundle = Bundle()
        bundle.putString("category", productModel.category)
        bundle.putString("title", productModel.title)
        bundle.putString("price", productModel.price.toString())
        bundle.putString("id", productModel.id.toString())
        bundle.putString("description", productModel.description)
        bundle.putString("image", productModel.imageURL)

        when(currentDestination){
            "data"->{
                navController.navigate(R.id.action_dataFragment_to_itemFragment,bundle)
            }
            "categories"->{
                navController.navigate(R.id.action_categoriesFragment_to_itemFragment,bundle)
            }
        }
    }

    override fun goToSignIn(currentDestination: String) {
        if (currentDestination == "start"){
          navController.navigate(R.id.action_startFragment_to_loginFragment)
        }
        else if(currentDestination == "signup")
            navController.navigate(R.id.action_signUpFragment_to_loginFragment)
        else
            navController.navigate(R.id.action_forgetPasswordFragment_to_loginFragment)

    }

    override fun goToSignUp(currentDestination: String) {

        if (currentDestination == "start"){
            navController.navigate(R.id.action_startFragment_to_signUpFragment)
        }
        else
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)

    }

    override fun showChip(show: Boolean) {
        if (show){
            chipNav.setMenuResource(R.menu.nav_menu)
            chipNav.setItemSelected(R.id.menuHome,true)
            navController.navigate(R.id.action_loginFragment_to_dataFragment)
        }
    }

    override fun goToForget() {
        navController.navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        chipNav.setItemSelected(R.id.menuHome,true)
    }

    fun userData(user: User?){
        if (user != null) {
            currentUser = user
            Log.i("user first name: ", user!!.firstName)
            Log.i("user last name: ", user!!.lastName)
            Log.i("user email: ", user!!.email)
        }

    }

    fun userCart(cart:Cart){
        if(cart != null && (cart.productIDs != null)){
            userCart = cart
        }
        else{
            userCart = Cart(FireStoreClass().getCurrentUserID())
        }
    }

    fun userFav(fav: Favorites?){
        if(fav != null)
            userFavorites = fav
        else
            userFavorites = Favorites(FireStoreClass().getCurrentUserID())
    }
}