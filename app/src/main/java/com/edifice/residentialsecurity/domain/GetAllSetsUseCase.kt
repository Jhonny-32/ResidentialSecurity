package com.edifice.residentialsecurity.domain

import com.edifice.residentialsecurity.data.ResidentialRepository
import com.edifice.residentialsecurity.data.model.Sets
import javax.inject.Inject

class GetAllSetsUseCase @Inject constructor(
    private val repository: ResidentialRepository
) {
    suspend operator fun invoke(conjunto: String, token: String):ArrayList<Sets>{
        return repository.getAllSets(conjunto, token)
    }
}