package com.example.moco_project_fibuflat.activityGroup.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.activityGroup.data.MoneyGoal
import com.example.moco_project_fibuflat.activityGroup.data.MoneyPoolEntry
import java.util.*

class HomeViewModel : ViewModel() {

    private val _allMoneyEntries = MutableListLiveData<MoneyPoolEntry>()
    val allMoneyEntries: LiveData<List<MoneyPoolEntry>> get() = _allMoneyEntries
    private val _moneyGoal = MutableLiveData(MoneyGoal(0.0,0.0))
    val moneyGoal: LiveData<MoneyGoal> get() = _moneyGoal

    fun addItem(moneyPoolEntry: MoneyPoolEntry) {
        _allMoneyEntries.add(moneyPoolEntry)
    }

    fun getGoalReached(): Boolean{
        if(_moneyGoal.value?.goalMoney!! <= currentMoney())
            return true
        return false //ToDo elvis
    }

    private fun currentMoney(): Double {
        var amount = 0.0
        _allMoneyEntries.value?.forEach { e -> amount += e.moneyAmount }
        return amount
    }

    fun moneyGoalSetGoal(goal: Double){
        moneyGoal.value?.goalMoney = goal
    }

    fun moneyGoalSetCurrent(){
        moneyGoal.value?.currentMoney = currentMoney()
    }

    fun getEntry(id: UUID): MoneyPoolEntry {
        _allMoneyEntries.value?.forEach { e ->
            if (e.id == id)
                return e
        }
        throw error("nothing found") //ToDo
    }


    fun deleteEntry(id: UUID) {
        _allMoneyEntries.remove(getEntry(id))
    }
}

class MutableListLiveData<T>(
    private val list: MutableList<T> = mutableListOf()
) : MutableList<T> by list, LiveData<List<T>>() {

    override fun add(element: T): Boolean =
        element.actionAndUpdate { list.add(it) }

    override fun add(index: Int, element: T) =
        list.add(index, element).also { updateValue() }

    override fun addAll(elements: Collection<T>): Boolean =
        elements.actionAndUpdate { list.addAll(elements) }

    override fun addAll(index: Int, elements: Collection<T>): Boolean =
        elements.actionAndUpdate { list.addAll(index, it) }

    override fun remove(element: T): Boolean =
        element.actionAndUpdate { list.remove(it) }

    override fun removeAt(index: Int): T =
        list.removeAt(index).also { updateValue() }

    override fun removeAll(elements: Collection<T>): Boolean =
        elements.actionAndUpdate { list.removeAll(it) }

    override fun retainAll(elements: Collection<T>): Boolean =
        elements.actionAndUpdate { list.retainAll(it) }

    override fun clear() =
        list.clear().also { updateValue() }

    override fun set(index: Int, element: T): T =
        list.set(index, element).also { updateValue() }

    private fun <T> T.actionAndUpdate(action: (item: T) -> Boolean): Boolean =
        action(this).applyIfTrue { updateValue() }

    private fun Boolean.applyIfTrue(action: () -> Unit): Boolean {
        takeIf { it }?.run { action() }
        return this
    }

    private fun updateValue() {
        value = list
    }
}