package com.example.moco_project_fibuflat.activityGroup.ui.groupManagement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.ListCases
import com.example.moco_project_fibuflat.data.OpenRequestGroup
import com.example.moco_project_fibuflat.data.User
import com.google.firebase.database.*
import kotlinx.coroutines.SupervisorJob

class GroupManagementViewModel : ViewModel() {
    private lateinit var requestList: ArrayList<OpenRequestGroup>
    private lateinit var requestListOld: ArrayList<OpenRequestGroup>
    private lateinit var databaseGroup: DatabaseReference //ToDo define earlier to requests
    private lateinit var databaseUser: DatabaseReference
    private lateinit var group: Group
    private lateinit var user: User
    private lateinit var groupNameRef: Query
    private lateinit var valueEventListener: ValueEventListener
    val job = SupervisorJob()

    private var _requestListNew: MutableLiveData<ArrayList<OpenRequestGroup>> = MutableLiveData()
    val requestListNew: LiveData<ArrayList<OpenRequestGroup>> get() = _requestListNew

    private var _toast: MutableLiveData<String> = MutableLiveData()
    val toast: LiveData<String> get() = _toast

    private var _listCases: ListCases? = ListCases.EMPTY
    val listCases get() = _listCases

    private var _index: Int? = 0
    val index get() = _index

    fun getUserData() {
        requestList = arrayListOf()
        requestListOld = arrayListOf()
        //ToDo ask Prof -> better in viewModel and given to function or define in here

        groupNameRef =
            databaseGroup.child(group.groupId!!).child("openRequestsByUsers")
                .orderByChild("OpenRequestGroup")

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (requestList.isNotEmpty())
                    requestList.clear()
                Log.d("adapterViewModel", "listener")
                if (snapshot.exists()) {
                    for (requestSnapshot in snapshot.children) {
                        val request = requestSnapshot.getValue(OpenRequestGroup::class.java)
                        requestList.add(request!!)
                    }
                }
                recyclerViewNewArranged(requestListOld, requestList)
                setListOldItems()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("adapter", "cancelled")
            }
        }
        groupNameRef.addValueEventListener(valueEventListener)
    }

    //could delete all requests belonging to the user in user and groups
    //but just display a message that hes already in a group
    //this way send requests stay active and group members decide if they want to delete it
    fun acceptUser(openRequestGroup: OpenRequestGroup) { //ToDo make functions with network call suspending

        databaseUser.child(openRequestGroup.userID.toString()).child("group").get()
            .addOnSuccessListener {

                if (it.getValue(Group::class.java)?.groupId != null)
                //Make toast here
                    _toast.value = openRequestGroup.username!!
                else
                    letUserJoin(openRequestGroup)
            }.addOnFailureListener { e: Exception ->
                Log.d("groupManagementViewModel", "$e")
            }

    }

    fun removeListener() {
        groupNameRef.removeEventListener(valueEventListener)
    }

    private fun letUserJoin(openRequestGroup: OpenRequestGroup) {

        val user = User(openRequestGroup.userID, openRequestGroup.username)

        databaseUser.child(user.userID.toString()).child("group")
            .setValue(group)
        databaseGroup.child(group.groupId.toString()).child("users")
            .child(user.userID.toString()).setValue(user)
        declineUser(openRequestGroup)
        Log.d("groupManagementViewModel", "User successfully joined Group")
    }

    fun declineUser(openRequestGroup: OpenRequestGroup) {
        databaseUser.child(openRequestGroup.userID!!).child("openRequestsToGroups")
            .child(openRequestGroup.requestID!!).removeValue()
        databaseGroup.child(this.group.groupId!!).child("openRequestsByUsers")
            .child(openRequestGroup.requestID!!).removeValue()
    }

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

    private fun recyclerViewNewArranged(
        arrayListOld: ArrayList<OpenRequestGroup>,
        arrayList: ArrayList<OpenRequestGroup>,
    ) {
        if (arrayListOld.isEmpty()) {
            _listCases = ListCases.EMPTY
            Log.d("adapter", "EMPTY")
        }

        if (arrayListOld.size > arrayList.size) { //Item deleted
            _listCases = ListCases.DELETED

            for ((i, openRequestGroup: OpenRequestGroup) in arrayListOld.withIndex()) { //item somewhere before the lists end
                if (i == arrayList.size) {
                    _index = i //here +1 if it need the old list
                    break
                }
                if (requestList[i] != openRequestGroup) {
                    _index = i
                    break
                }
            }
            Log.d("adapter", "DELETED")
        }
        if ((arrayListOld.size < arrayList.size) && arrayListOld.isNotEmpty()) { //Item added
            _listCases = ListCases.ADDED
            for ((i, openRequestGroup: OpenRequestGroup) in arrayList.withIndex()) {

                if (i == arrayListOld.size) {
                    _index = i
                    break
                }
                if (arrayListOld[i] != openRequestGroup) {
                    _index = i
                    break
                }
            }
            Log.d("adapter", "ADDED")
        }

        //ToDo if empty and item gets inserted, only shows after second one gets inserted too. No problem if fragment loads with only one item in list
    }

    private fun setListOldItems() {
        requestListOld.clear()
        for (ds in requestList)
            requestListOld.add(ds)
        _requestListNew.value = requestList
    }

}