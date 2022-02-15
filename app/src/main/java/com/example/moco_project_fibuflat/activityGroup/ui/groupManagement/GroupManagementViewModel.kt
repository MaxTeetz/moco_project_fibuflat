package com.example.moco_project_fibuflat.activityGroup.ui.groupManagement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.*
import com.google.firebase.database.*
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

    private var _listCaseRequest: ListCase? = ListCase.EMPTY
    val listCaseRequest get() = _listCaseRequest

    private var _listCaseMembers: ListCase? = ListCase.EMPTY
    val listCaseMember get() = _listCaseMembers

    private var _indexRequest: Int? = 0
    val indexRequest get() = _indexRequest

    private var _indexMember: Int? = 0
    val indexMember get() = _indexMember

    suspend fun getRequests(adapterCase: AdapterCase) {
        databaseRequestRef = databaseGroup.child(group.groupId!!).child("openRequestsByUsers")
            .orderByChild("OpenRequestGroup")
        fetchDataRequest(adapterCase)
    }

    suspend fun getGroupMembers(adapterCase: AdapterCase) {
        databaseMemberRef = databaseGroup.child(group.groupId!!).child("users").orderByChild("User")
        fetchDataMember(adapterCase)
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

    private suspend fun fetchDataRequest(adapterCase: AdapterCase) {

        withContext(Dispatchers.IO) {
            valueEventListenerRequest = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (requestList.isNotEmpty())
                        requestList.clear()
                    if (snapshot.exists()) {
                        for (requestSnapshot in snapshot.children) {
                            val request = requestSnapshot.getValue(OpenRequestGroup::class.java)
                            requestList.add(request!!)
                        }
                    }
                    recyclerViewNewArranged(requestListOld, requestList, adapterCase)
                    setRequestListOld()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("adapter", "cancelled")
                }
            }
        }
        databaseRequestRef.addValueEventListener(valueEventListenerRequest)
    }

    private suspend fun fetchDataMember(adapterCase: AdapterCase) {

        withContext(Dispatchers.IO) {
            valueEventListenerMember = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (memberList.isNotEmpty())
                        memberList.clear()

                    if (snapshot.exists()) {
                        for (requestSnapshot in snapshot.children) {
                            val user = requestSnapshot.getValue(User::class.java)
                            memberList.add(user!!)
                        }
                    }
                    recyclerViewNewArranged(memberListOld, memberList, adapterCase)
                    setMembersListOld()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("adapter", "cancelled")
                }
            }
        }
        databaseMemberRef.addValueEventListener(valueEventListenerMember)
    }

    private fun recyclerViewNewArranged(
        arrayListOld: ArrayList<*>,
        arrayList: ArrayList<*>,
        adapterCase: AdapterCase,
    ) {
        Log.d("viewModelGroupManagement", "$adapterCase")
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

        if (adapterCase == AdapterCase.Request)
            setListRequests(index, case!!, arrayList as ArrayList<OpenRequestGroup>)
        else
            setListMembers(index, case!!, arrayList as ArrayList<User>)

        //ToDo if empty and item gets inserted, only shows after second one gets inserted too. No problem if fragment loads with only one item in list
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

    private fun setListRequests(
        index: Int,
        listCase: ListCase,
        arrayList: ArrayList<OpenRequestGroup>,
    ) {
        Log.d("viewModelGroupManagementRequest", "$arrayList")
        Log.d("viewModelGroupManagementRequest", "$listCase")
        Log.d("viewModelGroupManagementRequest", "$index")
        _indexRequest = index
        _listCaseRequest = listCase
        _requestListNew.value = arrayList
    }

    private fun setListMembers(
        index: Int,
        listCase: ListCase,
        arrayList: ArrayList<User>,
    ) {
        Log.d("viewModelGroupManagementMember", "$arrayList")
        Log.d("viewModelGroupManagementMember", "$listCase")
        Log.d("viewModelGroupManagementMember", "$index")
        _indexMember = index
        _listCaseMembers = listCase
        _memberListNew.value = arrayList
    }


    private fun setRequestListOld() {
        Log.d("adapterViewModelSetOldList", "")
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