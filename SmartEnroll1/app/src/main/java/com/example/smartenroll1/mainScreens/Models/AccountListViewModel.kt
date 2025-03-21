package com.example.smartenroll1.mainScreens.Models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartenroll1.PaginatedAccountList
import com.example.smartenroll1.SmartEnrolCaller
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ceil

class AccountListViewModel : ViewModel() {
    private val _listAccount = MutableStateFlow<List<AccountItemModel>>(emptyList())
    private val _registeredInMonth = MutableStateFlow(0)
    val isSearchPage = MutableStateFlow(false)
    val listAccount = _listAccount.asStateFlow()
    val monthCount = _registeredInMonth.asStateFlow()

    // Search params
    private val _filterName = MutableStateFlow("")
    val filterName = _filterName.asStateFlow()
    private val _pageNumber = MutableStateFlow(1)
    val pageNumber = _pageNumber.asStateFlow()
    private val _pageSize = MutableStateFlow(10)
    val pageSize = _pageNumber.asStateFlow()

    private val _pageCount = MutableStateFlow(0)
    val pageCount = _pageNumber.asStateFlow()


    init {
        startPolling()
    }

    private fun startPolling() {
        viewModelScope.launch {
            while (true) {
                fetchDataFromServer(
                    sortByNewestDate = isSearchPage.value,
                    name = filterName.value,
                    accounts = { accounts ->
                        _listAccount.value = accounts
                    },
                    pageCount = { totalPages ->
                        _pageCount.value = totalPages
                    }
                )

                getMonthlyRegistration { count ->
                    _registeredInMonth.value = count
                }
                delay(20000) // 🔄 Poll every 20 seconds
            }
        }
    }

    private fun fetchDataFromServer(
        name: String = "", sortByNewestDate: Boolean = false,
        pageSize: Int = 10, pageNumber: Int = 1,
        accounts: (List<AccountItemModel>) -> Unit,
        pageCount: (Int) -> Unit
    ) {
        val api = SmartEnrolCaller.getApi()

        val response = api.getAccountList(name, sortByNewestDate, pageSize, pageNumber)
            .enqueue(object : Callback<PaginatedAccountList> {
                override fun onResponse(
                    call: Call<PaginatedAccountList>, response: Response<PaginatedAccountList>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            accounts(it.accounts)
                            pageCount(ceil((it.totalCounts / pageSize * 1f)).toInt())
                            for (account in it.accounts) {
                                Log.i(SmartEnrolCaller.TAG, "On response: ${account.accountName}")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<PaginatedAccountList>, throwable: Throwable) {
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

    public fun onSearch(name: String, pageNumber: Int = 1) {
        _filterName.value = name
        _pageNumber.value = pageNumber

        fetchDataFromServer(
            sortByNewestDate = isSearchPage.value,
            name = filterName.value,
            pageNumber = _pageNumber.value,
            pageSize = _pageSize.value,
            accounts = { accounts ->
                _listAccount.value = accounts
            },
            pageCount = { totalPages ->
                _pageCount.value = totalPages
            }
        )
    }
}