package com.edifice.residentialsecurity.data.network

import com.edifice.residentialsecurity.data.model.Sets
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SetsRoutes {

    @GET("sets/getSetsData/{conjunto}")
    suspend fun getSetData(
        @Path("conjunto") conjunto: String,
        @Header("Authorization") token: String
    ):ArrayList<Sets>

}