package com.example.smartenroll1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smartenroll1.databinding.ActivityLoginBinding
import com.example.smartenroll1.databinding.FragmentHomeBinding
import android.Manifest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(binding.main.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLogin.setOnClickListener {
            loginAccount()
//            testNotification()
        }
    }

    private val BASE_URL = "https://smartenrol.azurewebsites.net/api/"
    private val TAG: String = "CHECK_RESPONSE"
    fun loginAccount() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SmartEnrolApi::class.java)

        val loginRequest = LoginRequest(
            binding.etAcount.text.toString(),
            binding.etPassword.text.toString()
        )

        api.accountLogin(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "UserId: ${response.body()?.accountId}")
                    Log.i(TAG, "Token: ${response.body()?.token}")
                    TOKEN = response.body()?.token ?: ""
                }
            }

            override fun onFailure(call: Call<LoginResponse>, response: Throwable) {
                Log.i(TAG, "Login call failed.")
            }

        })
    }

    private lateinit var TOKEN: String
    fun testWeather() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SmartEnrolApi::class.java)

        api.getWeatherForecast(TOKEN).enqueue(object : Callback<List<WeatherResponse>> {
            override fun onResponse(
                call: Call<List<WeatherResponse>>,
                response: Response<List<WeatherResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (weather in it) {
                            Log.i(TAG, "Weather: ${weather}")
                        }
                    }

                }
            }

            override fun onFailure(call: Call<List<WeatherResponse>>, throwable: Throwable) {
                Log.i(TAG, "Get call failed.")
            }


        })
    }


    private val CHANNEL_ID: String = "all_notifications"
    private val NOTIFICATION_ID: Int = 1
    fun testNotification() {
        createNotificationChannel()

        val intent = Intent(applicationContext, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_MUTABLE)

        val builder = NotificationCompat
            .Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_account)
            .setContentTitle("My notification")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            // Set the intent that fires when the user taps the notification.
            .setContentIntent(pendingIntent)

        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        with(mNotificationManager) {
            notify(123, builder.build())

        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                CHANNEL_ID,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "This is default channel used for all other notifications"
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

    }


}