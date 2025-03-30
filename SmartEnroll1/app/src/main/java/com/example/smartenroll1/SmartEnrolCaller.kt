package com.example.smartenroll1

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.smartenroll1.mainScreens.Models.AccountItemModel
import com.example.smartenroll1.mainScreens.Models.GetAccountModel
import com.example.smartenroll1.managers.TokenManager
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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
        const val TAG: String = "SMARTENROL_API"

        fun getApi(client: OkHttpClient?): SmartEnrolApi {
            val api = Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())

            if (client != null) {
                api.client(client)
            }

            return api.build().create(SmartEnrolApi::class.java)
        }

        fun provideOkHttpClient(context: Context): OkHttpClient {
            val tokenManager = TokenManager(context)

            return OkHttpClient.Builder()
                .addInterceptor(AuthHeaderInterceptor(tokenManager))   // Adds token to header
                .addInterceptor(AuthErrorInterceptor(context, tokenManager)) // Handles 401
                .build()
        }

        private class AuthHeaderInterceptor(private val tokenManager: TokenManager) : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val requestBuilder = chain.request().newBuilder()
                tokenManager.getToken()?.let { token ->
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
                return chain.proceed(requestBuilder.build())

            }
        }

        private class AuthErrorInterceptor(
            private val context: Context,
            private val tokenManager: TokenManager
        ) : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val response = chain.proceed(chain.request())

                if (response.code() == 401) {
                    // Token expired or invalid
                    tokenManager.clearToken()

                    // Navigate to LoginActivity (on main thread)
                    Handler(Looper.getMainLooper()).post {
                        val intent = Intent(context, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        context.startActivity(intent)
                    }
                }

                return response
            }
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



