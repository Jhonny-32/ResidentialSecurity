package com.edifice.residentialsecurity.ui.home.administratorSecurity.createSecurity

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.domain.CreateSecurityUseCase
import com.edifice.residentialsecurity.ui.register.registerUser.RegisterUserViewModel
import com.edifice.residentialsecurity.ui.register.registerUser.RegisterUserViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val createSecurityUseCase: CreateSecurityUseCase
): ViewModel() {

    companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }

    private val _registerUser = MutableStateFlow<ViewUiState>(ViewUiState.Empty)
    val registerUser : StateFlow<ViewUiState>
        get() = _registerUser

    private val _viewState = MutableStateFlow(RegisterUserViewState())
    val viewState : StateFlow<RegisterUserViewState>
        get() = _viewState

    fun onSignInSelected(security: User, token: String){
        val viewStates = security.toSignInViewState()
        if (viewStates.registerValidated() && security.isNotEmpty()){
            registerUser(security, token)
        }else{
            onFieldsChanged(security)
        }
    }

    fun onFieldsChanged(security: User) {
        _viewState.value = security.toSignInViewState()
    }

    private fun registerUser(security: User, token: String) {
        viewModelScope.launch {
            _registerUser.value = ViewUiState.Loading
            val register = createSecurityUseCase.invoke(security, token)
            if (register.isSuccessful){
                _registerUser.value = ViewUiState.Success
            }else{
                _registerUser.value = ViewUiState.Error("Error al registrar el administrator")
            }
        }
    }

    private fun User.toSignInViewState(): RegisterUserViewState {
        return RegisterUserViewState(
            isValidName = isValidName(name),
            isValidLastName = isValidName(lastname),
            isValidDni = isValidNumber(dni),
            isValidEmail = isValidOrEmptyEmail(email),
            isValidPassword = isValidOrEmptyPassword(password),
            isValidPhone = isValidNumber(phone)
        )
    }

    private fun isValidName(name: String): Boolean =
        name.length >= MIN_PASSWORD_LENGTH || name.isEmpty()

    private fun isValidNumber(number: String): Boolean {
        return number.isNotEmpty() && number.isDigitsOnly()
    }
    private fun isValidOrEmptyEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

    private fun isValidOrEmptyPassword(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH || password.isEmpty()

}