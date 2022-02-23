package com.example.moco_project_fibuflat.activityGroup.ui.toDo

import android.util.Log
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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class ToDoViewModel : ViewModel() {
    private lateinit var databaseGroup: DatabaseReference
    private lateinit var databaseUser: DatabaseReference
    private lateinit var group: Group
    private lateinit var user: User
    private lateinit var valueEventListenerEntry: ValueEventListener

    private lateinit var databaseToDoRef: Query
    private val storageReference = FirebaseStorage.getInstance().reference

    private var entryList: ArrayList<ToDoEntry> = arrayListOf()
    private var entryListOld: ArrayList<ToDoEntry> = arrayListOf()

    private var _allToDoEntries: MutableList<ToDoEntry> = arrayListOf()
    val allToDoEntries: MutableList<ToDoEntry> get() = _allToDoEntries

    private var _listCase: MutableLiveData<ListCase?> = MutableLiveData()
    val listCase: LiveData<ListCase?> get() = _listCase

    private var _index: Int? = 0
    val index get() = _index

    private var _indexChanged: Int? = 0
    val indexChanged get() = _indexChanged

    private var _toastMessage: MutableLiveData<String?> = MutableLiveData()
    val toastMessage: LiveData<String?> get() = _toastMessage

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

    suspend fun getEntries() {
        databaseToDoRef =
            databaseGroup.child(group.groupId!!).child("todoEntries").orderByChild("id")
        fetchDataToDo()
    }

    private suspend fun fetchDataToDo() {
        withContext(Dispatchers.IO) {
            valueEventListenerEntry =
                GetSnapshotRecyclerView(entryList, entryListOld, listCase.value, ToDoEntry())
                { index, listCase, entryList, entryListOld ->
                    Log.d("viewModelToDOFetchData", "$index")
                    setListToDo(
                        index!!,
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
        Log.d("todoEntryList", "${this.entryList.size}")
        this._index = index
        if (listCase == ListCase.EMPTY) {
            Log.d("todoEntryListEmpty", "$index")
            for (entry in entryList) {
                _allToDoEntries.add(entry.copy())
            }
            this._listCase.value = listCase
            getImage()
        }

        if (listCase == ListCase.DELETED) {
            Log.d("todoEntryListDeleted", "$index")
            _allToDoEntries.removeAt(index)
            this._listCase.value = listCase
        }

        if (listCase == ListCase.ADDED) {
            Log.d("todoEntryListAdded", "$index")
            _allToDoEntries.add(index, entryList[index].copy())
            this._listCase.value = listCase
            getImageAdded(index, _allToDoEntries[index])
        }
        this.entryListOld = entryListOld

    }

    override fun onCleared() {
        super.onCleared()
        Log.d("toDoViewModel", "onCleared()")
    }

    private fun getImage() =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                for ((i, entry) in _allToDoEntries.withIndex()) {
                    if (entry.pictureAdded != null && entry.picture == null) {
                        val image =
                            storageReference.child(entry.pictureAdded!!)
                        val url = image.downloadUrl.await()
                        setImage(url.toString(), i)
                    }
                }
            } catch (e: Exception) {
                Log.d("toDoAdapter", "$e")
            }
        }

    private fun getImageAdded(index: Int, toDoEntry: ToDoEntry) =
        CoroutineScope(Dispatchers.IO).launch {
            if (toDoEntry.pictureAdded != null) {
                val image = storageReference.child(toDoEntry.pictureAdded!!)
                val url = image.downloadUrl.await()
                setImage(url.toString(), index)
            }
        }

    private fun setImage(imageUrls: String, index: Int) {
        _allToDoEntries[index].picture = imageUrls
        _indexChanged = index
        _listCase.postValue(ListCase.CHANGED)
    }

    suspend fun deleteItem(toDoEntry: ToDoEntry) {
        withContext(Dispatchers.IO) {
            databaseGroup.child(group.groupId!!).child("todoEntries").child(toDoEntry.id!!)
                .removeValue()

            if (toDoEntry.pictureAdded != null)
                storageReference.child(toDoEntry.pictureAdded!!).delete().addOnSuccessListener {
                    _toastMessage.postValue("Delete successful")
                }.addOnFailureListener {
                    _toastMessage.postValue("Delete not successful")
                }
        }
    }

    fun reset() {
        _listCase.value = ListCase.EMPTY
    }
}