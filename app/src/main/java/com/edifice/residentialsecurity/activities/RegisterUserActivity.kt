package com.edifice.residentialsecurity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.databinding.ActivityRegisterUserBinding
import com.edifice.residentialsecurity.models.ResponseHttp
import com.edifice.residentialsecurity.models.User
import com.edifice.residentialsecurity.providers.UserProvider
import com.edifice.residentialsecurity.util.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterUserActivity : AppCompatActivity() {

    val TAG = "RegisterUserActivity"

    private lateinit var binding: ActivityRegisterUserBinding

    var userProvider = UserProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener{
            register()
        }

    }
    private fun register(){
        val name = binding.edittextName.text.toString()
        val lastname = binding.edittextLastname.text.toString()
        val phone = binding.edittextPhone.text.toString()
        val email = binding.edittextEmail.text.toString()
        val dni = binding.edittextDni.text.toString()
        val password = binding.edittextPassword.text.toString()
        val confirmPassword = binding.edittextConfirmPassword.text.toString()

        if (isValidForm(name = name, lastname = lastname, phone = phone,  email = email, dni = dni, password = password, confirmPassword = confirmPassword)){

            val user = User(
                name = name,
                lastname = lastname,
                phone = phone,
                email = email,
                dni = dni,
                password = password
            )
            userProvider.register(user)?.enqueue(object : Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    if(response.body()?.isSuccess == true){
                        saveUserInSession(response.body()?.data.toString())
                        goToResidential()
                    }
                    
                    Toast.makeText(this@RegisterUserActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "Response: ${response.body()?.message}")
                    Log.d(TAG, "Body: ${response.body()}")
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Se produjo un error ${t.message}")
                    Toast.makeText(this@RegisterUserActivity, "Se produjo un error: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun isValidForm(
        name : String,
        lastname : String,
        email : String,
        phone: String,
        dni : String,
        password : String,
        confirmPassword: String
    ): Boolean{
        if(name.isBlank()){
            Toast.makeText(this, "Debe ingresar su nombre ", Toast.LENGTH_SHORT).show()
            return false
        }
        if(lastname.isBlank()){
            Toast.makeText(this, "Debe ingresar su apellido ", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!email.isEmailValid()){
            Toast.makeText(this, "Debe ingresar un email", Toast.LENGTH_SHORT).show()
            return false
        }
        if(phone.isBlank()){
            Toast.makeText(this, "Debe ingresar un numero de celular", Toast.LENGTH_SHORT).show()
            return false
        }
        if(dni.isBlank()){
            Toast.makeText(this, "Debe ingresar su numero de identificación", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password.isBlank()){
            Toast.makeText(this, "Debe ingresar una contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password.isBlank()){
            Toast.makeText(this, "Debe ingresar una contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if(confirmPassword != password){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private  fun String.isEmailValid(): Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun goToResidential(){
        val i = Intent(this, RegisterResidentialActivity::class.java)
        startActivity(i)
    }

    private fun saveUserInSession(data: String){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)
    }

}