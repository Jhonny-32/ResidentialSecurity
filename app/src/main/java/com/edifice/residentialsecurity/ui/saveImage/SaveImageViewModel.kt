package com.edifice.residentialsecurity.ui.saveImage

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edifice.residentialsecurity.core.Event
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.domain.GetDataResidentialUseCase
import com.edifice.residentialsecurity.domain.SendRegisterUserUseCase
import com.edifice.residentialsecurity.domain.SendUseImageUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SaveImageViewModel @Inject constructor(
    private val sendUseImageUseCase: SendUseImageUseCase,
    private val sharedPrefsRepositoryImpl: SharedPrefsRepositoryImpl
):ViewModel() {

    private val _navigateAdminitrator = MutableLiveData<Event<Boolean>>()
    val navigateAdministrator : LiveData<Event<Boolean>>
        get() = _navigateAdminitrator

    private val _statusSaveImage = MutableStateFlow<ViewUiState>(ViewUiState.Empty)
    val statusSaveImage : StateFlow<ViewUiState>
        get() = _statusSaveImage

    fun sendImage(file: File, user: User, token: String){
        viewModelScope.launch {
            _statusSaveImage.value = ViewUiState.Loading
            val imageUser = sendUseImageUseCase(file, user, token)

            if (imageUser?.isSuccessful == true){
                saveUserInSession(imageUser.body()?.data.toString())
                _navigateAdminitrator.value = Event(true)
                _statusSaveImage.value = ViewUiState.Success
            }else{
                _statusSaveImage.value = ViewUiState.Error("Debes seleccionar una imagen.")
            }
        }
    }

    private fun saveUserInSession(data: String){
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPrefsRepositoryImpl?.save("user", user)
    }




}