package com.edifice.residentialsecurity.domain

import com.edifice.residentialsecurity.data.ResidentialRepository
import com.edifice.residentialsecurity.data.model.ResponseHttp
import retrofit2.Response
import javax.inject.Inject

class SendRegisterUserUseCase @Inject constructor(
    private val repository : ResidentialRepository
) {
    suspend operator fun invoke(idResidential: String, idUser: String):Response<ResponseHttp>?{
        return repository.sendResidentialUser(idResidential, idUser)
    }
}