package com.example.smartenroll1.MainScreens.Models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartenroll1.SmartEnrolCaller
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountListViewModel : ViewModel() {
    private val _listAccount = MutableStateFlow<List<AccountItemModel>>(emptyList())
    private val _registeredInMonth = MutableStateFlow(0)
    val listAccount = _listAccount.asStateFlow()
    val monthCount = _registeredInMonth.asStateFlow()

    init {
        startPolling()
    }

    private fun startPolling() {
        viewModelScope.launch {
            while (true) {
                fetchDataFromServer { accounts ->
                    _listAccount.value = accounts
                }
                getMonthlyRegistration { count ->
                    _registeredInMonth.value = count
                }
                delay(5000) // 🔄 Poll every 5 seconds
            }
        }
    }

    private fun fetchDataFromServer(accounts: (List<AccountItemModel>) -> Unit) {
        val api = SmartEnrolCaller.getApi()

        val response = api.getAllAccount().enqueue(object : Callback<List<AccountItemModel>> {
            override fun onResponse(
                call: Call<List<AccountItemModel>>, response: Response<List<AccountItemModel>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        accounts(it)
                        for (account in it) {
                            Log.i(SmartEnrolCaller.TAG, "On response: ${account.accountName}")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<AccountItemModel>>, throwable: Throwable) {
                throwable.printStackTrace()
            }
        })
    }

    private fun getMonthlyRegistration(count: (Int) -> Unit) {
        val api = SmartEnrolCaller.getApi()
        val thisMonth = 3

        val response =
            api.getAccountsByMonth(thisMonth).enqueue(object : Callback<List<AccountItemModel>> {
                override fun onResponse(
                    p0: Call<List<AccountItemModel>>,
                    p1: Response<List<AccountItemModel>>
                ) {
                    if (p1.isSuccessful) {
                        p1.body()?.let {
                            count(it.count())
                        }
                    }
                }

                override fun onFailure(p0: Call<List<AccountItemModel>>, p1: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }
}