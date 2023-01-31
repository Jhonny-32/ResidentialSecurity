package com.edifice.residentialsecurity.data.model

import com.google.gson.annotations.SerializedName

class Sets
    (
    @SerializedName("id") val id  : String? = null,
    @SerializedName("tower") val tower : String,
    @SerializedName("apartament") val apartament : String,
    @SerializedName("idclient") val idClient : String,
    @SerializedName("name") val name : String,
    @SerializedName("lastname") val lastname : String
){
    override fun toString(): String {
        return "Torre: $tower | Apartamento: $apartament"
    }

}