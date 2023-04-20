package com.edifice.residentialsecurity.ui.home.administratorResident.createResident

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.data.model.Sets
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.domain.CreateResidentUseCase
import com.edifice.residentialsecurity.ui.home.administratorSecurity.createSecurity.CreateViewModel
import com.edifice.residentialsecurity.ui.register.registerUser.RegisterUserViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateResidentViewModel @Inject constructor(
    private val createResidentUseCase: CreateResidentUseCase
):ViewModel(){

    companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }

    private val _registerUser = MutableStateFlow<ViewUiState>(ViewUiState.Empty)
    val registerUser : StateFlow<ViewUiState>
        get() = _registerUser

    private val _viewStateResident = MutableStateFlow(RegisterResidentViewState())
    val viewStateResident : StateFlow<RegisterResidentViewState>
        get() = _viewStateResident


    fun onSignInSelected(resident: User,token: String){
        val viewStateResident = resident.toSignInViewState()
        if (viewStateResident.registerValidated() && resident.isNotEmpty()){
            registerResident(resident,token)
        }else{
            onFieldsChangedResident(resident)
        }
    }

    private fun registerResident(resident: User, token: String) {
        viewModelScope.launch {
            _registerUser.value = ViewUiState.Loading
            val register = createResidentUseCase.invoke(resident, token)
            if (register.isSuccessful){
                _registerUser.value = ViewUiState.Success
            }else{
                _registerUser.value = ViewUiState.Error("Error al registrar propietario")
            }
        }
    }

    fun onFieldsChangedResident(resident: User) {
        _viewStateResident.value = resident.toSignInViewState()
    }

    private fun User.toSignInViewState(): RegisterResidentViewState {
        return RegisterResidentViewState(
            isValidName = isValidName(name),
            isValidLastName = isValidName(lastname),
            isValidDni = isValidNumber(dni!!),
            isValidEmail = isValidOrEmptyEmail(email!!),
            isValidPassword = isValidOrEmptyPassword(password!!),
            isValidPhone = isValidNumber(phone),
            isValidTower = isValidNumber(tower!!),
            isValidApartament = isValidNumber(apartament!!)
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