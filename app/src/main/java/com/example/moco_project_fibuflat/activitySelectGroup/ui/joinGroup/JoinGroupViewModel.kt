package com.example.moco_project_fibuflat.activitySelectGroup.ui.joinGroup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.OpenRequestGroup
import com.example.moco_project_fibuflat.data.OpenRequestUser
import com.example.moco_project_fibuflat.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

//ToDo !!!This whole class need to use coroutines. Currently one function after an other -> blocking code !!!
class JoinGroupViewModel : ViewModel() {
    private lateinit var database: DatabaseReference
    private lateinit var user: User
    private lateinit var group: Group
    private lateinit var userID: String
    private var stop: Boolean = false

    fun joinGroup(groupName: String, groupId: String) {
        stop = false
        userID = FirebaseAuth.getInstance().currentUser!!.uid

        checkRequestAlreadySent(groupName, groupId)

    }

    private fun checkRequestAlreadySent(groupName: String, groupId: String) {
        database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")
        val userRef = database.child(userID).child("openRequests").orderByChild("groupName")
            .equalTo(groupName)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    if (ds.child("groupID").getValue(String::class.java)
                            ?.substring(0, 4) == groupId
                    ) {
                        stop = true
                        break
                    }
                }
                if (!stop)
                    getGroup(groupName, groupId)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("joinFragment", "Request not found")
            }

        }
        userRef.addListenerForSingleValueEvent(valueEventListener)
    }

    private fun getGroup(groupName: String, groupId: String) {
        database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Groups")
        val groupsNamesRef = database.orderByChild("groupName").equalTo(groupName)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    Log.d("joinFragment1", ds.toString())
                    if (ds.child("groupId").getValue(String::class.java)
                            ?.substring(0, 4) == groupId
                    ) {
                        Log.d("joinFragment", ds.toString())
                        group = ds.getValue(Group::class.java)!!
                        getUser()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("joinFragment", "Group not found")
            }
        }

        groupsNamesRef.addListenerForSingleValueEvent(valueEventListener)
    }

    private fun getUser() {

        this.database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")

        val userRef = database.child(userID)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User::class.java)!!
                    setDatabase()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("joinGroup", "cancelled")
            }
        }
        userRef.addListenerForSingleValueEvent(valueEventListener)
    }

    private fun setDatabase() {

        val requestID: String = UUID.randomUUID().toString()
        val openRequestGroup = OpenRequestGroup(user.userID, user.username)
        val openRequestUser = OpenRequestUser(group.groupId, requestID, group.groupName)

        //set Group Request
        database.parent!!.child("Groups").child(group.groupId!!).child("openRequestsByUsers")
            .child(requestID).setValue(openRequestGroup)

        //set User Request
        database = //works
            database.parent!!.child("Users").child(userID).child("openRequestsToGroups").child(requestID)
        database.setValue(openRequestUser)
    }

}