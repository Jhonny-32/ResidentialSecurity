package com.edifice.residentialsecurity.providers

import com.edifice.residentialsecurity.core.ApiRoutes
import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.network.ResidentialsRoutes
import retrofit2.Call

class ResidentialsProvider {

    private var residentialRoutes: ResidentialsRoutes? = null

    init {
        val api = ApiRoutes()
        residentialRoutes = api.getResidentialsRoutes()
    }
    /*
    fun register(residential : Residential): Call<ResponseHttp>?{
        return residentialRoutes?.register(residential)
    }*/




}