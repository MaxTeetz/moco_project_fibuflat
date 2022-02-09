package com.example.moco_project_fibuflat.activitySelectGroup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private lateinit var database: DatabaseReference

class SelectGroupActivityViewModel : ViewModel() {

    fun createGroup(groupID: String, groupName: String, groupAdminID: String) {
        val mutableList = mutableListOf(groupAdminID)
        val group = Group(groupID, groupName, mutableList)
        database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Groups")
        database.child(groupID).setValue(group).addOnSuccessListener {
            Log.d("selectGroupActivity", "createdDBInstance")
        }.addOnFailureListener {
            Log.d("selectGroupActivity", "couldn't create db entry")
        }
    }
}