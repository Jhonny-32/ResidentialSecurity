package com.edifice.residentialsecurity.data.network

import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserRoutes {
    @POST("user/create")
    suspend fun register(@Body user: User) : Response<ResponseHttp>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("email") email: String, @Field("password") password : String): Response<ResponseHttp>

    @Multipart
    @PUT("users/update")
    suspend fun update(
        @Part image: MultipartBody.Part,
        @Part("user") user : RequestBody,
        @Header("Authorization") token: String
    ): Response<ResponseHttp>

    @PUT("users/updateWithOut")
    suspend fun updateWithOutImage(
        @Body user : User,
        @Header("Authorization") token: String
    ): Response<ResponseHttp>

    @FormUrlEncoded
    @POST("user/getDataResident")
    suspend fun getDataResident(
        @Field("conjunto") conjunto: String,
        @Header("Authorization") token: String
    ): ArrayList<User>

    @GET("users/getDataUser/{nameRol}/{nameResidential}")
    suspend fun getDataUser(
        @Path("nameRol") rol: String,
        @Path("nameResidential") residential: String,
        @Header("Authorization") token: String
    ):ArrayList<User>

    @POST("user/createSecurity")
    suspend fun createSecurity(
        @Body user: User,
        @Header("Authorization") token: String
    ):Response<ResponseHttp>
}