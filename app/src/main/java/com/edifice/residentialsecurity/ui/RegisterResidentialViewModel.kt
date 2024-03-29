package com.edifice.residentialsecurity.ui

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edifice.residentialsecurity.core.Event
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.domain.RegisterResidentialUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterResidentialViewModel @Inject constructor(
    private val registerResidentialUseCase : RegisterResidentialUseCase
) : ViewModel() {

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }

    private val _navigateSaveImage = MutableLiveData<Event<Boolean>>()
    val navigateSaveImage : LiveData<Event<Boolean>>
        get() = _navigateSaveImage

    private val _registerResidentialState = MutableStateFlow<ViewUiState>(ViewUiState.Empty)
    val loginUiState: StateFlow<ViewUiState>
        get() = _registerResidentialState

    private val _viewState = MutableStateFlow(RegisterViewState())
    val viewState: StateFlow<RegisterViewState>
        get() = _viewState

    fun onSignInSelected(residential: Residential) {
        val viewStates = residential.toSignInViewState()
        if (viewStates.registerValidated() && residential.isNotEmpty()){
            register(residential)
        }else{
            onFieldsChanged(residential)
        }
    }

    private fun register(residential: Residential){
        viewModelScope.launch {
            _registerResidentialState.value = ViewUiState.Loading
            val register = registerResidentialUseCase(residential)
            if (register?.isSuccessful == true){
                _registerResidentialState.value = ViewUiState.Success
                _navigateSaveImage.value = Event(true)
            }else{
                _registerResidentialState.value = ViewUiState.Error("Error al registrar el conjunto")
            }
        }
    }

    fun onFieldsChanged(userSignIn: Residential) {
        _viewState.value = userSignIn.toSignInViewState()
    }

    private fun isValidName(name: String): Boolean =
        name.length >= MIN_PASSWORD_LENGTH || name.isEmpty()

    private fun isValidNumber(number: String): Boolean {
        return number.isNotEmpty() && number.isDigitsOnly()
    }

    private fun Residential.toSignInViewState(): RegisterViewState {
        return RegisterViewState(
            isValidNameResidential = isValidName(name),
            isValidAddress = isValidName(address),
            isValidNit = isValidNumber(nit)
        )
    }
}