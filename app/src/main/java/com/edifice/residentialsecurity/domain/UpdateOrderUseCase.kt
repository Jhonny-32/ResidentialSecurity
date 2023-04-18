package com.edifice.residentialsecurity.domain

import com.edifice.residentialsecurity.data.ResidentialRepository
import com.edifice.residentialsecurity.data.model.Order
import com.edifice.residentialsecurity.data.model.ResponseHttp
import retrofit2.Response
import javax.inject.Inject

class UpdateOrderUseCase @Inject constructor(
    private val repository: ResidentialRepository
) {

    suspend operator fun invoke(order: Order, token: String):Response<ResponseHttp>{
        return repository.updateOrder(order, token)
    }

}