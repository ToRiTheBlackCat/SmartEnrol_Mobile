package com.example.smartenroll1.mainScreens.Models

import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartenroll1.PaginatedAccountList
import com.example.smartenroll1.SmartEnrolCaller
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import java.util.GregorianCalendar
import kotlin.math.ceil

class AccountListViewModel() : ViewModel() {
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
    private val _pageCount = MutableStateFlow(0)
    val pageCount = _pageNumber.asStateFlow()

    // Chart Data
    private val _monthEntries = MutableStateFlow<ArrayList<BarEntry>>(arrayListOf())
    val monthEntries = _monthEntries.asStateFlow()

    // Temp OkHttpClient
    private val _client = MutableStateFlow<OkHttpClient?>(null)
    val tempClient = _client.asStateFlow()

    init {
        startPolling()
    }

    private fun startPolling() {
        viewModelScope.launch {
            while (true) {
                fetchFromSeverFast()

                delay(5000) // 🔄 Poll every 5 seconds
            }
        }
    }

    private fun fetchDataFromServer(
        name: String = "", sortByNewestDate: Boolean = false,
        pageSize: Int = 10, pageNumber: Int = 1,
        accounts: (List<AccountItemModel>) -> Unit,
        pageCount: (Int) -> Unit
    ) {
        val api = SmartEnrolCaller.getApi(tempClient.value)

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

    fun fetchFromSeverFast() {
        viewModelScope.launch {
            try {
                val api = SmartEnrolCaller.getApi(tempClient.value)
                val paginatedResult = api.getAccountListSuspend(
                    sortNewestDate = isSearchPage.value,
                    name = filterName.value,
                    pageNumber = _pageNumber.value,
                    pageSize = _pageSize.value,
                )
                if (paginatedResult.isSuccessful && paginatedResult.body() != null) {
                    _listAccount.value = paginatedResult.body()!!.accounts
                    _pageCount.value =
                        ceil(_listAccount.value.count() / _pageSize.value * 1f).toInt()
                }

            } catch (e: Exception) {
                e.printStackTrace()  // Handle error
            }
        }
    }

    fun getChartData() {
        try {
            //Get user registration count or previous 5 months
            viewModelScope.launch {
                val entries = ArrayList<BarEntry>()

                for (i in 4 downTo 0) {
                    val calendar = GregorianCalendar()
                    calendar.add(Calendar.MONTH, if (i > 0) -i else 0)
                    val month = calendar.get(Calendar.MONTH) + 1

                    val monthResult = SmartEnrolCaller.getApi(tempClient.value).getByMonthSimple(month)

                    if (monthResult.isNotEmpty()) {
                        val monthCount = monthResult.count()

                        if (i == 0) {
                            _registeredInMonth.value = monthCount
                        }
                        val temp = BarEntry(4f - i, (monthCount).toFloat(), month)
                        entries.add(temp)
                    } else {
                        val temp = BarEntry(4f - i, 0f, month)
                        entries.add(temp)
//                        throw Exception("getByMonth api call failed at month ${tempMonth}")
                    }
                }

                if (entries.isNotEmpty())
                    _monthEntries.value = entries
            }
        } catch (e: Exception) {
            e.printStackTrace()  // Handle error
        }
    }

    fun onSearch(name: String, pageNumber: Int = 1) {
        _filterName.value = name
        _pageNumber.value = pageNumber

        fetchFromSeverFast()
//        fetchDataFromServer(
//            sortByNewestDate = isSearchPage.value,
//            name = filterName.value,
//            pageNumber = _pageNumber.value,
//            pageSize = _pageSize.value,
//            accounts = { accounts ->
//                _listAccount.value = accounts
//            },
//            pageCount = { totalPages ->
//                _pageCount.value = totalPages
//            }
//        )
    }


    companion object {

    }
}