package com.example.moco_project_fibuflat.activityGroup.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.ListCase
import com.example.moco_project_fibuflat.data.MoneyPoolEntry
import com.example.moco_project_fibuflat.data.User
import com.example.moco_project_fibuflat.helperClasses.GetSnapshotRecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
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

    private var _listCase: ListCase? = null
    val listCase get() = _listCase

    private var _index: Int? = 0
    val index get() = _index

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

    fun removeListeners() {
        databaseEntryRef.removeEventListener(valueEventListenerEntry)
    }

    suspend fun getEntries() {
        databaseEntryRef = databaseGroup.child(group.groupId!!).child("moneyPoolEntries")
            .orderByChild("stringDate")
        fetchDataEntry()
    }

    private suspend fun fetchDataEntry() {
        withContext(Dispatchers.IO) {
            valueEventListenerEntry = GetSnapshotRecyclerView(entryList, entryListOld, listCase, MoneyPoolEntry())
            {index, listCase, entryList -> setListEntry(index!!, listCase!!, entryList) }

            databaseEntryRef.addValueEventListener(valueEventListenerEntry)
        }
    }

    private fun setListEntry(
        index: Int,
        listCase: ListCase,
        arrayList: ArrayList<MoneyPoolEntry>,
    ) {
        _index = index
        _listCase = listCase
        _allMoneyEntries.value = arrayList

        setEntryListOld()
    }

    private fun setEntryListOld() {
        entryListOld.clear()
        Log.d("homeViewModel", "setEntryListOld")
        for (mpe in entryList) {
            entryListOld.add(mpe)
        }
    }
}