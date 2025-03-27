package com.example.smartenroll1.mainScreens.Models

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartenroll1.PaginatedAccountList
import com.example.smartenroll1.SmartEnrolCaller
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import java.util.GregorianCalendar
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

    private val _monthEntries = MutableStateFlow<ArrayList<BarEntry>>(arrayListOf())
    val monthEntries = _monthEntries.asStateFlow()


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



                if (isSearchPage.value != false) {
                    // Get user registration count or previous 5 months
                    val calendar = GregorianCalendar()
                    val entries = ArrayList<BarEntry>()

                    for (i in 0..4) {
                        calendar.add(Calendar.MONTH, if (i > 0) -1 else 0)
                        val tempMonth = calendar.get(Calendar.MONTH) + 1

                        getMonthlyRegistration(tempMonth) { count ->
                            if (i == 0) {
                                _registeredInMonth.value = count
                            }

                            entries.add(BarEntry(i.toFloat(), count.toFloat(), tempMonth))
                        }
                    }

                    _monthEntries.value = entries
                }

                delay(5000) // 🔄 Poll every 20 seconds
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

    private fun getMonthlyRegistration(month: Int, count: (Int) -> Unit) {
        when (month) {
            0 -> count(20)
            1 -> count(20)
            2 -> count(46)
            3 -> count(15)
            4 -> count(10)
            5 -> count(25)
            6 -> count(60)
            7 -> count(60)
            8 -> count(55)
            9 -> count(33)
            10 -> count(10)
            11 -> count(0)
            12 -> count(12)
        }
        return

        val api = SmartEnrolCaller.getApi()

        val response =
            api.getAccountsByMonth(month).enqueue(object : Callback<List<AccountItemModel>> {
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