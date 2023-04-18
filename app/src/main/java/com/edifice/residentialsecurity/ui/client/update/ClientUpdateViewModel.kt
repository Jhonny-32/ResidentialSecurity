package com.edifice.residentialsecurity.ui.client.update

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edifice.residentialsecurity.core.Event
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.domain.SendUseImageUseCase
import com.edifice.residentialsecurity.domain.UpdateUserWithOutUseCase
import com.edifice.residentialsecurity.ui.register.registerUser.RegisterUserViewModel
import com.edifice.residentialsecurity.ui.register.registerUser.RegisterUserViewState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ClientUpdateViewModel @Inject constructor(
    private val updateUserWithOutUseCase: UpdateUserWithOutUseCase,
    private val sendUseImageUseCase: SendUseImageUseCase,
    private val sharedPrefsRepositoryImpl: SharedPrefsRepositoryImpl

) : ViewModel() {

    private val _viewState = MutableStateFlow(RegisterUserViewState())
    val viewState : StateFlow<RegisterUserViewState>
        get() = _viewState

    private val _updateUser = MutableStateFlow<ViewUiState>(ViewUiState.Empty)
    val updateUser : StateFlow<ViewUiState>
        get() = _updateUser


    fun updateUserImage(image: File, user: User, token: String){
        viewModelScope.launch {
            _updateUser.value = ViewUiState.Loading
            val update = sendUseImageUseCase(image, user, token)
            if (update!!.isSuccessful){
                saveUserInSession(update.body()?.data.toString())
                _updateUser.value = ViewUiState.SuccessMessage("Actualizado Correctamente")
            }else{
                _updateUser.value = ViewUiState.Error("Error al actualizar los datos")
            }
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch {
            _updateUser.value = ViewUiState.Loading
            val updateOutImage = updateUserWithOutUseCase(user)
            if (updateOutImage!!.isSuccessful){
                saveUserInSession(updateOutImage.body()?.data.toString())
                _updateUser.value = ViewUiState.SuccessMessage("Actualizado Correctamente")
            }else{
                _updateUser.value = ViewUiState.Error("Error al actualizar los datos")
            }
        }
    }

    private fun onFieldsChanged(user: User) {
        _viewState.value = user.toSignInViewState()
    }

    private fun isValidName(name: String): Boolean =
        name.length >= RegisterUserViewModel.MIN_PASSWORD_LENGTH || name.isEmpty()

    private fun isValidNumber(number: String): Boolean {
        return number.isNotEmpty() && number.isDigitsOnly()
    }
    private fun isValidOrEmptyEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

    private fun isValidOrEmptyPassword(password: String): Boolean = password.length >= RegisterUserViewModel.MIN_PASSWORD_LENGTH || password.isEmpty()

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