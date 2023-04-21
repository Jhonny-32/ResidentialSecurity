package com.edifice.residentialsecurity.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


class User(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") var name: String,
    @SerializedName("lastname") var lastname: String,
    @SerializedName("phone") var phone: String,
    @SerializedName("email") val email: String ? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("dni") val dni: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("session_token") val sessionToken: String? = null,
    @SerializedName("conjunto") val conjunto: String? = null,
    @SerializedName("roles") val roles: ArrayList<Rol>? = null,

    @SerializedName("tower") var tower: String ? = null,
    @SerializedName("apartament") var apartament: String? = null,
    @SerializedName("residential") var residential: String? = null,
    @SerializedName("residentialID") var residentialID: String? = null,
    @SerializedName("idset") var idset: String? = null,
    @SerializedName("setid") var setid: String? = null
) {
    fun getFullName(): String = "$name $lastname"



    fun isNotEmpty() = name.isNotEmpty() && lastname.isNotEmpty() && phone.isNotEmpty() && email!!.isNotEmpty() && dni!!.isNotEmpty() && password!!.isNotEmpty()

    fun toJson(): String{
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "User(id=$id, idset=$idset, name='$name', lastname='$lastname', phone='$phone', email=$email, image=$image, dni=$dni, password=$password, sessionToken=$sessionToken, conjunto=$conjunto, roles=$roles, tower=$tower, apartament=$apartament, residential=$residential, residentialID=$residentialID)"
    }


}

