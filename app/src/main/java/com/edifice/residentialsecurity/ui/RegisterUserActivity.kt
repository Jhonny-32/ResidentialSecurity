package com.edifice.residentialsecurity.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.core.ex.dismissKeyboard
import com.edifice.residentialsecurity.core.ex.loseFocusAfterAction
import com.edifice.residentialsecurity.core.ex.onTextChanged
import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.databinding.ActivityRegisterUserBinding
import com.edifice.residentialsecurity.providers.UserProvider
import com.edifice.residentialsecurity.util.SharedPref
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding
    private val registerUserViewModel: RegisterUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }
    private fun initUI(){
        initListeners()
        initObservers()
    }
    private fun initListeners(){
        binding.edittextName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextName.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextName.onTextChanged { onFieldChanged() }

        binding.edittextLastname.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextLastname.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextLastname.onTextChanged { onFieldChanged() }

        binding.edittextPhone.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextPhone.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextPhone.onTextChanged { onFieldChanged() }

        binding.edittextEmail.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextEmail.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextEmail.onTextChanged { onFieldChanged() }

        binding.edittextDni.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextDni.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextDni.onTextChanged { onFieldChanged() }

        binding.edittextPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextPassword.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextPassword.onTextChanged { onFieldChanged() }


        with(binding){
            btnRegister.setOnClickListener {
                it.dismissKeyboard()
                registerUserViewModel.onSignInSelected(
                    User(
                        name = binding.edittextName.text.toString(),
                        lastname = binding.edittextLastname.text.toString(),
                        phone = binding.edittextPhone.text.toString(),
                        email = binding.edittextEmail.text.toString(),
                        dni = binding.edittextDni.text.toString(),
                        password = binding.edittextPassword.toString()
                    )
                )
            }
        }

    }

    private fun initObservers(){
        lifecycleScope.launch {
            registerUserViewModel.viewState.collect{viewState ->
                updateUI(viewState)
            }
        }
        lifecycleScope.launchWhenCreated {
            registerUserViewModel.registerUser.collect {
                when (it) {
                    is ViewUiState.Success -> {
                        Snackbar.make(
                            binding.root,
                            "Registrado correctamente el administrador",
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.isVisible = false
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

    private fun updateUI(viewState: RegisterUserViewState) {
        with(binding){
            edittextName.error =
                if (viewState.isValidName) null else "El nombre no es valido"
            edittextLastname.error =
                if (viewState.isValidLastName) null else "El apellido no es valido"
            edittextPhone.error =
                if(viewState.isValidPhone) null else "El telefono no es valido"
            edittextEmail.error =
                if(viewState.isValidEmail) null else "El email no es valido"
            edittextDni.error =
                if(viewState.isValidDni) null else "El DNI no es valido"
            edittextPassword.error =
                if(viewState.isValidPassword) null else "Contraseña invalida"

        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            registerUserViewModel.onFieldsChanged(
                User(
                    name = binding.edittextName.text.toString(),
                    lastname = binding.edittextLastname.text.toString(),
                    phone = binding.edittextPhone.text.toString(),
                    email = binding.edittextEmail.text.toString(),
                    dni = binding.edittextDni.text.toString(),
                    password = binding.edittextPassword.toString()
                )
            )
        }
    }

    /*
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
                password = password,
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
    }*/

}