package com.edifice.residentialsecurity.domain

import com.edifice.residentialsecurity.data.ResidentialRepository
import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.ResponseHttp
import retrofit2.Response
import javax.inject.Inject

class RegisterResidentialUseCase @Inject constructor(
    private val repository : ResidentialRepository
){

    suspend operator fun invoke(residential: Residential): Response<ResponseHttp>? {
        return repository.getRegisterResidential(residential)
    }

}