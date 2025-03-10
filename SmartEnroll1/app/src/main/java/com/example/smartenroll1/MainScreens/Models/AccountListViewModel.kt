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

class AccountListViewModel:ViewModel() {
    private val _listAccount = MutableStateFlow<List<AccountItemModel>>(emptyList())
    val listAccount = _listAccount.asStateFlow()

    init {
        startPolling()
    }

    private fun startPolling() {
        viewModelScope.launch {
            while (true) {
                fetchDataFromServer { accountModels ->
                    _listAccount.value = accountModels
                }
                delay(5000) // 🔄 Poll every 5 seconds
            }
        }
    }

    private fun fetchDataFromServer(accounts: (List<AccountItemModel>) -> Unit){
        val api = SmartEnrolCaller.getApi()

        val response = api.getAllAccount().enqueue(object : Callback<List<AccountItemModel>> {
            override fun onResponse(
                call: Call<List<AccountItemModel>>,
                response: Response<List<AccountItemModel>>
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
}