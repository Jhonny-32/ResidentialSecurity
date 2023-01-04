package com.edifice.residentialsecurity.domain

import com.edifice.residentialsecurity.data.ResidentialRepository
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import retrofit2.Response
import javax.inject.Inject

class GetDataResidentialUseCase @Inject constructor(
    private val repository: ResidentialRepository
) {

    suspend operator fun invoke(conjunto: String, token: String): ArrayList<User>? {
        val response = repository.getDataResident(conjunto, token)
        if (response?.isEmpty() == true) {
            return response
        } else {
            throw Exception("Error al obtener los datos de los residentes")
        }
    }

}