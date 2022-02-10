package com.example.moco_project_fibuflat.activitySelectGroup.ui.joinGroup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class JoinGroupViewModel : ViewModel() {
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

    private fun sendRequest(id: String){
        database.child(id).child("openRequests").child(UUID.randomUUID().toString()).setValue(FirebaseAuth.getInstance().currentUser!!.uid)
    }
}