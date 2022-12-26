package com.edifice.residentialsecurity.data.model

import com.google.gson.annotations.SerializedName

class Rol(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String
) {
    override fun toString(): String {
        return "Rol(id='$id', name='$name', image='$image')"
    }
}