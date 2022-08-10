package com.edifice.residentialsecurity.models

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("id") val id  : String? = null,
    @SerializedName("name") val name  : String,
    @SerializedName("lastname") val lastname  : String,
    @SerializedName("phone") val phone  : String,
    @SerializedName("email") val email  : String,
    @SerializedName("image") val image  : String ? = null,
    @SerializedName("dni") val dni  : String,
    @SerializedName("password") val password  : String,
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("lng") val lng: Double? = null,
) {
    override fun toString(): String {
        return "User(id=$id, name='$name', lastname='$lastname', phone='$phone', email='$email', image=$image, dni='$dni', password='$password', lat=$lat, lng=$lng)"
    }
}