package com.example.smartenroll1.MainScreens.Models

import androidx.lifecycle.ViewModel
import com.example.smartenroll1.SmartEnrolCaller
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountDetailViewModel : ViewModel() {
    private val _accountId = MutableStateFlow(-1)
    private val _account = MutableStateFlow<StudentAccountProfileModel?>(null)
    val account = _account.asStateFlow()

    fun setAccountId(accountId: Int) {
        _accountId.value = accountId

        fetchDetail { accountDetail ->
            _account.value = accountDetail
        }
    }

    private fun fetchDetail(account: (StudentAccountProfileModel) -> Unit) {
        val api = SmartEnrolCaller.getApi()

        val response =
            api.getAccountById(_accountId.value).enqueue(object : Callback<GetAccountModel> {
                override fun onResponse(
                    call: Call<GetAccountModel>,
                    response: Response<GetAccountModel>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            account(it.account)
                        }
                    }
                }

                override fun onFailure(call: Call<GetAccountModel>, throwable: Throwable) {
                    throwable.printStackTrace()
                }
            })

    }
}
