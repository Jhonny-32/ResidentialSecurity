package com.edifice.residentialsecurity.ui.client.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.databinding.ActivityClientHomeBinding
import com.edifice.residentialsecurity.ui.client.clientFragment.ClientOrdersFragment
import com.edifice.residentialsecurity.ui.profile.ClientProfileFragment
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.util.SharedPref
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClientHomeActivity : AppCompatActivity() {

    companion object {
        fun create(context: Context): Intent =
            Intent(context, ClientHomeActivity::class.java)
    }

    private lateinit var binding: ActivityClientHomeBinding

    @Inject
    lateinit var sharedPref: SharedPrefsRepositoryImpl

    private val TAG = "ClientHomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
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