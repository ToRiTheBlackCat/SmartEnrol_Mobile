package com.example.smartenroll1

import com.example.smartenroll1.mainScreens.Models.AccountItemModel
import com.example.smartenroll1.mainScreens.Models.GetAccountModel
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

class SmartEnrolCaller {
    companion object {
        private val BASE_URL = "https://smartenrol2.azurewebsites.net/api/"
        const val TAG: String = "SMARTENROLL_API"

        fun getApi(): SmartEnrolApi {
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

    @GET("Account/paged")
    fun getAccountList(
        @Query("name") name: String = "",
        @Query("sortByNewestDate") sortNewestDate: Boolean = false,
        @Query("pageSize") pageSize: Int = 10,
        @Query("pageNumber") pageNumber: Int = 1
    ): Call<PaginatedAccountList>

    @GET("Account/{accountId}")
    fun getAccountById(@Path("accountId") accountId: Int): Call<GetAccountModel>

    @GET("Account/get-month/{month}")
    fun getAccountsByMonth(@Path("month") month: Int): Call<List<AccountItemModel>>

    @GET("Account/get-month/{month}")
    suspend fun getByMonthSimple(@Path("month") month: Int): List<AccountItemModel>

    @GET("Account/paged")
    suspend fun getAccountListSuspend(
        @Query("name") name: String = "",
        @Query("sortByNewestDate") sortNewestDate: Boolean = false,
        @Query("pageSize") pageSize: Int = 10,
        @Query("pageNumber") pageNumber: Int = 1
    ): Response<PaginatedAccountList>
}

data class PaginatedAccountList(
    val totalCounts: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val accounts: List<AccountItemModel>
)

