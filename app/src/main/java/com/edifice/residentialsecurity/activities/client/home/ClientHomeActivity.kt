package com.edifice.residentialsecurity.activities.client.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.databinding.ActivityClientHomeBinding
import com.edifice.residentialsecurity.fragments.client.ClientOrdersFragment
import com.edifice.residentialsecurity.fragments.client.ClientProfileFragment
import com.edifice.residentialsecurity.models.User
import com.edifice.residentialsecurity.util.SharedPref
import com.google.gson.Gson

class ClientHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientHomeBinding
    var sharedPref: SharedPref? = null

    private val TAG = "ClientHomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)
        openFragment(ClientOrdersFragment())
        binding.bottomNavigations.setOnItemSelectedListener{

            when(it.itemId){
                R.id.item_profile ->{
                    openFragment(ClientProfileFragment())
                    true
                }
                R.id.item_orders ->{
                    openFragment(ClientOrdersFragment())
                    true
                }
                else -> false
            }

        }
        getUserFromSession()
    }

    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun getUserFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()

        if(!sharedPref.getData("user").isNullOrBlank()){
            val user = gson.fromJson(sharedPref.getData("user"), User::class.java)
            Log.d(TAG, "usuario: $user")
        }

    }
}