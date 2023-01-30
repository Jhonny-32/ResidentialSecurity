package com.edifice.residentialsecurity.domain

import com.edifice.residentialsecurity.data.ResidentialRepository
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class SendUseImageUseCase @Inject constructor(
    private val repository : ResidentialRepository
) {
    suspend operator fun invoke(file: File, user: User, token: String):Response<ResponseHttp>?{
        return repository.sendImageUser(file, user, token)
    }
}