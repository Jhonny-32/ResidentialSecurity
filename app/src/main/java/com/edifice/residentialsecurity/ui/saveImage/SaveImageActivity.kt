package com.edifice.residentialsecurity.ui.saveImage

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.core.ex.dismissKeyboard
import com.edifice.residentialsecurity.databinding.ActivitySaveImageBinding
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.home.AdministratorHomeActivity
import com.edifice.residentialsecurity.ui.login.MainActivity
import com.edifice.residentialsecurity.ui.register.registerResidential.RegisterResidentialActivity
import com.edifice.residentialsecurity.util.SharedPref
import java.io.File
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class SaveImageActivity : AppCompatActivity() {

    companion object {
        fun create(context: Context): Intent = Intent(context, SaveImageActivity::class.java)
    }

    @Inject
    lateinit var sharedPref: SharedPrefsRepositoryImpl

    private val saveImageViewModel: SaveImageViewModel by viewModels()

    private lateinit var binding: ActivitySaveImageBinding
    private var imageFile: File? = null
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()

    }
    private fun initUI(){
        initListeners()
        initObservers()
    }

    private fun initListeners(){
        getUserFromSession()
        with(binding){
            btnConfirm.setOnClickListener {
                saveImageViewModel.sendImage(imageFile!!, user!!, user!!.sessionToken!!)
            }
        }
    }

    private fun initObservers() {
        binding.circleimageUser.setOnClickListener { selectImage() }
        binding.btnNext.setOnClickListener { goToHomeAdministrator() }
        saveImageViewModel.navigateAdministrator.observe(this){
            it.getContentIfNotHandled()?.let {
                goToHomeAdministrator()
            }
        }
        lifecycleScope.launchWhenCreated {
            saveImageViewModel.statusSaveImage.collect {
                when (it) {
                    is ViewUiState.Success -> {
                        binding.progressBar.isVisible = false
                        goToHomeAdministrator()
                    }
                    is ViewUiState.Error -> {
                        Snackbar.make(
                            binding.root,
                            it.message,
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.isVisible = false
                    }
                    is ViewUiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                RESULT_OK -> {
                    val fileUri = data?.data
                    imageFile =
                        File(fileUri?.path.toString()) // El ARCHIVO QUE VAMOS A GUARDAR EN EL SERVIDOR
                    binding.circleimageUser.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "La tarea se cancelo", Toast.LENGTH_LONG).show()
                }
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
            Log.d("IMAGE", "HOLA ${user.toString()}")
        }
    }

    private fun goToHomeAdministrator() {
        startActivity(AdministratorHomeActivity.create(this))
    }

}