package com.edifice.residentialsecurity.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("id") val id  : String? = null,
    @SerializedName("name") var name  : String,
    @SerializedName("lastname") var lastname  : String,
    @SerializedName("phone") var phone  : String,
    @SerializedName("email") val email  : String,
    @SerializedName("image") val image  : String ? = null,
    @SerializedName("dni") val dni  : String,
    @SerializedName("password") val password  : String,
    @SerializedName(  "session_token") val sessionToken: String? = null,
    @SerializedName("roles") val roles: ArrayList<Rol>?= null
) {

    override fun toString(): String {
        return "User(id=$id, name='$name', lastname='$lastname', phone='$phone', email='$email', image=$image, dni='$dni', password='$password', sessionToken=$sessionToken, roles=$roles)"
    }

    fun toJson(): String{
        return Gson().toJson(this)
    }



}