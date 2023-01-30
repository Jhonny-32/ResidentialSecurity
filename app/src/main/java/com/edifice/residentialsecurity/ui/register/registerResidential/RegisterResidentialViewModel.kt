package com.edifice.residentialsecurity.ui.register.registerResidential

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edifice.residentialsecurity.core.Event
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.domain.RegisterResidentialUseCase
import com.edifice.residentialsecurity.domain.SendRegisterUserUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterResidentialViewModel @Inject constructor(
    private val registerResidentialUseCase : RegisterResidentialUseCase,
    private var user : User,
    private var residential: Residential,
    private val sendRegisterUserUseCase: SendRegisterUserUseCase,
    private val sharedPrefsRepositoryImpl: SharedPrefsRepositoryImpl

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
                saveUserInSession(register.body()?.data.toString())
                sendRegisterUserUseCase(getResidentData(),getUserData())
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
    private fun saveUserInSession(data: String){
        val gson = Gson()
        val residential = gson.fromJson(data, Residential::class.java)
        sharedPrefsRepositoryImpl.save("residential", residential)
    }

    fun getUserData() : String {
        val gson = Gson()
        if (!sharedPrefsRepositoryImpl?.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPrefsRepositoryImpl?.getData("user"), User::class.java)
            Log.d("JHONNY", user.id.toString())
            return user?.id.toString()
        }
        return ""
    }


    fun getResidentData() : String {
        val gson = Gson()
        if (!sharedPrefsRepositoryImpl?.getData("residential").isNullOrBlank()) {
            residential = gson.fromJson(sharedPrefsRepositoryImpl?.getData("residential"), Residential::class.java)
            Log.d("JHONNY", residential.id.toString())
            return residential?.id.toString()
        }
        return ""
    }

}