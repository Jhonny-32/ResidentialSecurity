package com.edifice.residentialsecurity.activities.client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.activities.MainActivity
import com.edifice.residentialsecurity.databinding.ActivityClientHomeBinding
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

        binding.btnLogout.setOnClickListener{ logout() }

    }

    private fun getUserFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()

        if(!sharedPref.getData("user").isNullOrBlank()){
            val user = gson.fromJson(sharedPref.getData("user"), User::class.java)
            Log.d(TAG, "usuario: $user")
        }

    }

    private fun logout(){
        sharedPref?.remove("user")
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

}