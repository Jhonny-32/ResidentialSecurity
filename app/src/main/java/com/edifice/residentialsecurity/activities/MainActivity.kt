package com.edifice.residentialsecurity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.activities.client.ClientHomeActivity
import com.edifice.residentialsecurity.databinding.ActivityMainBinding
import com.edifice.residentialsecurity.models.ResponseHttp
import com.edifice.residentialsecurity.models.User
import com.edifice.residentialsecurity.providers.UserProvider
import com.edifice.residentialsecurity.util.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var userProvider = UserProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnLogin.setOnClickListener{ login() }
        getUserFromSession()
    }

    private fun login(){
        val email = binding.edittextEmail.text.toString()
        val password = binding.edittextPassword.text.toString()

        if(isValidForm(email, password)){
            userProvider.login(email,password)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse( call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    if (response.body()?.isSuccess == true){
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        saveUserInSession(response.body()?.data.toString())
                        goToClientHome()
                    }else{
                        Toast.makeText(this@MainActivity, "Los no son correctos", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Hubo un error ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
            //Toast.makeText(this, "El formulario es valido", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "No valido", Toast.LENGTH_SHORT).show()
        }
        
    }

    private fun isValidForm(email: String, password : String): Boolean{
        if(!email.isEmailValid()){
            Toast.makeText(this, "Debe ingresar un correo", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password.isBlank()){
            Toast.makeText(this, "Debe ingresar una contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun String.isEmailValid(): Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun saveUserInSession(data: String){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)
    }

    private fun goToClientHome(){
        val i = Intent(this, ClientHomeActivity::class.java)
        startActivity(i)
    }

    private fun getUserFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()

        if(!sharedPref.getData("user").isNullOrBlank()){
            val user = gson.fromJson(sharedPref.getData("user"), User::class.java)
            goToClientHome()
        }

    }
}