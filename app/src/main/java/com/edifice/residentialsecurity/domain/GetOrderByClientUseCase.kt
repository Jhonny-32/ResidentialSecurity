package com.edifice.residentialsecurity.domain

import com.edifice.residentialsecurity.data.ResidentialRepository
import com.edifice.residentialsecurity.data.model.Order
import javax.inject.Inject

class GetOrderByClientUseCase @Inject constructor(
    private val repository: ResidentialRepository
) {
    suspend operator fun invoke(
        status: String,
        conjunto: String,
        idClient: String,
        token: String)
    :ArrayList<Order>{
        return repository.getOrdersByClientAndStatus(status, conjunto, idClient, token)
    }
}