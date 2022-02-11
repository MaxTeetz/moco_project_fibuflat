package com.example.moco_project_fibuflat.activitySelectGroup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.User
import com.google.firebase.database.*

//ToDo !!!This whole class need to use coroutines. Currently one function after an other -> blocking code !!!
class SelectGroupActivityViewModel : ViewModel() {

    private lateinit var user: User
    private lateinit var group: Group
    private lateinit var userKey: String
    private lateinit var database: DatabaseReference


    fun createGroup(group: Group, userID: String) {
        this.userKey = userID
        this.group = group
        getUser(userID)
    }

    private fun getUser(userID: String) {
        this.database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")

        val userRef = database.child(userID)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User::class.java)!!
                    Log.d("selectGroup", snapshot.key.toString())
                    setDatabase() //ToDo this part?
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("selectGroup", "cancelled")
            }
        }
        userRef.addListenerForSingleValueEvent(valueEventListener)
    }

    private fun setDatabase() {
        Log.d("selectGroup", "await")
        database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Groups")

        database.child(group.groupId!!).setValue(group)
        database.child(group.groupId!!).child("users").child(userKey).setValue(user)
    }
}