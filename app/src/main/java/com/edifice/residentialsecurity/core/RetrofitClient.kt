package com.edifice.residentialsecurity.core

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    fun getClientWithToken(url: String, token: String): Retrofit{
        val client = OkHttpClient.Builder()
        client.addInterceptor{chain ->
            val request = chain.request()
            val newRequest = request.newBuilder().header("Authorization", token)
            chain.proceed(newRequest.build())
        }
        return Retrofit.Builder()
            .baseUrl(url)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}