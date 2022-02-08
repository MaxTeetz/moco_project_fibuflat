package com.example.moco_project_fibuflat.activityGroup.data

data class MoneyPoolEntry(
    val id: String, //identifier of entry -> random UUID to string
    val stringUser: String, //username
    val moneyAmount: Int, //money
    val stringDate: String, //date
    val stringInfo: String //additional info
)

data class MoneyGoal(
    var goalMoney: Double,
    var currentMoney: Double
)
