package com.edifice.residentialsecurity.ui.register.registerUser

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edifice.residentialsecurity.core.Event
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.domain.RegisterUserCaseUse
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val registerResidentialUseCase : RegisterUserCaseUse,
    private val sharedPrefsRepositoryImpl: SharedPrefsRepositoryImpl

) : ViewModel() {


    companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }

    private val _registerUser = MutableStateFlow<ViewUiState>(ViewUiState.Empty)
    val registerUser : StateFlow<ViewUiState>
        get() = _registerUser

    private val _viewState = MutableStateFlow(RegisterUserViewState())
    val viewState : StateFlow<RegisterUserViewState>
        get() = _viewState

    private val _navigateRegisterResidential = MutableLiveData<Event<Boolean>>()
    val navigateRegisterResidential : LiveData<Event<Boolean>>
        get() = _navigateRegisterResidential


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
                saveUserInSession(register.body()?.data.toString())
                _registerUser.value = ViewUiState.Success
                _navigateRegisterResidential.value = Event(true)
            }else{
                _registerUser.value = ViewUiState.Error("Error al registrar el administrator")
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
            isValidDni = isValidNumber(dni!!),
            isValidEmail = isValidOrEmptyEmail(email!!),
            isValidPassword = isValidOrEmptyPassword(password!!),
            isValidPhone = isValidNumber(phone)
        )
    }

    private fun saveUserInSession(data: String){
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPrefsRepositoryImpl.save("user", user)
    }

}