package com.edifice.residentialsecurity.providers

import com.edifice.residentialsecurity.api.ApiRoutes
import com.edifice.residentialsecurity.models.Residential
import com.edifice.residentialsecurity.models.ResponseHttp
import com.edifice.residentialsecurity.routes.ResidentialsRoutes
import retrofit2.Call

class ResidentialsProvider {

    private var residentialRoutes: ResidentialsRoutes? = null

    init {
        val api = ApiRoutes()
        residentialRoutes = api.getResidentialsRoutes()
    }

    fun register(residential : Residential): Call<ResponseHttp>?{
        return residentialRoutes?.register(residential)
    }




}