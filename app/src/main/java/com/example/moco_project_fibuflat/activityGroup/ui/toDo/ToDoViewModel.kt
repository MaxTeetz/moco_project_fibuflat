package com.example.moco_project_fibuflat.activityGroup.ui.toDo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.ListCase
import com.example.moco_project_fibuflat.data.ToDoEntry
import com.example.moco_project_fibuflat.data.User
import com.example.moco_project_fibuflat.helperClasses.GetSnapshotRecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToDoViewModel : ViewModel() {
    private lateinit var databaseGroup: DatabaseReference
    private lateinit var databaseUser: DatabaseReference
    private lateinit var group: Group
    private lateinit var user: User
    private lateinit var valueEventListenerEntry: ValueEventListener

    private lateinit var databaseToDoRef: Query

    private var entryList: ArrayList<ToDoEntry> = arrayListOf()
    private var entryListOld: ArrayList<ToDoEntry> = arrayListOf()

    private val _allToDoEntries: MutableLiveData<ArrayList<ToDoEntry>> = MutableLiveData()
    val allToDoEntries: LiveData<ArrayList<ToDoEntry>> get() = _allToDoEntries

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
        databaseToDoRef.removeEventListener(valueEventListenerEntry)
    }

    suspend fun getEntries() {
        databaseToDoRef =
            databaseGroup.child(group.groupId!!).child("todoEntries").orderByChild("id")
        fetchDataToDo()
    }

    private suspend fun fetchDataToDo() {
        withContext(Dispatchers.IO) {
            valueEventListenerEntry =
                GetSnapshotRecyclerView(entryList, entryListOld, listCase, ToDoEntry())
                { index, listCase, entryList, entryListOld ->
                    setListToDo(index!!,
                        listCase!!,
                        entryList,
                        entryListOld)
                }

            databaseToDoRef.addValueEventListener(valueEventListenerEntry)
        }
    }

    private fun setListToDo(
        index: Int,
        listCase: ListCase,
        entryList: java.util.ArrayList<ToDoEntry>,
        entryListOld: java.util.ArrayList<ToDoEntry>,
    ) {
        this._index = index
        this._listCase = listCase
        this._allToDoEntries.value = entryList
        this.entryListOld = entryListOld
    }
}