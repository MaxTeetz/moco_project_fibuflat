package com.example.moco_project_fibuflat.activityGroup.ui.groupManagement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.ListCases
import com.example.moco_project_fibuflat.data.OpenRequestGroup
import com.example.moco_project_fibuflat.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class GroupManagementViewModel : ViewModel() {
    private lateinit var requestList: ArrayList<OpenRequestGroup>
    private lateinit var requestListOld: ArrayList<OpenRequestGroup>
    private lateinit var databaseGroup: DatabaseReference
    private lateinit var databaseUser: DatabaseReference
    private lateinit var group: Group
    private lateinit var user: User

    private var _requestListNew: MutableLiveData<ArrayList<OpenRequestGroup>> = MutableLiveData()
    val requestListNew: LiveData<ArrayList<OpenRequestGroup>> get() = _requestListNew

    private var _listCases: ListCases? = ListCases.EMPTY
    val listCases get() = _listCases

    private var _index: Int? = null
    val index get() = _index

    fun getUserData(database: DatabaseReference, group: Group) {
        requestList = arrayListOf()
        requestListOld = arrayListOf()

        this.databaseGroup =
            database //ToDo ask Prof -> better in viewModel and given to function or define in here

        val groupNameRef =
            database.child(this.group.groupId!!).child("openRequestsByUsers")
                .orderByChild("OpenRequestGroup")

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (requestList.isNotEmpty())
                    requestList.clear()

                if (snapshot.exists()) {
                    for (requestSnapshot in snapshot.children) {
                        val request = requestSnapshot.getValue(OpenRequestGroup::class.java)
                        requestList.add(request!!)
                    }
                }
                //_requestListNew.value = requestList
                recyclerViewNewArranged(requestListOld, requestList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("adapter", "cancelled")
            }
        }
        groupNameRef.addValueEventListener(valueEventListener)
    }

    private fun recyclerViewNewArranged(
        arrayListOld: ArrayList<OpenRequestGroup>,
        arrayList: ArrayList<OpenRequestGroup>,
    ) { //ok gets whole new list, I guess

        Log.d("adapter1", arrayListOld.size.toString())
        Log.d("adapter2", arrayList.size.toString())

        if (arrayListOld.isEmpty()) {
            Log.d("adapter", "empty")
            _listCases = ListCases.EMPTY
        }

        if (arrayListOld.size > arrayList.size) { //Item deleted
            Log.d("adapter", "deleted")

            _listCases = ListCases.DELETED

            for ((i, openRequestGroup: OpenRequestGroup) in arrayListOld.withIndex()) { //item somewhere before the lists end
                if (i == arrayListOld.size) {
                    _index = i //here +1 if it need the old list
                    break
                }
                if (requestList[i] != openRequestGroup) {
                    _index = i
                    break
                }
            }
        }
        Log.d("adapter", _index.toString())
        if ((arrayListOld.size < arrayList.size) && arrayListOld.isNotEmpty()) { //Item added
            Log.d("adapter", "added")
            _listCases = ListCases.ADDED
        }

        setListOldItems()
        _requestListNew.value = arrayList
    }

    private fun setListOldItems() {
        requestListOld.clear()
        for (ds in requestList)
            requestListOld.add(ds)
    }

    fun acceptUser(openRequestGroup: OpenRequestGroup) {
    }

    fun declineUser(openRequestGroup: OpenRequestGroup) {
        databaseUser.child(openRequestGroup.userID!!).child("openRequestsToGroups")
            .child(openRequestGroup.requestID!!).removeValue()
        databaseGroup.child(this.group.groupId!!).child("openRequestsByUsers")
            .child(openRequestGroup.requestID!!).removeValue()
    }
}