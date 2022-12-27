package com.edifice.residentialsecurity.ui

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.core.ex.dismissKeyboard
import com.edifice.residentialsecurity.core.ex.loseFocusAfterAction
import com.edifice.residentialsecurity.core.ex.onTextChanged
import com.edifice.residentialsecurity.ui.home.AdministratorHomeActivity
import com.edifice.residentialsecurity.ui.client.home.ClientHomeActivity
import com.edifice.residentialsecurity.ui.manager.ManagerHomeActivity
import com.edifice.residentialsecurity.ui.securityGuard.SecurityHomeActivity
import com.edifice.residentialsecurity.databinding.ActivityMainBinding
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.providers.UserProvider
import com.edifice.residentialsecurity.util.SharedPref
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val TAG ="MainActivity"

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        /*
        binding.btnLogin.setOnClickListener{ login() }
        binding.txtRegister.setOnClickListener{ goToRegister() }
        getUserFromSession()*/
    }

    private fun initUi(){
        initListeners()
        initObservers()
    }

    private fun initListeners(){
        binding.edittextEmail.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextEmail.onTextChanged { onFieldChanged() }

        binding.edittextPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
        binding.edittextPassword.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextPassword.onTextChanged { onFieldChanged() }

        binding.btnLogin.setOnClickListener {
            it.dismissKeyboard()
            mainViewModel.onLoginSelected(
                email = binding.edittextEmail.text.toString(),
                password = binding.edittextPassword.text.toString()
            )
        }
    }

    private fun initObservers(){
        lifecycleScope.launchWhenStarted {
            mainViewModel.viewState.collect { viewState ->
                updateUI(viewState)
            }
        }
        lifecycleScope.launchWhenCreated {
            mainViewModel.loginUser.collect {
                when (it) {
                    is ViewUiState.Success -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.login_success),
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

    private fun updateUI(viewState: LoginUserViewState) {
        with(binding) {
            edittextEmail.error =
                if (viewState.isValidUser) null else getString(R.string.login_error_email)
            edittextPassword.error =
                if (viewState.isValidPassword) null else getString(R.string.login_error_password)
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            mainViewModel.onFieldsChanged(
                email = binding.edittextEmail.text.toString(),
                password = binding.edittextPassword.text.toString()
            )
        }
    }

    /*
    private fun login(){
        val email = binding.edittextEmail.text.toString()
        val password = binding.edittextPassword.text.toString()

        if(isValidForm(email, password)){
            userProvider.login(email,password)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse( call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    if (response.body()?.isSuccess == true){
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        saveUserInSession(response.body()?.data.toString())
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

    private fun String.isEmailValid(): Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun goToSelectRol(){
        val i = Intent(this, SelectRolesActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun saveUserInSession(data: String){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)
        Log.d(TAG, "USER $user")

        if (user.roles?.size!! >= 1){
            goToSelectRol()
        }


    }
    private fun goToRegister(){
        val i = Intent(this, RegisterUserActivity::class.java)
        startActivity(i)
    }

    private fun goToClientHome(){
        val i = Intent(this, ClientHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun goToAdministrator(){
        val i = Intent(this, AdministratorHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
    private fun goToSecurityGuard(){
        val i = Intent(this, SecurityHomeActivity::class.java)
        i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun goToManager(){
        val i = Intent(this, ManagerHomeActivity::class.java)
        startActivity(i)
    }

    private fun getUserFromSession() {
        val sharedPref = SharedPref(this)
        val gson = Gson()
        if (!sharedPref.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            gson.fromJson(sharedPref.getData("user"), User::class.java)
            if (!sharedPref.getData("rol").isNullOrBlank()) {
                // SI EL USUARIO SELECCIONO EL ROL
                val rol = sharedPref.getData("rol")?.replace("\"", "")

                if (rol == "ADMINISTRADOR") {
                    goToAdministrator()
                } else if (rol == "PROPIETARIO") {
                    goToClientHome()
                } else if (rol == "VIGILANTE") {
                    goToSecurityGuard()
                }else if (rol == "MANAGER") {
                    goToManager()
                }
            } else {
                goToManager()
            }
        }
    }
    */

}