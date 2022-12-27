package com.edifice.residentialsecurity.data

import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.data.network.ResidentialService
import retrofit2.Response

class ResidentialRepository {

    private val residentialApi = ResidentialService()


    suspend fun getRegisterResidential(residential: Residential):Response<ResponseHttp>? {
        return residentialApi.registerResidential(residential)
    }

    suspend fun getRegisterUser(user: User):Response<ResponseHttp>?{
        return residentialApi.registerUser(user)
    }

}