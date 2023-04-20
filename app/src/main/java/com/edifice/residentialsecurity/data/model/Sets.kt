package com.edifice.residentialsecurity.data.model

import com.google.gson.annotations.SerializedName

class Sets
    (
    @SerializedName("id") val id  : String? = null,
    @SerializedName("tower") val tower : String,
    @SerializedName("apartament") val apartament : String,
    @SerializedName("idclient") val idClient : String? = null,
    @SerializedName("name") val name : String? = null,
    @SerializedName("lastname") val lastname : String? = null
){
    fun isNotEmpty() = tower.isNotEmpty() && apartament.isNotEmpty()
    override fun toString(): String {
        return "Torre: $tower | Apartamento: $apartament"
    }

}