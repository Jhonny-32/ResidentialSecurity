package com.edifice.residentialsecurity.data.network

import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.ResponseHttp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ResidentialsRoutes {

    @POST("residential/create")
    suspend fun register(@Body residential: Residential) : Response<ResponseHttp>

    @FormUrlEncoded
    @POST("residential/saveResidentialUser")
    suspend fun registerResidentialUser(
        @Field("id_residential") idResidential : String,
        @Field("id_user") idUser : String
    ): Response<ResponseHttp>

}