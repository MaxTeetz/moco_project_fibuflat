package com.example.moco_project_fibuflat.data

data class MoneyPoolEntry(
    val id: String? = null, //identifier of entry -> random UUID to string
    val stringUser: String? = null, //username
    val moneyAmount: Int? = null, //money
    val stringDate: String? = null, //date
    val stringInfo: String? = null //additional info
)

data class MoneyGoal(
    var currentMoney: Double? = null,
    var goalMoney: Double? = null
)
