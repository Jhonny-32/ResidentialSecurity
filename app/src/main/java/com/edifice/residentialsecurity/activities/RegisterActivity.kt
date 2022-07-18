package com.edifice.residentialsecurity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    val TAG = "RegisterActivity"

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnRegister.setOnClickListener{
            register()
        }

    }

    private fun register() {
        val name = binding.edittextName.text.toString()
        val nit = binding.edittextNit.text.toString()
        val address = binding.edittextAddress.text.toString()
        val refPont = binding.edittextRefPoint.text.toString()

        Log.d(TAG, "En nombre del conjunto es: $name")
        Log.d(TAG, "El NIT: $nit")
        Log.d(TAG, "La direccion es: $address")
        Log.d(TAG, "El punto de referencia es: $refPont")
    }


}