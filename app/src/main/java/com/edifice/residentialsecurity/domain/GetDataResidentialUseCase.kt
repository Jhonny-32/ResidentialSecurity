package com.edifice.residentialsecurity.domain

import com.edifice.residentialsecurity.data.ResidentialRepository
import com.edifice.residentialsecurity.data.model.User
import javax.inject.Inject

class GetDataResidentialUseCase @Inject constructor(
    private val repository: ResidentialRepository
) {

    suspend operator fun invoke(conjunto: String, token: String): ArrayList<User>? {
        return repository.getDataResident(conjunto, token)
    }

}