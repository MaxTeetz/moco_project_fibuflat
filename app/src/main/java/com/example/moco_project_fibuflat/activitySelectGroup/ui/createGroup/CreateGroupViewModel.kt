package com.example.moco_project_fibuflat.activitySelectGroup.ui.createGroup

import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.User
import com.google.firebase.database.DatabaseReference
import java.util.*

class CreateGroupViewModel : ViewModel() {

    private lateinit var databaseUser: DatabaseReference
    private lateinit var databaseGroup: DatabaseReference
    private lateinit var user: User
    fun checkIfFilled(name: String): Boolean {
        return name.isBlank()
    }

    fun createGroup(
        groupName: String,
        user: User,
        databaseUser: DatabaseReference,
        databaseGroup: DatabaseReference,
    ) {
        this.databaseUser = databaseUser
        this.databaseGroup = databaseGroup
        this.user = user

        val group = Group(UUID.randomUUID().toString(), groupName)

        databaseUser.child(user.userID!!).child("group").setValue(group).addOnSuccessListener {
            setGroupValue(group)
        }
    }

    private fun setGroupValue(group: Group) {
        databaseGroup.child(group.groupId!!).setValue(group)
        databaseGroup.child(group.groupId).child("users").child(user.userID!!)
            .setValue(User(user.userID, user.username))
    }
}