package com.edifice.residentialsecurity.domain

import com.edifice.residentialsecurity.data.ResidentialRepository
import com.edifice.residentialsecurity.data.model.Order
import com.edifice.residentialsecurity.data.model.ResponseHttp
import retrofit2.Call
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class SendOrderUseCase @Inject constructor(
    private val repository: ResidentialRepository
) {
    suspend operator fun invoke(files: List<File>, order: Order): Response<ResponseHttp>{
        return repository.sendOrder(files, order)
    }
}