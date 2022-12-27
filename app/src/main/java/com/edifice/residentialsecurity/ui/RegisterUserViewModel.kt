package com.edifice.residentialsecurity.ui

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.domain.RegisterUserCaseUse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterUserViewModel: ViewModel() {

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }

    private var registerResidentialUseCase = RegisterUserCaseUse()

    private val _registerUser = MutableStateFlow<ViewUiState>(ViewUiState.Empty)
    val registerUser : StateFlow<ViewUiState>
        get() = _registerUser

    private val _viewState = MutableStateFlow(RegisterUserViewState())
    val viewState : StateFlow<RegisterUserViewState>
        get() = _viewState

    fun onSignInSelected(user: User){
        val viewStates = user.toSignInViewState()
        if (viewStates.registerValidated() && user.isNotEmpty()){
            registerUser(user)
        }else{
            onFieldsChanged(user)
        }
    }

     private fun registerUser(user: User) {
        viewModelScope.launch {
            _registerUser.value = ViewUiState.Loading
            val register = registerResidentialUseCase.invoke(user)
            if (register?.isSuccessful == true){
                _registerUser.value = ViewUiState.Success
                //saveUserInSession(register.body().toString())
            }else{
                _registerUser.value = ViewUiState.Error(R.string.register_user_ui_state.toString())
            }
        }
    }

    fun onFieldsChanged(user: User) {
        _viewState.value = user.toSignInViewState()
    }

    private fun isValidName(name: String): Boolean =
        name.length >= MIN_PASSWORD_LENGTH || name.isEmpty()

    private fun isValidNumber(number: String): Boolean {
        return number.isNotEmpty() && number.isDigitsOnly()
    }
    private fun isValidOrEmptyEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

    private fun isValidOrEmptyPassword(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH || password.isEmpty()


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
    /*
    private fun saveUserInSession(data: String){
        val sharedPref = SharedPref(Activity())
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)
    }*/

}