package com.edifice.residentialsecurity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.adapters.RolesAdapter
import com.edifice.residentialsecurity.databinding.ActivitySelectRolesBinding
import com.edifice.residentialsecurity.models.User
import com.edifice.residentialsecurity.util.SharedPref
import com.google.gson.Gson

class SelectRolesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectRolesBinding
    var user: User?= null
    var adapter: RolesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectRolesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerviewRoles.layoutManager = LinearLayoutManager(this)

        getUserFromSession()

        adapter = RolesAdapter(user?.roles!!, this)
        binding.recyclerviewRoles.adapter = adapter

    }
    private fun getUserFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()

        if(!sharedPref.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }
}