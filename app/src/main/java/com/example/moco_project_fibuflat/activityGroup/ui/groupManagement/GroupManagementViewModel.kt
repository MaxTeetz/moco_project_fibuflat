package com.example.moco_project_fibuflat.activityGroup.ui.groupManagement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.ListCase
import com.example.moco_project_fibuflat.data.OpenRequestGroup
import com.example.moco_project_fibuflat.data.User
import com.example.moco_project_fibuflat.helperClasses.GetSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST") //always clear, so its ok to suppress it
class GroupManagementViewModel : ViewModel() {

    private var requestList: ArrayList<OpenRequestGroup> = arrayListOf()
    private var requestListOld: ArrayList<OpenRequestGroup> = arrayListOf()
    private var memberList: ArrayList<User> = arrayListOf()
    private var memberListOld: ArrayList<User> = arrayListOf()

    private lateinit var databaseGroup: DatabaseReference
    private lateinit var databaseUser: DatabaseReference
    private lateinit var group: Group
    private lateinit var user: User
    private lateinit var valueEventListenerRequest: ValueEventListener
    private lateinit var valueEventListenerMember: ValueEventListener

    private lateinit var databaseRequestRef: Query
    private lateinit var databaseMemberRef: Query

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

    fun acceptUser(openRequestGroup: OpenRequestGroup) { //ToDo make functions with network call suspending

        //could delete all requests belonging to the user in user and groups
        //but just display a message that hes already in a group
        //this way send requests stay active and group members decide if they want to delete it
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

    fun declineUser(openRequestGroup: OpenRequestGroup) {
        databaseUser.child(openRequestGroup.userID!!).child("openRequestsToGroups")
            .child(openRequestGroup.requestID!!).removeValue()
        databaseGroup.child(this.group.groupId!!).child("openRequestsByUsers")
            .child(openRequestGroup.requestID!!).removeValue()
    }

    fun removeListeners() {
        databaseRequestRef.removeEventListener(valueEventListenerRequest)
        databaseMemberRef.removeEventListener(valueEventListenerMember)
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

    private suspend fun fetchDataRequest() {

        withContext(Dispatchers.IO) {
            valueEventListenerRequest =
                GetSnapshot(requestList, requestListOld, listCaseRequest, OpenRequestGroup())
                { index, listCase, entryList -> setListRequests(index!!, listCase!!, entryList) }

            databaseRequestRef.addValueEventListener(valueEventListenerRequest)
        }
    }

    private suspend fun fetchDataMember() {

        withContext(Dispatchers.IO) {
            valueEventListenerMember =
                GetSnapshot(memberList, memberListOld, listCaseMember, User())
                { index, listCase, entryList -> setListMembers(index!!, listCase!!, entryList) }

            databaseMemberRef.addValueEventListener(valueEventListenerMember)
        }
    }

    private fun setListRequests(
        index: Int,
        listCase: ListCase,
        arrayList: ArrayList<OpenRequestGroup>,
    ) {
        _indexRequest = index
        _listCaseRequest = listCase
        _requestListNew.value = arrayList

        setRequestListOld()
    }

    private fun setListMembers(
        index: Int,
        listCase: ListCase,
        arrayList: ArrayList<User>,
    ) {
        _indexMember = index
        _listCaseMembers = listCase
        _memberListNew.value = arrayList

        setMembersListOld()
    }

    private fun setRequestListOld() {
        requestListOld.clear()
        for (ds in requestList)
            requestListOld.add(ds)
    }

    private fun setMembersListOld() {
        memberListOld.clear()
        for (ds in memberList)
            memberListOld.add(ds)
    }
}