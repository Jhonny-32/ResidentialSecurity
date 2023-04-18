package com.edifice.residentialsecurity.ui.securityGuard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.data.model.Order
import com.edifice.residentialsecurity.data.model.Sets
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.domain.*
import com.edifice.residentialsecurity.util.SharedPref
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SecurityViewModel @Inject constructor(
    private val getDataResidentialUseCase: GetDataResidentialUseCase,
    private val sendOrderUseCase: SendOrderUseCase,
    private val getOrderByStatusUseCase: GetOrderByStatusUseCase,
    private val getAllSetsUseCase: GetAllSetsUseCase,
    private val updateOrderUseCase: UpdateOrderUseCase,
    private val sharedPrefsRepositoryImpl: SharedPrefsRepositoryImpl
) : ViewModel() {

    init {
        dataResidential()
    }

    private val _dataResident = MutableLiveData<ArrayList<User>>()
    val dataResident: LiveData<ArrayList<User>>
        get() = _dataResident

    private val _ordersByStatus = MutableLiveData<ArrayList<Order>>()
    val ordersByStatus : LiveData<ArrayList<Order>>
    get() = _ordersByStatus

    private val _setsDataClient = MutableLiveData<ArrayList<Sets>>()
    val setsDataClient : LiveData<ArrayList<Sets>>
        get() = _setsDataClient

    private val _createOrder = MutableStateFlow<ViewUiState>(ViewUiState.Empty)
    val createOrder : StateFlow<ViewUiState>
        get() = _createOrder

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
            _createOrder.value = ViewUiState.Loading
            val result = sendOrderUseCase.invoke(files, order)
            if (result.isSuccessful){
                _createOrder.value = ViewUiState.Success
            }else{
                _createOrder.value = ViewUiState.Error("Error al crear una orden")
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

    fun getSetsData(conjunto:String, token: String){
        viewModelScope.launch {
            val data = getAllSetsUseCase.invoke(conjunto, token)
            if (data.isNotEmpty()){
                _setsDataClient.value = data
            }
        }
    }

    fun updateOrder(order: Order, token: String){
        viewModelScope.launch {
            updateOrderUseCase.invoke(order, token)
        }
    }

    fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPrefsRepositoryImpl.getData("user").isNullOrBlank()) {
            gson.fromJson(sharedPrefsRepositoryImpl.getData("user"), User::class.java)
        }
    }
}