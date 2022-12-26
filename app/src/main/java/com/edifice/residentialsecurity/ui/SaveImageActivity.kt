package com.edifice.residentialsecurity.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.edifice.residentialsecurity.databinding.ActivitySaveImageBinding
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.providers.UserProvider
import com.edifice.residentialsecurity.util.SharedPref
import java.io.File
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SaveImageActivity : AppCompatActivity() {

    val TAG = "SaveImageActivity"

    private lateinit var binding : ActivitySaveImageBinding
    private var imageFile: File? = null
    private var usersProvider : UserProvider?= null
    var user: User? = null
    var sharedPref: SharedPref ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)
        getUserFromSession()
        usersProvider = UserProvider(user?.sessionToken)

        binding.circleimageUser.setOnClickListener{ selectImage() }
        binding.btnConfirm.setOnClickListener{ saveImage() }
    }

    private fun saveImage(){

        if(imageFile != null && user != null){
            usersProvider?.update(imageFile!!, user!!)?.enqueue(object : Callback<ResponseHttp>{
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    Log.d(TAG, "Response: ${response}")
                    Log.d(TAG, "Body ${response.body()}")

                    saveUserInSession(response.body()?.data.toString())

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                    Toast.makeText(this@SaveImageActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }else{
            Toast.makeText(this, "La imagen no puede se nula ni tampoco los datos de sesion del usuario", Toast.LENGTH_SHORT).show()
        }
    }
    private val startImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == RESULT_OK) {
                val fileUri = data?.data
                imageFile = File(fileUri?.path.toString()) // El ARCHIVO QUE VAMOS A GUARDAR EN EL SERVIDOR
                binding.circleimageUser.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "La tarea se cancelo", Toast.LENGTH_LONG).show()
            }

        }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop() //Para que el usuario pueda corta la imagen
            .compress(1024) //Para comprimir la imagen
            .maxResultSize(1080, 1080)//Maximo de tamaño en imagenes
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }

    }

    private fun getUserFromSession() {
        val gson = Gson()

        if (!sharedPref?.getData("user").isNullOrBlank()) {
            //Si el usuario esta en session
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun saveUserInSession(data: String){
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref?.save("user", user)
    }

}