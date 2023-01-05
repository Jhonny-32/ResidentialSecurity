package com.edifice.residentialsecurity.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import android.util.Patterns
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edifice.residentialsecurity.core.Event
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.domain.LoginUseCase
import com.edifice.residentialsecurity.ui.home.AdministratorHomeActivity
import com.edifice.residentialsecurity.ui.manager.ManagerHomeActivity
import com.edifice.residentialsecurity.ui.securityGuard.SecurityHomeActivity
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginUseCase : LoginUseCase,
    private val sharedPrefsRepositoryImpl: SharedPrefsRepositoryImpl
) : ViewModel() {

    private companion object {
        const val MIN_PASSWORD_LENGTH = 4
    }
    private val _navigateSecurity = MutableLiveData<Event<Boolean>>()
    val navigateSecurity : LiveData<Event<Boolean>>
        get() = _navigateSecurity

    private val _navigateSelectRol = MutableLiveData<Event<Boolean>>()
    val navigateSelectRol : LiveData<Event<Boolean>>
        get() = _navigateSelectRol

    private val _navigateRegister = MutableLiveData<Event<Boolean>>()
    val navigateRegister : LiveData<Event<Boolean>>
        get() = _navigateRegister

    private val _loginUser = MutableStateFlow<ViewUiState>(ViewUiState.Empty)
    val loginUser : StateFlow<ViewUiState>
        get() = _loginUser


    private val _viewState = MutableStateFlow(LoginUserViewState())
    val viewState: StateFlow<LoginUserViewState>
        get() = _viewState

    fun onLoginSelected(email: String, password: String){
        if (isValidOrEmptyEmail(email) && isValidOrEmptyPassword(password)){
            loginUser(email , password )
        }else{
            onFieldsChanged(email, password)
        }
    }
    private fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginUser.value = ViewUiState.Loading
            val register = loginUseCase.invoke(email, password)
            if (register?.isSuccessful == true){
                _loginUser.value = ViewUiState.Success
                saveUserInSession(register.body()?.data.toString())
            }else{
                _loginUser.value = ViewUiState.Error("Error al iniciar sesion")
            }
        }
    }

    fun onFieldsChanged(email: String, password: String) {
        _viewState.value = LoginUserViewState(
            isValidUser = isValidOrEmptyEmail(email),
            isValidPassword = isValidOrEmptyPassword(password)
        )
    }

    fun onRegisterSelected(){
        _navigateRegister.value = Event(true)
    }
    fun onSecurityGuard(){
        _navigateSecurity.value = Event(true)
    }

    private fun isValidOrEmptyEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

    private fun isValidOrEmptyPassword(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH || password.isEmpty()


    private fun saveUserInSession(data: String){
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPrefsRepositoryImpl.save("user", user)

        if (user.roles?.size!! >= 1){
            _navigateSelectRol.value = Event(true)
        }
    }

    fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPrefsRepositoryImpl.getData("user").isNullOrBlank()) {
            // SI EL USARIO EXISTE EN SESION
            gson.fromJson(sharedPrefsRepositoryImpl.getData("user"), User::class.java)
            if (!sharedPrefsRepositoryImpl.getData("rol").isNullOrBlank()) {
                // SI EL USUARIO SELECCIONO EL ROL
                when (sharedPrefsRepositoryImpl.getData("rol")?.replace("\"", "")) {
                    "ADMINISTRADOR" -> {

                    }
                    "PROPIETARIO" -> {
                        Log.d("JHONNY", "USER PROPIETARIO")
                    }
                    "VIGILANTE" -> {
                        onSecurityGuard()
                    }
                    "MANAGER" -> {

                    }
                }
            } else {
                Log.d("JHONNY", "USER PROPIETARIO")
            }
        }
    }
}