package com.edifice.residentialsecurity.ui.home.administratorSecurity.updateSecurity

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.domain.UpdateUserWithOutUseCase
import com.edifice.residentialsecurity.ui.register.registerUser.RegisterUserViewModel
import com.edifice.residentialsecurity.ui.register.registerUser.RegisterUserViewState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateSecurityViewModel @Inject constructor(
    private val updateUserWithOutUseCase: UpdateUserWithOutUseCase,
) : ViewModel() {

    private val _updateUser = MutableStateFlow<ViewUiState>(ViewUiState.Empty)
    val updateUser: StateFlow<ViewUiState>
        get() = _updateUser

    private val _viewState = MutableStateFlow(UpdateSecurityViewState())
    val viewState : StateFlow<UpdateSecurityViewState>
        get() = _viewState

    fun onSignInSelected(user: User){
        val viewStates = user.toUpdateViewState()
        if (viewStates.updateValidated()){
            updateUser(user)
        }else{
            onFieldsChanged(user)
        }
    }

    private fun updateUser(user: User) {
        viewModelScope.launch {
            _updateUser.value = ViewUiState.Loading
            val updateOutImage = updateUserWithOutUseCase(user)
            if (updateOutImage.isSuccessful) {
                _updateUser.value = ViewUiState.Success
            } else {
                _updateUser.value = ViewUiState.Error("Error al actualizar los datos")
            }
        }
    }

    fun onFieldsChanged(user: User) {
        _viewState.value = user.toUpdateViewState()
    }

    private fun isValidName(name: String): Boolean =
        name.length >= RegisterUserViewModel.MIN_PASSWORD_LENGTH || name.isEmpty()

    private fun isValidNumber(number: String): Boolean {
        return number.isNotEmpty() && number.isDigitsOnly()
    }
    private fun User.toUpdateViewState(): UpdateSecurityViewState {
        return UpdateSecurityViewState(
            isValidName = isValidName(name),
            isValidLastName = isValidName(lastname),
            isValidPhone = isValidNumber(phone)
        )
    }


}