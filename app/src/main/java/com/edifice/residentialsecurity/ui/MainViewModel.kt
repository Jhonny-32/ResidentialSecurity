package com.edifice.residentialsecurity.ui

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.domain.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private companion object {
        const val MIN_PASSWORD_LENGTH = 4
    }

    private val _loginUser = MutableStateFlow<ViewUiState>(ViewUiState.Empty)
    val loginUser : StateFlow<ViewUiState>
        get() = _loginUser


    private val _viewState = MutableStateFlow(LoginUserViewState())
    val viewState: StateFlow<LoginUserViewState>
        get() = _viewState

    private val loginUseCase = LoginUseCase()

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
                //saveUserInSession(register.body().toString())
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

    private fun isValidOrEmptyEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

    private fun isValidOrEmptyPassword(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH || password.isEmpty()


}