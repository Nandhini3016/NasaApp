package com.example.nasaapp.service

import com.example.nasaapp.model.NasaInfoResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaInfoService {

    @GET("planetary/apod")
    suspend fun getNasaInfo(@Query("api_key") apiKey : String) : NasaInfoResponse


    companion object {
        val BASE_URL = "https://api.nasa.gov/"

        fun create() : NasaInfoService {
           /* val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC*/
            val client = OkHttpClient.Builder()
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NasaInfoService::class.java)
        }
    }
}