package com.edifice.residentialsecurity.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.edifice.residentialsecurity.ui.adapters.RolesAdapter
import com.edifice.residentialsecurity.databinding.ActivitySelectRolesBinding
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectRolesActivity : AppCompatActivity() {

    companion object {
        fun create(context: Context): Intent {
            val intent = Intent(context, SelectRolesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }
    }
    private lateinit var binding: ActivitySelectRolesBinding
    var user: User?= null

    @Inject
    lateinit var adapter: RolesAdapter

    @Inject
    lateinit var sharedPref: SharedPrefsRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectRolesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }
    private fun initUi(){
        initObservers()
    }

    private fun initObservers() {
        getUserFromSession()
        adapter = RolesAdapter(user?.roles!!, sharedPref,this )
        binding.recyclerviewRoles.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewRoles.adapter = adapter
    }

    private fun getUserFromSession(){
        val gson = Gson()
        if(!sharedPref.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }
}