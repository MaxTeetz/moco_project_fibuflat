package com.example.moco_project_fibuflat.activityGroup.ui.groupInfo

import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.User
import com.example.moco_project_fibuflat.helperClasses.GetSnapshotSingleEvent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GroupInfoViewModel : ViewModel() {
    private lateinit var user: User
    private lateinit var group: Group
    private lateinit var dataBaseUsers: DatabaseReference
    private lateinit var dataBaseGroups: DatabaseReference

    //ToDo ask prof
    suspend fun leaveGroup(
        user: User,
        group: Group,
        dataBaseUsers: DatabaseReference,
        dataBaseGroups: DatabaseReference,
    ) {
        this.user = user
        this.group = group
        this.dataBaseUsers = dataBaseUsers
        this.dataBaseGroups = dataBaseGroups
        deleteInUser()
        deleteInGroup()
    }

    private suspend fun deleteInGroup() {
        withContext(Dispatchers.IO) {
            dataBaseGroups.child(group.groupId!!).child("users").child(user.userID!!).removeValue()

            GetSnapshotSingleEvent(dataBaseGroups.child(group.groupId!!)
                .child("users")) { snapshot -> deleteGroup(snapshot) }
        }
    }

    private suspend fun deleteInUser() {
        withContext(Dispatchers.IO) {
            dataBaseUsers.child(user.userID!!).child("group").removeValue()
        }
    }

    private fun deleteGroup(snapshot: DataSnapshot) {
        if (snapshot.exists())
            return
        else
            dataBaseGroups.child(group.groupId!!).removeValue()
    }
}