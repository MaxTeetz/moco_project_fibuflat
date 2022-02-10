package com.example.moco_project_fibuflat.activitySelectGroup.ui.joinGroup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.OpenRequestGroup
import com.example.moco_project_fibuflat.data.OpenRequestUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class JoinGroupViewModel : ViewModel() {
    private lateinit var username: String
    private lateinit var database: DatabaseReference

    fun joinGroup(groupName: String, id: String) {
        database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Groups")
        val groupsNamesRef = database.orderByChild("groupName").equalTo(groupName)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    if (ds.child("groupID").getValue(String::class.java)?.substring(0, 4) == id) {
                        val groupID = ds.child("groupID").getValue(String::class.java)
                        sendRequest(groupID!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("joinGroup", "Group not found") //ToDo ask Prof
            }
        }
        groupsNamesRef.addListenerForSingleValueEvent(valueEventListener)
    }

    private fun sendRequest(groupID: String) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val requestID: String = UUID.randomUUID().toString()

        val openRequestGroup = OpenRequestGroup(UUID.randomUUID().toString(), "Max Mustermann")
        //set Group Request
        database.child(groupID).child("openRequests").child(requestID).setValue(openRequestGroup) //ToDo get Username !!!

        //ToDo make sure, to only send one request
        //set User Request
        val openRequestUser = OpenRequestUser(groupID, requestID)
        database = //works
            database.parent!!.child("Users").child(uid).child("openRequests").child(requestID)
        database.setValue(openRequestUser)
    //same as in Group. To delete later in Group, if user joined other Group -> works
    }

}