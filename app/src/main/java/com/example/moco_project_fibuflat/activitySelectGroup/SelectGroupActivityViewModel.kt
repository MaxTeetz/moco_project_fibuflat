package com.example.moco_project_fibuflat.activitySelectGroup

import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private lateinit var database: DatabaseReference

class SelectGroupActivityViewModel : ViewModel() {

    private var name: String? = null
    fun createGroup(groupID: String, groupName: String, groupAdminID: String) {

        database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")
        database.child(groupAdminID).child("username").get().addOnSuccessListener {
            this.name = it.value.toString()
        }
        val group = Group(groupID, groupName)
        database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Groups")
        database.child(groupID).setValue(group)
        database.child(groupID).child("users").child(groupAdminID).setValue("this.name") //ToDo get name !!!
    }
}