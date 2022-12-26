package com.edifice.residentialsecurity.core

import com.edifice.residentialsecurity.data.network.ResidentialsRoutes
import com.edifice.residentialsecurity.data.network.UserRoutes

class ApiRoutes {

    val API_URL = "http://192.168.0.10:3000/api/"
    val retrofit = RetrofitClient()

    fun getResidentialsRoutes(): ResidentialsRoutes {
        return retrofit.getClient(API_URL).create(ResidentialsRoutes::class.java)
    }

    fun getUserRoutes(): UserRoutes {
        return retrofit.getClient(API_URL).create(UserRoutes::class.java)
    }

    fun getUsersRoutesWithToken(token: String): UserRoutes {
        return retrofit.getClientWithToken(API_URL, token).create(UserRoutes::class.java)
    }
}