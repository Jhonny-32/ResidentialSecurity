package com.edifice.residentialsecurity.routes

import com.edifice.residentialsecurity.models.Residential
import com.edifice.residentialsecurity.models.ResponseHttp
import com.edifice.residentialsecurity.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserRoutes {
    @POST("user/create")
    fun register(@Body user: User) : Call<ResponseHttp>

    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("email") email: String, @Field("password") password : String): Call<ResponseHttp>
}