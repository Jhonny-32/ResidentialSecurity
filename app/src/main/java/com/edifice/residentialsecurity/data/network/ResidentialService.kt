package com.edifice.residentialsecurity.data.network

import com.edifice.residentialsecurity.core.ApiRoutes
import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class ResidentialService @Inject constructor(
    private val  residentialRoutes: ResidentialsRoutes,
    private val userRoutes: UserRoutes) {
    
    suspend fun registerResidential(residential : Residential): Response<ResponseHttp>?{
        return withContext(Dispatchers.IO){
            residentialRoutes.register(residential)
        }
    }

    suspend fun registerUser(user: User):Response<ResponseHttp>?{
        return withContext(Dispatchers.IO){
            userRoutes.register(user)
        }
    }

    suspend fun login(email: String, password: String) : Response<ResponseHttp>?{
        return withContext(Dispatchers.IO){
            userRoutes.login(email, password)
        }
    }

    suspend fun getDataResident(conjunto: String, token : String): ArrayList<User>? {
        return withContext(Dispatchers.IO){
            userRoutes?.getDataResident(conjunto, token)
        }
    }

}