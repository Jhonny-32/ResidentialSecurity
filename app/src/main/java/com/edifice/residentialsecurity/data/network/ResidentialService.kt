package com.edifice.residentialsecurity.data.network

import com.edifice.residentialsecurity.core.ApiRoutes
import com.edifice.residentialsecurity.data.model.Order
import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import javax.inject.Inject

class ResidentialService @Inject constructor(
    private val  residentialRoutes: ResidentialsRoutes,
    private val userRoutes: UserRoutes,
    private val orderRoutes: OrderRoutes
    ) {
    
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

    suspend fun updateUserImage(file: File, user: User, token: String): Response<ResponseHttp>? {
        return withContext(Dispatchers.IO){
            val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
            val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
            val requestBody = RequestBody.create(MediaType.parse("text/plain"), user.toJson())

            userRoutes?.update(image, requestBody, token)
        }
    }

    suspend fun updateUserWithOut(user: User, token: String):Response<ResponseHttp>?{
        return withContext(Dispatchers.IO){
            userRoutes.updateWithOutImage(user, token)
        }
    }

    suspend fun createResidentialUser(idResidential: String, idUser: String): Response<ResponseHttp>?{
        return withContext(Dispatchers.IO){
            residentialRoutes.registerResidentialUser(idResidential,idUser)
        }
    }

    suspend fun createOrders(files: List<File>, order: Order):Response<ResponseHttp>{
            val images = arrayOfNulls<MultipartBody.Part>(files.size)

            for (i in files.indices){
                val reqFile = RequestBody.create(MediaType.parse("image/*"), files[i])
                images[i] = MultipartBody.Part.createFormData("image", files[i].name, reqFile)
            }

            val requestBody = RequestBody.create(MediaType.parse("text/plain"), order.toJson())
            return orderRoutes.createOrder(images, requestBody)
    }

    suspend fun getOrdersByStatus(status: String,conjunto: String ,token: String): ArrayList<Order>{
        return orderRoutes.getOrdersByStatus(status, conjunto ,token)
    }

    suspend fun getOrdersByClienAndStatus(
        status: String,
        conjunto: String,
        idClient: String,
        token:String)
    : ArrayList<Order>{
        return orderRoutes.getOrderByClientStatus(status, conjunto, idClient, token)
    }
}