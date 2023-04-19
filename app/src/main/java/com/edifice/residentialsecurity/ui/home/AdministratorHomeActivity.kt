package com.edifice.residentialsecurity.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.databinding.ActivityAdministratorHomeBinding
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.SelectRolesActivity
import com.edifice.residentialsecurity.ui.home.administratorResident.AdministratorDataResidentFragment
import com.edifice.residentialsecurity.ui.home.administratorSecurity.AdministratorDataSecurityFragment
import com.edifice.residentialsecurity.ui.home.administratorOrder.AdministratorOrdersFragment
import com.edifice.residentialsecurity.ui.profile.ClientProfileFragment
import com.edifice.residentialsecurity.util.SharedPref
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdministratorHomeActivity : AppCompatActivity() {

    companion object {
        fun create(context: Context): Intent {
            val intent = Intent(context, AdministratorHomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }
    }

    private lateinit var binding: ActivityAdministratorHomeBinding

    @Inject
    lateinit var sharedPref: SharedPrefsRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdministratorHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openFragment(AdministratorOrdersFragment())
        binding.bottomNavigations.setOnItemSelectedListener{
            when(it.itemId){
                R.id.item_orders -> {
                    clearBackStack()
                    openFragment(AdministratorOrdersFragment())
                    true
                }
                R.id.item_data_security ->{
                    clearBackStack()
                    openFragment(AdministratorDataSecurityFragment())
                    true
                }
                R.id.item_data_resident ->{
                    clearBackStack()
                    openFragment(AdministratorDataResidentFragment())
                    true
                }
                R.id.item_profile ->{
                    clearBackStack()
                    openFragment(ClientProfileFragment())
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
    private fun clearBackStack() {
        val fragmentManager = supportFragmentManager
        for (i in 0 until fragmentManager.backStackEntryCount) {
            fragmentManager.popBackStack()
        }
    }

    private fun getUserFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()

        if(!sharedPref.getData("user").isNullOrBlank()){
            gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }
}