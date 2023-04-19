package com.edifice.residentialsecurity.ui.home.administratorResident

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.domain.GetDataResidentialUseCase
import com.edifice.residentialsecurity.domain.GetDataUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdmiResidentViewModel @Inject constructor(
    private val getDataResidentialUseCase: GetDataResidentialUseCase,
    private val sharedPrefsRepositoryImpl: SharedPrefsRepositoryImpl
): ViewModel(){

    init {
        dataResidential()
    }

    private val _dataResident = MutableLiveData<ArrayList<User>>()
    val dataResident: LiveData<ArrayList<User>>
        get() = _dataResident

    fun dataResidential() {
        viewModelScope.launch {
            val token: String
            val conjunto: String
            val gson = Gson()
            if (!sharedPrefsRepositoryImpl.getData("user").isNullOrBlank()) {
                val user =
                    gson.fromJson(sharedPrefsRepositoryImpl.getData("user"), User::class.java)
                token = user.sessionToken.toString()
                conjunto = user.conjunto.toString()
            } else {
                token = ""
                conjunto = ""
            }
            val data = getDataResidentialUseCase.invoke(conjunto, token)
            if (data?.isNotEmpty() == true) {
                _dataResident.value = data!!
            }
        }
    }

}