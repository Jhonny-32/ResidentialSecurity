package com.edifice.residentialsecurity.data.network

import android.util.Log
import com.edifice.residentialsecurity.core.ApiRoutes
import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.ResponseHttp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class ResidentialService {

    private var residentialRoutes: ResidentialsRoutes? = null

    init {
        val api = ApiRoutes()
        residentialRoutes = api.getResidentialsRoutes()
    }

    suspend fun registerResidential(residential : Residential): Response<ResponseHttp>?{
        return withContext(Dispatchers.IO){
            Log.d("JHONNY", "HOLA ERROR EN EL SERVICIO")
            residentialRoutes?.register(residential)
        }
    }
}