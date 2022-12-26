package com.edifice.residentialsecurity.data.model

import com.google.gson.annotations.SerializedName

class Residential(
    @SerializedName("id") val id  : String? = null,
    @SerializedName("name") val name  : String,
    @SerializedName("nit") val nit  : String,
    @SerializedName("address") val address  : String,
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("lng") val lng: Double? = null,

)
{
    override fun toString(): String {
        return "Residential(id='$id', name='$name', nit='$nit', address='$address', lat=$lat, lng=$lng)"
    }

    fun isNotEmpty() = name.isNotEmpty() && nit.isNotEmpty() && address.isNotEmpty()


}