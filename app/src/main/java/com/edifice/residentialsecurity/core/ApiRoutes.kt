package com.edifice.residentialsecurity.core

import com.edifice.residentialsecurity.data.network.ResidentialsRoutes
import com.edifice.residentialsecurity.data.network.UserRoutes
import retrofit2.Retrofit
import javax.inject.Inject

class ApiRoutes(
){
    val API_URL = "http://192.168.0.10:3000/api/"
    val retrofit = RetrofitClient()

    fun getUsersRoutesWithToken(token: String): UserRoutes {
        return retrofit.getClientWithToken(API_URL, token).create(UserRoutes::class.java)
    }
}