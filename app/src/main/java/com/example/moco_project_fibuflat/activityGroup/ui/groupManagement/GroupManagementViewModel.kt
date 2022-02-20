package com.example.moco_project_fibuflat.activityGroup.ui.groupManagement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.ListCase
import com.example.moco_project_fibuflat.data.OpenRequestGroup
import com.example.moco_project_fibuflat.data.User
import com.example.moco_project_fibuflat.helperClasses.GetSnapshotRecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GroupManagementViewModel : ViewModel() {

    private lateinit var databaseGroup: DatabaseReference
    private lateinit var databaseUser: DatabaseReference
    private lateinit var group: Group
    private lateinit var user: User
    private lateinit var valueEventListenerRequest: ValueEventListener
    private lateinit var valueEventListenerMember: ValueEventListener

    private lateinit var databaseRequestRef: Query
    private lateinit var databaseMemberRef: Query

    private var requestList: ArrayList<OpenRequestGroup> = arrayListOf()
    private var requestListOld: ArrayList<OpenRequestGroup> = arrayListOf()
    private var memberList: ArrayList<User> = arrayListOf()
    private var memberListOld: ArrayList<User> = arrayListOf()

    private var _requestListNew: MutableLiveData<ArrayList<OpenRequestGroup>> = MutableLiveData()
    val requestListNew: LiveData<ArrayList<OpenRequestGroup>> get() = _requestListNew

    private var _memberListNew: MutableLiveData<ArrayList<User>> = MutableLiveData()
    val memberListNew: LiveData<ArrayList<User>> get() = _memberListNew

    private var _toast: MutableLiveData<String> = MutableLiveData()
    val toast: LiveData<String> get() = _toast

    private var _listCaseRequest: ListCase? = null
    val listCaseRequest get() = _listCaseRequest

    private var _listCaseMembers: ListCase? = null
    val listCaseMember get() = _listCaseMembers

    private var _indexRequest: Int? = 0
    val indexRequest get() = _indexRequest

    private var _indexMember: Int? = 0
    val indexMember get() = _indexMember

    suspend fun getRequests() {
        databaseRequestRef = databaseGroup.child(group.groupId!!).child("openRequestsByUsers")
            .orderByChild("OpenRequestGroup")
        fetchDataRequest()
    }

    suspend fun getGroupMembers() {
        databaseMemberRef = databaseGroup.child(group.groupId!!).child("users").orderByChild("User")
        fetchDataMember()
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

    suspend fun acceptUser(openRequestGroup: OpenRequestGroup) {
        //could delete all requests belonging to the user in user and groups
        //but just display a message that hes already in a group
        //this way send requests stay active and group members decide if they want to delete it

        val group: Group? =
            databaseUser.child(openRequestGroup.userID.toString()).child("group").get().await()
            .getValue(Group::class.java)
        Log.d("groupManagementViewModel1", "$group")
        if (group != null)
            _toast.value = openRequestGroup.username!!
        else
            letUserJoin(openRequestGroup)
    }

    suspend fun declineUser(openRequestGroup: OpenRequestGroup) {
        withContext(Dispatchers.IO) {
            Log.d("groupManagementViewModel3", "")
            databaseUser.child(openRequestGroup.userID!!).child("openRequestsToGroups")
                .child(openRequestGroup.requestID!!).removeValue()
            databaseGroup.child(group.groupId!!).child("openRequestsByUsers")
                .child(openRequestGroup.requestID!!).removeValue()
            Log.d("groupManagementViewModel4", "")
        }
    }

    fun removeListeners() {
        databaseRequestRef.removeEventListener(valueEventListenerRequest)
        databaseMemberRef.removeEventListener(valueEventListenerMember)
    }

    private suspend fun letUserJoin(openRequestGroup: OpenRequestGroup) {
        withContext(Dispatchers.IO) {
            val user = User(openRequestGroup.userID, openRequestGroup.username)

            Log.d("groupManagementViewModel2", "")
            databaseUser.child(user.userID.toString()).child("group")
                .setValue(group)
            databaseGroup.child(group.groupId.toString()).child("users")
                .child(user.userID.toString()).setValue(user)
            declineUser(openRequestGroup)
            Log.d("groupManagementViewModel5", "User successfully joined Group")
        }
    }

    private suspend fun fetchDataRequest() {
        //ToDoEntry cleaner if the assignment is outside the coroutine?
        withContext(Dispatchers.IO) {
            valueEventListenerRequest =
                GetSnapshotRecyclerView(
                    requestList,
                    requestListOld,
                    listCaseRequest,
                    OpenRequestGroup())
                { index, listCase, entryList, entryListOld -> setListRequests(index!!, listCase!!, entryList, entryListOld) }

            databaseRequestRef.addValueEventListener(valueEventListenerRequest)
        }
    }

    private suspend fun fetchDataMember() {
        withContext(Dispatchers.IO) {
            valueEventListenerMember =
                GetSnapshotRecyclerView(memberList, memberListOld, listCaseMember, User())
                { index, listCase, entryList, entryListOld -> setListMembers(index!!, listCase!!, entryList, entryListOld) }

            databaseMemberRef.addValueEventListener(valueEventListenerMember)
        }
    }

    private fun setListRequests(
        index: Int,
        listCase: ListCase,
        arrayList: ArrayList<OpenRequestGroup>,
        entryListOld: ArrayList<OpenRequestGroup>,
    ) {
        this._indexRequest = index
        this._listCaseRequest = listCase
        this._requestListNew.value = arrayList
        this.requestListOld = entryListOld
    }

    private fun setListMembers(
        index: Int,
        listCase: ListCase,
        arrayList: ArrayList<User>,
        entryListOld: ArrayList<User>,
    ) {
        this._indexMember = index
        this._listCaseMembers = listCase
        this._memberListNew.value = arrayList
        this.memberListOld = entryListOld
    }
}