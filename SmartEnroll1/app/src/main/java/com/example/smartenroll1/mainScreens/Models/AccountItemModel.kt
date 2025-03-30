package com.example.smartenroll1.mainScreens.Models


data class AccountItemModel(
    val accountId: Int,
    val accountName: String,
    val email: String,
    val roleId: Int,
    val areaId: Int,
    val createdDate: String
)

data class StudentAccountProfileModel(
    val accountId:Int,
    val accountName: String,
    val email: String,
    val areaId: Int,
    val areaName: String
)


data class GetAccountModel(
    val account: StudentAccountProfileModel,
    val message: String
)