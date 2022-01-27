package com.example.moco_project_fibuflat.ActivityGroup.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.ActivityGroup.Adapter.Data.MoneyPoolEntry

class HomeViewModel : ViewModel() {

    private val _allMoneyEntries: MutableLiveData<List<MoneyPoolEntry>> = MutableLiveData()
    val allMoneyEntries: LiveData<List<MoneyPoolEntry>> get() = _allMoneyEntries

    fun addEntry(moneyPoolEntry: MoneyPoolEntry) {
        Log.d("addEntry", moneyPoolEntry.stringDate)
        _allMoneyEntries.value =
            _allMoneyEntries.value?.plus(moneyPoolEntry) ?: listOf(moneyPoolEntry)
    }

    fun getCurrentMoney(): Double {
        var amount: Double = 0.0
        allMoneyEntries.value?.forEach { e -> amount += e.stringMoneyId }
        return amount //ToDo get no number after decimal
    }
}