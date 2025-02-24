package com.example.smartenroll1

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SmartEnrolApi {
    @GET("comments")
    fun getComments(): Call<List<Comments>>

    @POST("Account/login")
    fun accountLogin(@Body request: LoginRequest): Call<LoginResponse>

    @GET("WeatherForecast")
    fun getWeatherForecast(@Header("Authorization") token: String): Call<List<WeatherResponse>>

//    @POST("/send")
//    suspend fun sendMessage(@Body body: SendMessageDto)
//
//    @POST("/broadcast")
//    suspend fun broadcastMessage(@Body body: SendMessageDto)

}