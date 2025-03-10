package com.example.smartenroll1

import com.example.smartenroll1.MainScreens.Models.AccountItemModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

class SmartEnrolCaller {
    companion object {
        private val BASE_URL = "https://smartenrol2.azurewebsites.net/api/"
        val TAG: String = "GET_ACCOUNT"

        fun getApi(): SmartEnrolApi{
            val api = Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SmartEnrolApi::class.java)

            return api
        }
    }

}

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

    @GET("Account")
    fun getAllAccount(): Call<List<AccountItemModel>>
}