package com.edifice.residentialsecurity.ui.client

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edifice.residentialsecurity.data.model.Order
import com.edifice.residentialsecurity.domain.GetOrderByClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val getOrderByClientUseCase: GetOrderByClientUseCase
):ViewModel(){

    private val _orderClient = MutableLiveData<ArrayList<Order>>()
    val orderClient : LiveData<ArrayList<Order>>
    get() = _orderClient

    fun getOrderClient(
        status: String,
        conjunto: String,
        idClient: String,
        token: String
    ){
        viewModelScope.launch {
            val data = getOrderByClientUseCase.invoke(status, conjunto, idClient, token)
            if (data.isNotEmpty()){
                _orderClient.value = data
            }
        }
    }

}