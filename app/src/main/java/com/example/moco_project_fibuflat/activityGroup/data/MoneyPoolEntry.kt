package com.example.moco_project_fibuflat.activityGroup.data

import java.util.*

data class MoneyPoolEntry(
    val id: UUID,
    val stringUser: String,
    val moneyAmount: Int,
    val stringDate: String,
    val stringInfo: String
)
