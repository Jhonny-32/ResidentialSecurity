package com.edifice.residentialsecurity.providers

import com.edifice.residentialsecurity.api.ApiRoutes
import com.edifice.residentialsecurity.models.ResponseHttp
import com.edifice.residentialsecurity.models.User
import com.edifice.residentialsecurity.routes.UserRoutes
import retrofit2.Call

class UserProvider {

    private var userRoutes: UserRoutes? = null

    init {
        val api = ApiRoutes()
        userRoutes = api.getUserRoutes()
    }

    fun register(user : User): Call<ResponseHttp>?{
        return userRoutes?.register(user)
    }
    fun login(email : String, password : String): Call<ResponseHttp>?{
        return userRoutes?.login(email,password)
    }

}