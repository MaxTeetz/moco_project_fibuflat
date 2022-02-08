package com.example.moco_project_fibuflat.activityGroup.data

import java.util.*

data class MoneyPoolEntry(
    val id: UUID, //identifier of entry
    val stringUser: String, //username
    val moneyAmount: Int, //money
    val stringDate: String, //date
    val stringInfo: String //additional info
)

data class MoneyGoal(
    var goalMoney: Double,
    var currentMoney: Double
)
