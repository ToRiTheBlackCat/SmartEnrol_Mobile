package com.example.smartenroll1

data class LoginRequest(
    var email: String,
    var password: String,
)

data class LoginResponse(
    var accountId: String,
    var token: String,
)

data class WeatherResponse(
    var date: String,
    var temperatureC: Float,
    var temperatureF: Float,
    var summary: String
)
