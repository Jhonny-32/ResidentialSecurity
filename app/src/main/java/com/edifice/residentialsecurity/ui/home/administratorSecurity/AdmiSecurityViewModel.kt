package com.edifice.residentialsecurity.ui.home.administratorSecurity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edifice.residentialsecurity.core.Event
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.domain.GetDataUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdmiSecurityViewModel @Inject constructor(
    private val getDataUseCase: GetDataUseCase,
    private val sharedPrefsRepositoryImpl: SharedPrefsRepositoryImpl
):ViewModel(){

    init {
        getData()
    }


    private val _getData = MutableLiveData<ArrayList<User>>()
    val getData : LiveData<ArrayList<User>>
        get() = _getData


    fun getData(){
        viewModelScope.launch {
            val token: String
            val residential: String
            val rol = "VIGILANTE"
            val gson = Gson()
            if (!sharedPrefsRepositoryImpl.getData("user").isNullOrBlank()) {
                val user = gson.fromJson(sharedPrefsRepositoryImpl.getData("user"), User::class.java)
                token = user.sessionToken.toString()
                residential = user.conjunto.toString()
            } else {
                token = ""
                residential = ""
            }
            val result = getDataUseCase.invoke(rol, residential, token)
            if (result.isNotEmpty()){
                _getData.value = result
            }
        }
    }


}