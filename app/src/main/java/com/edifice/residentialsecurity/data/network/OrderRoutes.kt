package com.edifice.residentialsecurity.data.network


import com.edifice.residentialsecurity.data.model.Order
import com.edifice.residentialsecurity.data.model.ResponseHttp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface OrderRoutes {

    @GET("orders/findByStatus/{statuss}/{conjunto}")
    suspend fun getOrdersByStatus(
        @Path("statuss") status : String,
        @Path("conjunto") conjunto: String,
        @Header("Authorization") token: String
    ): ArrayList<Order>

    @GET("orders/findByClientAndStatus/{statuss}/{conjunto}/{idClient}")
    suspend fun getOrderByClientStatus(
        @Path("statuss") status: String,
        @Path("conjunto") conjunto: String,
        @Path("idClient") idClient: String,
        @Header("Authorization") token: String
    ):ArrayList<Order>

    @Multipart
    @POST("orders/create")
    suspend fun createOrder(
        @Part images: Array<MultipartBody.Part?>,
        @Part("order") order: RequestBody,
    ) : Response<ResponseHttp>

    @POST("orders/updateOrder")
    suspend fun updateOrder(
        @Body order : Order,
        @Header("Authorization") token: String
    ) : Response<ResponseHttp>

}