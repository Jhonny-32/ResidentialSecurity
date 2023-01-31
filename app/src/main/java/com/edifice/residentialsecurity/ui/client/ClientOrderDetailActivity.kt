package com.edifice.residentialsecurity.ui.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.data.model.Order
import com.edifice.residentialsecurity.databinding.ActivityClientOrderDetailBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientOrderDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientOrderDetailBinding

    var order : Order? = null
    val gson = Gson()
    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


    }
}