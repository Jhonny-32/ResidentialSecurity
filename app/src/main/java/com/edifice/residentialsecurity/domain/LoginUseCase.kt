package com.edifice.residentialsecurity.domain

import com.edifice.residentialsecurity.data.ResidentialRepository
import com.edifice.residentialsecurity.data.model.ResponseHttp
import retrofit2.Response

class LoginUseCase {

    private val repository = ResidentialRepository()

    suspend operator fun invoke(email: String, password: String):Response<ResponseHttp>?{
        return repository.getLogin(email, password)
    }

}