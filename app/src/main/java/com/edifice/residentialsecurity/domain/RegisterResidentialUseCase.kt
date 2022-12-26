package com.edifice.residentialsecurity.domain

import android.util.Log
import com.edifice.residentialsecurity.data.ResidentialRepository
import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.ResponseHttp
import retrofit2.Call
import retrofit2.Response

class RegisterResidentialUseCase {

    private val repository = ResidentialRepository()

    suspend operator fun invoke(residential: Residential): Response<ResponseHttp>? {
        Log.d("JHONNY", "HOLA ERROR EN EL DOMINIO")
        return repository.getRegisterResidential(residential)
    }

}