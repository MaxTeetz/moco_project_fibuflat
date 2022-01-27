package com.example.moco_project_fibuflat.ActivityGroup.ui.home.AddEntry

import androidx.lifecycle.ViewModel

class AddEntryViewModel : ViewModel() {

    fun isEntryValid(moneyAmount: String): Boolean {
        if(moneyAmount.isNotBlank() && moneyAmount.toInt() < 20001)
            return true
        return false
    }
}