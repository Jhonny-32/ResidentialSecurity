package com.edifice.residentialsecurity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.edifice.residentialsecurity.databinding.ActivityRegisterBinding
import com.edifice.residentialsecurity.models.Residential
import com.edifice.residentialsecurity.models.ResponseHttp
import com.edifice.residentialsecurity.providers.ResidentialsProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterResidentialActivity : AppCompatActivity() {

    val TAG = "RegisterActivity"

    private lateinit var binding: ActivityRegisterBinding

    var residentialsProvider = ResidentialsProvider()

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

        if(isValidForm(name = name, nit=nit, address = address, refPont = refPont)){

            val residential = Residential(
                name = name,
                nit = nit,
                address = address
            )

            residentialsProvider.register(residential)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Toast.makeText(this@RegisterResidentialActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "Response: ${response.body()?.message}")
                    Log.d(TAG, "Body: ${response.body()}")
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Se produjo un error ${t.message}")
                    Toast.makeText(this@RegisterResidentialActivity, "Se produjo un error: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })

        }


        Log.d(TAG, "En nombre del conjunto es: $name")
        Log.d(TAG, "El NIT: $nit")
        Log.d(TAG, "La direccion es: $address")
        Log.d(TAG, "El punto de referencia es: $refPont")
    }

    private fun isValidForm(
        name : String,
        nit : String,
        address : String,
        refPont: String
    ): Boolean{
        if(name.isBlank()){
            Toast.makeText(this, "Debe ingresar el nombre del conjunto residencial", Toast.LENGTH_SHORT).show()
            return false
        }
        if(nit.isBlank()){
            Toast.makeText(this, "Debe ingresar el NIT", Toast.LENGTH_SHORT).show()
            return false
        }
        if(address.isBlank()){
            Toast.makeText(this, "Debe ingresar la dirección del conjunto residencial", Toast.LENGTH_SHORT).show()
            return false
        }
        if(refPont.isBlank()){
            Toast.makeText(this, "Punto de referencia", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}