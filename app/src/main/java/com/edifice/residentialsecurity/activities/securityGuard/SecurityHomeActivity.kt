package com.edifice.residentialsecurity.activities.securityGuard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.databinding.ActivitySecurityHomeBinding
import com.edifice.residentialsecurity.fragments.client.ClientOrdersFragment
import com.edifice.residentialsecurity.fragments.client.ClientProfileFragment
import com.edifice.residentialsecurity.fragments.securityGuard.SecurityDataResidentFragment
import com.edifice.residentialsecurity.fragments.securityGuard.SecurityOrdersFragment
import com.edifice.residentialsecurity.models.User
import com.edifice.residentialsecurity.util.SharedPref
import com.google.gson.Gson

class SecurityHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecurityHomeBinding
    var sharedPref: SharedPref? = null

    private val TAG = "SecurityHomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecurityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        openFragment(SecurityOrdersFragment())
        binding.bottomNavigations.setOnItemSelectedListener{
            when(it.itemId){
                R.id.item_orders ->{
                    openFragment(SecurityOrdersFragment())
                    true
                }
                R.id.item_data_resident ->{
                    openFragment(SecurityDataResidentFragment())
                    true
                }
                R.id.item_profile -> {
                    openFragment(ClientProfileFragment())
                    true
                }
                else -> false
            }
        }

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