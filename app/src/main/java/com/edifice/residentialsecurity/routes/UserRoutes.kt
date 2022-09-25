package com.edifice.residentialsecurity.routes

import com.edifice.residentialsecurity.models.Residential
import com.edifice.residentialsecurity.models.ResponseHttp
import com.edifice.residentialsecurity.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UserRoutes {
    @POST("user/create")
    fun register(@Body user: User) : Call<ResponseHttp>

    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("email") email: String, @Field("password") password : String): Call<ResponseHttp>

    @Multipart
    @PUT("users/update")
    fun update(
        @Part image: MultipartBody.Part,
        @Part("user") user : RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>

    @PUT("users/updateWithOut")
    fun updateWithOutImage(
        @Body user : User,
        @Header("Authorization") token: String
    ): Call<ResponseHttp>
}