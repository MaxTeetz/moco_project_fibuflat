package com.example.moco_project_fibuflat.ActivityGroup.ui.home.AddEntry

import androidx.lifecycle.ViewModel

class AddEntryViewModel : ViewModel() {

    fun isEntryValid(moneyAmount: String): Boolean {
        if(moneyAmount.toInt() > 20000)
            return false
        return moneyAmount.isNotBlank()
    }
}