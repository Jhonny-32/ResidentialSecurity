package com.edifice.residentialsecurity.data.network

import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.ResponseHttp
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ResidentialsRoutes {

    @POST("residential/create")
    suspend fun register(@Body residential: Residential) : Response<ResponseHttp>


}