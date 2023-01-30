package com.edifice.residentialsecurity.domain

import com.edifice.residentialsecurity.data.ResidentialRepository
import com.edifice.residentialsecurity.data.model.Order
import retrofit2.Response
import javax.inject.Inject

class GetOrderByStatusUseCase @Inject constructor(
    private val repository: ResidentialRepository
) {

    suspend operator fun invoke(status: String,conjunto: String, token: String):ArrayList<Order>{
        return repository.getOrdersByStatus(status, conjunto ,token)
    }

}