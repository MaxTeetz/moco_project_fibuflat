package com.example.moco_project_fibuflat.ActivityGroup.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.ActivityGroup.Adapter.Data.MoneyPoolEntry
import com.example.moco_project_fibuflat.ActivityGroup.Adapter.MoneyPoolAdapter
import java.util.*

class HomeViewModel : ViewModel() {

    private val _allMoneyEntries: MutableLiveData<MutableList<MoneyPoolEntry>> =
        MutableLiveData<MutableList<MoneyPoolEntry>>() //ToDo get data from database
    val allMoneyEntries: LiveData<MutableList<MoneyPoolEntry>> get() = _allMoneyEntries //ToDo in extra class?

    fun addEntry(moneyPoolEntry: MoneyPoolEntry) {
        Log.d("addEntry", moneyPoolEntry.stringDate)
        _allMoneyEntries.value =
            (_allMoneyEntries.value?.plus(moneyPoolEntry)
                ?: listOf(moneyPoolEntry)) as MutableList<MoneyPoolEntry>?
    }

    fun getCurrentMoney(): Double {
        var amount: Double = 0.0
        allMoneyEntries.value?.forEach { e -> amount += e.stringMoneyId }
        return amount //ToDo get no number after decimal
    }

    fun getEntry(id: UUID): MoneyPoolEntry {
        allMoneyEntries.value?.forEach { e ->
            if (e.id == id)
                return e
        }
        throw error("nothing found") //ToDo
    }


    fun deleteEntry(id: UUID) {
        //_allMoneyEntries.value?.clear()
        Log.d("viewModel", "viewModel: " + id.toString())
        for ((i, value) in _allMoneyEntries.value!!.withIndex()) {
            if (value.id == id) {
                Log.d("viewModel", "checked: " + i)
                _allMoneyEntries.value?.removeAt(i)
                val adapter = MoneyPoolAdapter
            }
        }
    }
}