package com.edifice.residentialsecurity.domain

import com.edifice.residentialsecurity.data.ResidentialRepository
import com.edifice.residentialsecurity.data.model.User
import javax.inject.Inject

class GetDataUseCase @Inject constructor(
    private val repository: ResidentialRepository
) {
    suspend operator fun invoke(rol: String, residential: String, token: String): ArrayList<User>{
        return repository.getUserData(rol, residential, token)
    }
}