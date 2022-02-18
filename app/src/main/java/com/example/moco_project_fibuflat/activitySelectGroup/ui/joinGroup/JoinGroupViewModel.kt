package com.example.moco_project_fibuflat.activitySelectGroup.ui.joinGroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.OpenRequestGroup
import com.example.moco_project_fibuflat.data.OpenRequestUser
import com.example.moco_project_fibuflat.data.User
import com.example.moco_project_fibuflat.helperClasses.GetSnapshotSingleEvent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class JoinGroupViewModel : ViewModel() {
    private lateinit var databaseUser: DatabaseReference
    private lateinit var databaseGroup: DatabaseReference
    private lateinit var user: User
    private lateinit var groupName: String
    private lateinit var groupId: String

    private var _success: MutableLiveData<String> = MutableLiveData()
    val success: LiveData<String> get() = _success

    suspend fun joinGroup(
        groupName: String,
        groupId: String,
        user: User,
        databaseReferenceUser: DatabaseReference,
        databaseReferenceGroup: DatabaseReference,
    ) {
        this.groupName = groupName
        this.groupId = groupId
        this.user = user
        this.databaseUser = databaseReferenceUser
        this.databaseGroup = databaseReferenceGroup

        val userRef =
            databaseUser.child(user.userID!!).child("openRequestsToGroups")
                .orderByChild("groupName")
                .equalTo(this.groupName)

        withContext(Dispatchers.IO) { //ToDo ask prof if suspend or anything is needed for every function
            GetSnapshotSingleEvent(userRef) { snapshot -> checkAlreadySent(snapshot) }
        }
    }

    private fun checkAlreadySent(snapshot: DataSnapshot) {
        val groupsNamesRef = databaseGroup.orderByChild("groupName").equalTo(groupName)

        if (snapshot.exists()) {
            for (requests in snapshot.children) {
                if (requests.child("groupID").getValue(String::class.java)
                        ?.substring(0, 4) == groupId
                ) {
                    return
                }
            }
        }
        GetSnapshotSingleEvent(groupsNamesRef) { snapshotReturn -> getGroup(snapshotReturn) }
    }

    private fun getGroup(snapshot: DataSnapshot) {
        for (ds in snapshot.children) {
            if (ds.child("groupId").getValue(String::class.java)
                    ?.substring(0, 4) == groupId
            ) {
                setDatabase(
                    Group(
                        ds.child("groupId").getValue(String::class.java)!!,
                        ds.child("groupName")
                            .getValue(String::class.java))) //don't need the whole snapshot
                break
            }
        }
    }

    private fun setDatabase(group: Group) {
        val requestID: String = UUID.randomUUID().toString()
        val openRequestGroup =
            OpenRequestGroup(requestID, user.userID, user.username)
        val openRequestUser = OpenRequestUser(requestID, group.groupId, group.groupName)

        //set Group Request
        databaseGroup.child(group.groupId!!).child("openRequestsByUsers")
            .child(requestID).setValue(openRequestGroup)

        //set User Request
        databaseUser.child(user.userID!!).child("openRequestsToGroups")
            .child(requestID).setValue(openRequestUser)

        _success.value = "Request to group ${group.groupName} successful send!"
    }
}