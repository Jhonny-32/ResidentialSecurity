package com.edifice.residentialsecurity.ui.client.update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.databinding.ActivityClientUpdateBinding
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.register.registerResidential.RegisterResidentialViewModel
import com.edifice.residentialsecurity.util.SharedPref
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class ClientUpdateActivity : AppCompatActivity() {

    var user : User? = null
    private var imageFile: File? = null

    private val clientUpdateViewModel : ClientUpdateViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPrefsRepositoryImpl


    private lateinit var binding: ActivityClientUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUserFromSession()


        binding.edittextName.setText(user?.name)
        binding.edittextLastname.setText(user?.lastname)
        binding.edittextPhone.setText(user?.phone)

        if(!user?.image.isNullOrBlank()){
            Glide.with(this).load(user?.image).into(binding.circleimageUser)
        }
        binding.circleimageUser.setOnClickListener{ selectImage() }
        binding.btnUpdate.setOnClickListener{ updateData() }

    }
    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
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
    private fun updateData(){
        user?.name = binding.edittextName.text.toString()
        user?.lastname = binding.edittextLastname.text.toString()
        user?.phone = binding.edittextPhone.text.toString()

        if (imageFile != null){
            clientUpdateViewModel?.updateUserImage(imageFile!!, user!!, user?.sessionToken!!)
        }
        else{
            clientUpdateViewModel?.updateUser(user!!, user?.sessionToken!!)
        }
    }
}