package com.example.smartenroll1.mainScreens.Singleton

import android.icu.util.Calendar
import com.example.smartenroll1.SmartEnrolCaller
import com.example.smartenroll1.mainScreens.Models.AccountItemModel
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.util.ArrayList
import java.util.GregorianCalendar

object NotificationRepository {
    private val _notificationReceived = MutableStateFlow<String>("")
    val notificationReceived = _notificationReceived.asStateFlow()

    fun setNotificationReceived(notification: String) {
        _notificationReceived.value = notification
    }
}