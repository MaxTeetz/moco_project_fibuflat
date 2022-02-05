package com.example.moco_project_fibuflat.activityGroup.ui.home.poolEntry

import androidx.lifecycle.ViewModel

class AddEntryViewModel : ViewModel() {

    fun isEntryValid(moneyAmount: String, message: String): Boolean {
        if(moneyAmount.isNotBlank() && moneyAmount.toInt() < 20001 && message.isNotBlank())
            return true
        return false
    }
}