package com.edifice.residentialsecurity.data

import com.edifice.residentialsecurity.data.model.*
import com.edifice.residentialsecurity.data.network.ResidentialService
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class ResidentialRepository @Inject constructor(private val residentialApi : ResidentialService){

    suspend fun getRegisterResidential(residential: Residential):Response<ResponseHttp>? {
        return residentialApi.registerResidential(residential)
    }

    suspend fun getRegisterUser(user: User):Response<ResponseHttp>?{
        return residentialApi.registerUser(user)
    }

    suspend fun getLogin(email: String, password: String):Response<ResponseHttp>?{
        return residentialApi.login(email, password)
    }

    suspend fun getDataResident(conjunto: String, token: String): ArrayList<User>? {
        return residentialApi.getDataResident(conjunto, token)
    }

    suspend fun sendImageUser(file: File, user: User, token: String):Response<ResponseHttp>?{
        return residentialApi.updateUserImage(file,user,token)
    }

    suspend fun updateUser(user: User): Response<ResponseHttp>{
        return residentialApi.updateUserWithOut(user)
    }

    suspend fun sendResidentialUser(idResidential: String, idUser: String): Response<ResponseHttp>?{
        return residentialApi.createResidentialUser(idResidential, idUser)
    }

    suspend fun sendOrder(files : List<File>, order: Order):Response<ResponseHttp>{
        return residentialApi.createOrders(files, order)
    }

    suspend fun getOrdersByStatus(status: String,conjunto: String ,token: String):ArrayList<Order>{
        return residentialApi.getOrdersByStatus(status, conjunto ,token)
    }

    suspend fun getOrdersByClientAndStatus(
        status: String,
        conjunto: String,
        idClient: String,
        token: String
    ):ArrayList<Order>{
        return residentialApi.getOrdersByClienAndStatus(status, conjunto, idClient, token)
    }

    suspend fun getAllSets(cojunto: String, token: String):ArrayList<Sets>{
        return residentialApi.getAllSets(cojunto, token)
    }

    suspend fun updateOrder(order: Order, token: String):Response<ResponseHttp>{
        return residentialApi.updateOrder(order, token)
    }
    suspend fun getUserData(rol: String, residential: String, token: String): ArrayList<User>{
        return residentialApi.getDataUser(rol, residential, token)
    }
    suspend fun createSecurity(security: User, token: String):Response<ResponseHttp>{
        return residentialApi.createSecurity(security, token)
    }
    suspend fun createResident(resident: User, token: String):Response<ResponseHttp>{
        return residentialApi.createResident(resident,token)
    }
    suspend fun updateResident(resident: User, token: String):Response<ResponseHttp>{
        return residentialApi.updateResident(resident, token)
    }

}