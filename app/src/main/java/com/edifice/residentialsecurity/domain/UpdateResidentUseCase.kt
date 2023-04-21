package com.edifice.residentialsecurity.domain

import com.edifice.residentialsecurity.data.ResidentialRepository
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import retrofit2.Response
import javax.inject.Inject

class UpdateResidentUseCase @Inject constructor(
    private val repository: ResidentialRepository
) {

    suspend operator fun invoke(resident: User, token: String):Response<ResponseHttp>{
        return repository.updateResident(resident, token)
    }

}