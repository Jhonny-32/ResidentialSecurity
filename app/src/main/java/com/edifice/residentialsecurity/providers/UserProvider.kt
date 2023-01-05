/*package com.edifice.residentialsecurity.providers

import com.edifice.residentialsecurity.core.ApiRoutes
import com.edifice.residentialsecurity.data.model.ResponseHttp
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.data.network.UserRoutes
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class UserProvider(val token: String? =null) {

    private var userRoutes: UserRoutes? = null
    private var userRoutesToken: UserRoutes? = null

    init {
        val api = ApiRoutes()
        userRoutes = api.getUserRoutes()

        if (token != null){
            userRoutesToken = api.getUsersRoutesWithToken(token!!)
        }

    }
    /*
    fun login(email : String, password : String): Call<ResponseHttp>?{
        return userRoutes?.login(email,password)
    }*/
    fun updateWithOutImage(user: User): Call<ResponseHttp>?{
        return userRoutesToken?.updateWithOutImage(user, token!!)
    }
    fun update(file: File, user: User): Call<ResponseHttp>? {
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), user.toJson())

        return userRoutesToken?.update(image, requestBody, token!!)
    }

    fun getDataResident(conjunto: String): Call<ArrayList<User>>?{
        return userRoutesToken?.getDataResident(conjunto, token!!)
    }

}*/