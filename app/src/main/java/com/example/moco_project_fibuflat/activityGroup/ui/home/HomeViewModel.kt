package com.example.moco_project_fibuflat.activityGroup.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.*
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private var entryList: ArrayList<MoneyPoolEntry> = arrayListOf()
    private var entryListOld: ArrayList<MoneyPoolEntry> = arrayListOf()

    private lateinit var databaseGroup: DatabaseReference
    private lateinit var databaseUser: DatabaseReference
    private lateinit var group: Group
    private lateinit var user: User
    private lateinit var valueEventListenerEntry: ValueEventListener

    private lateinit var databaseEntryRef: Query

    private val _allMoneyEntries: MutableLiveData<ArrayList<MoneyPoolEntry>> = MutableLiveData()
    val allMoneyEntries: LiveData<ArrayList<MoneyPoolEntry>> get() = _allMoneyEntries

    private var _listCase: ListCase? = ListCase.EMPTY
    val listCase get() = _listCase

    private var _index: Int? = 0
    val index get() = _index

    private val _moneyGoal = MutableLiveData(MoneyGoal(0.0, 0.0))
    val moneyGoal: LiveData<MoneyGoal> get() = _moneyGoal

    fun setDataViewModel(
        dataBaseUsers: DatabaseReference,
        dataBaseGroups: DatabaseReference,
        group: LiveData<Group>,
        user: LiveData<User>,
    ) {
        this.databaseUser = dataBaseUsers
        this.databaseGroup = dataBaseGroups
        this.group = group.value!!
        this.user = user.value!!
    }

    fun removeListeners(){
        databaseEntryRef.removeEventListener(valueEventListenerEntry)
    }

    suspend fun getEntries() {
        databaseEntryRef = databaseGroup.child(group.groupId!!).child("moneyPoolEntries")
            .orderByChild("stringDate")
        fetchDataEntry()
    }

    private suspend fun fetchDataEntry() {

        withContext(Dispatchers.IO) {
            valueEventListenerEntry = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (entryList.isNotEmpty())
                        entryList.clear()
                    if (snapshot.exists()) {
                        for (entrySnapshot in snapshot.children) {
                            Log.d("homeFragment", entrySnapshot.toString())
                            val moneyPoolEntry = entrySnapshot.getValue(MoneyPoolEntry::class.java)
                            entryList.add(moneyPoolEntry!!)
                        }
                    }
                    recyclerViewNewArranged(entryListOld, entryList)
                    setEntryListOld()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("adapter", "cancelled")
                }
            }
        }
        databaseEntryRef.addValueEventListener(valueEventListenerEntry)
    }

    private fun recyclerViewNewArranged(
        arrayListOld: ArrayList<*>,
        arrayList: ArrayList<*>,
    ) {
        var index = 0
        var case: ListCase? = null

        if (arrayListOld.isEmpty()) {
            case = ListCase.EMPTY
        }

        if (arrayListOld.size > arrayList.size) { //Item deleted
            case = ListCase.DELETED
            index = deleted(arrayListOld, arrayList)
        }
        if ((arrayListOld.size < arrayList.size) && arrayListOld.isNotEmpty()) { //Item added
            case = ListCase.ADDED
            index = added(arrayListOld, arrayList)
        }

        Log.d("homeViewModelOld", "$arrayListOld")
        Log.d("homeViewModelNew", "$arrayList")
        Log.d("homeViewModelCase", "$case")
        Log.d("homeViewModelIndex", "$index")

        @Suppress("UNCHECKED_CAST")
        setListEntry(index, case!!, arrayList as ArrayList<MoneyPoolEntry>)
    }

    private fun added(
        arrayListOld: ArrayList<*>,
        arrayList: ArrayList<*>,
    ): Int {
        for ((i, any: Any) in arrayList.withIndex()) {

            if (i == arrayListOld.size) { //item somewhere in between
                return i
            }
            if (arrayListOld[i] != any) { //item at lists end
                return i
            }
        }
        return 0
    }

    private fun deleted(
        arrayListOld: ArrayList<*>,
        arrayList: ArrayList<*>,
    ): Int {
        for ((i, any: Any) in arrayListOld.withIndex()) {

            if (i == arrayList.size) {
                return i
            }
            if (arrayList[i] != any) {
                return i
            }
        }
        return 0
    }

    private fun setListEntry(
        index: Int,
        listCase: ListCase,
        arrayList: ArrayList<MoneyPoolEntry>,
    ) {
        Log.d("homeViewModel2", "$arrayList")
        Log.d("homeViewModel2", "$listCase")
        Log.d("homeViewModel2", "$index")
        _index = index
        _listCase = listCase
        _allMoneyEntries.value = arrayList
    }

    private fun setEntryListOld() {
        entryListOld.clear()
        for (mpe in entryList)
            entryListOld.add(mpe)
    }
}

class MutableListLiveData<T>(
    private val list: MutableList<T> = mutableListOf(),
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