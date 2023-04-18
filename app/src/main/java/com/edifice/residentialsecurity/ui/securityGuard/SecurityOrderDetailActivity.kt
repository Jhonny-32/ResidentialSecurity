package com.edifice.residentialsecurity.ui.securityGuard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.denzcoskun.imageslider.models.SlideModel
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.data.model.Order
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.databinding.ActivitySecurityOrderDetailBinding
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SecurityOrderDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecurityOrderDetailBinding

    var order : Order? = null
    val gson = Gson()
    var toolbar: Toolbar? = null
    private val securityViewModel: SecurityViewModel by viewModels()
    var user : User?=null

    @Inject
    lateinit var sharedPrefsRepositoryImpl: SharedPrefsRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecurityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUserFromSession()

        order = gson.fromJson(intent.getStringExtra("order"), Order::class.java)

        toolbar = findViewById(R.id.toolbar_simple)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        toolbar?.title = "Order #${order?.id}"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(order?.image1))
        imageList.add(SlideModel(order?.image2))
        imageList.add(SlideModel(order?.image3))

        binding.imageslider.setImageList(imageList)
        binding.texviewDescription.text = order?.descriptions
        binding.textviewClient.text = "${order?.client?.name} ${order?.client?.lastname}"
        binding.textviewResidential.text = order?.name
        binding.textviewDate.text = order?.timestamp
        binding.textviewStatus.text = order?.status

        Log.d("ORDER", "Order: ${order.toString()}")

        if (order?.status == "ASIGNADO"){
            binding.btnUpdate.visibility = View.VISIBLE
        }

        binding.btnUpdate.setOnClickListener { securityViewModel.updateOrder(order!!, user?.sessionToken!!) }

    }

    fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPrefsRepositoryImpl.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPrefsRepositoryImpl.getData("user"), User::class.java)
        }
    }
}