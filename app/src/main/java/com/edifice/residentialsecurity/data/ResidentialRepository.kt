package com.edifice.residentialsecurity.data

import android.util.Log
import com.edifice.residentialsecurity.core.ApiRoutes
import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.data.network.ResidentialService
import retrofit2.Call
import retrofit2.Response

class ResidentialRepository {

    private val residentialApi = ResidentialService()


    suspend fun getRegisterResidential(residential: Residential):Response<ResponseHttp>? {
        Log.d("JHONNY", "HOLA ERROR EN EL REPOSITORIO")
        return residentialApi.registerResidential(residential)
    }

    suspend fun getRegisterUser(user: User):Response<ResponseHttp>?{
        return residentialApi.registerUser(user)
    }

}