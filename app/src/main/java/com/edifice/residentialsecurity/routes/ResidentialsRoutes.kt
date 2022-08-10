package com.edifice.residentialsecurity.routes

import com.edifice.residentialsecurity.models.Residential
import com.edifice.residentialsecurity.models.ResponseHttp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ResidentialsRoutes {

    @POST("residential/create")
    fun register(@Body residential: Residential) : Call<ResponseHttp>


}