package com.example.moco_project_fibuflat.ActivityGroup.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.ActivityGroup.Adapter.Data.MoneyPoolEntry

class HomeViewModel : ViewModel() {

    private val _allMoneyEntries: MutableLiveData<List<MoneyPoolEntry>> = MutableLiveData()
    val allMoneyEntries: LiveData<List<MoneyPoolEntry>> get() = _allMoneyEntries

    fun addEntry(moneyPoolEntry: MoneyPoolEntry){
        _allMoneyEntries.value = _allMoneyEntries.value?.plus(moneyPoolEntry) ?: listOf(moneyPoolEntry)
    }
}