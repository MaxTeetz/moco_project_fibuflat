package com.example.moco_project_fibuflat.ActivityGroup.ui.home

import androidx.lifecycle.ViewModel

class AddEntryViewModel : ViewModel() {

    fun isEntryValid(moneyAmount: String): Boolean {
        if (moneyAmount.isBlank())
            return false
        return true
    }

    fun addItem(moneyAmount: Int) {
        val viewModel = HomeViewModel()
        //val moneyPoolEntry = MoneyPoolEntry(R.string.,moneyAmount) ToDo
        //Log.d("addEntry", moneyAmount.toString())
        //viewModel.addEntry(moneyPoolEntry)
    }
}