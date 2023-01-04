package com.edifice.residentialsecurity.data

import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.data.network.ResidentialService
import retrofit2.Response
import javax.inject.Inject

class ResidentialRepository @Inject constructor(private val residentialApi : ResidentialService){

    suspend fun getRegisterResidential(residential: Residential):Response<ResponseHttp>? {
        return residentialApi.registerResidential(residential)
    }

    suspend fun getRegisterUser(user: User):Response<ResponseHttp>?{
        return residentialApi.registerUser(user)
    }

    suspend fun getLogin(email: String, password: String):Response<ResponseHttp>?{
        return residentialApi.login(email, password)
    }

    suspend fun getDataResident(conjunto: String, token: String): ArrayList<User>? {
        return residentialApi.getDataResident(conjunto, token)
    }

}