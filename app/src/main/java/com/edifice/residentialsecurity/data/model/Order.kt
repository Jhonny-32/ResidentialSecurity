package com.edifice.residentialsecurity.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id") val id  : String?= null,
    @SerializedName("idsets") val idsets: String,
    @SerializedName("iduser") val iduser: String,
    @SerializedName("image1") val image1: String?=null,
    @SerializedName("image2") val image2: String?=null,
    @SerializedName("image3") val image3: String?=null,
    @SerializedName("descriptions") val descriptions: String,
    @SerializedName("statuss") val status: String,
    @SerializedName("timestamp") val timestamp: Long = 0,
    @SerializedName("client") val client: User ?=null,
    @SerializedName("sets") val sets: Sets ?= null
) {

    fun toJson():String{
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Order(id=$id, idsets=$idsets, iduser=$iduser, image1='$image1', image2='$image2', image3='$image3', descriptions='$descriptions', status='$status')"
    }
}