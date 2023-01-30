package com.edifice.residentialsecurity.ui.securityGuard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.edifice.residentialsecurity.data.model.Order
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.domain.GetDataResidentialUseCase
import com.edifice.residentialsecurity.domain.GetOrderByStatusUseCase
import com.edifice.residentialsecurity.domain.SendOrderUseCase
import com.edifice.residentialsecurity.util.SharedPref
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SecurityViewModel @Inject constructor(
    private val getDataResidentialUseCase: GetDataResidentialUseCase,
    private val sendOrderUseCase: SendOrderUseCase,
    private val getOrderByStatusUseCase: GetOrderByStatusUseCase,
    private val sharedPrefsRepositoryImpl: SharedPrefsRepositoryImpl
) : ViewModel() {

    private val _dataResident = MutableLiveData<ArrayList<User>>()
    val dataResident: LiveData<ArrayList<User>>
        get() = _dataResident

    private val _ordersByStatus = MutableLiveData<ArrayList<Order>>()
    val ordersByStatus : LiveData<ArrayList<Order>>
    get() = _ordersByStatus

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

    fun sendOrder(files : List<File>, order: Order){
        viewModelScope.launch {
            val result = sendOrderUseCase.invoke(files, order)
            if (result.isSuccessful){
                Log.d("JHONNY",result.body()?.message.toString())
            }else{
                Log.d("JHONNY",result.body()?.error.toString())

            }
        }
    }

    fun getOrders(status: String, conjunto: String,token: String){
        viewModelScope.launch {
            val response = getOrderByStatusUseCase.invoke(status,conjunto, token)
            if (response.isNotEmpty()){
                _ordersByStatus.value = response
            }
        }
    }

    fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPrefsRepositoryImpl.getData("user").isNullOrBlank()) {
            gson.fromJson(sharedPrefsRepositoryImpl.getData("user"), User::class.java)
        }
    }
}