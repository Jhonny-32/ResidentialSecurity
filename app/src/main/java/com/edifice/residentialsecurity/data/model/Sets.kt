package com.edifice.residentialsecurity.data.model

import com.google.gson.annotations.SerializedName

class Sets
    (
    @SerializedName("id") val id  : String? = null,
    @SerializedName("tower") val tower : String,
    @SerializedName("apartament") val apartament : String
){
    override fun toString(): String {
        return "Sets(id=$id, tower='$tower', apartament='$apartament')"
    }
}