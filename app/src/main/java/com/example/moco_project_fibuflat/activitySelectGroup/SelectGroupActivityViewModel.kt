package com.example.moco_project_fibuflat.activitySelectGroup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SelectGroupActivityViewModel : ViewModel() {
    private var _groupStatus: MutableLiveData<Group> = MutableLiveData()
    val groupStatus: LiveData<Group> get() = _groupStatus

    suspend fun checkGroupStatus(userReference: DatabaseReference, user: User) {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.d("selectGroupViewModel", "$snapshot")
                    _groupStatus.value = snapshot.getValue(Group::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("SelectGroupGroupStatus", "$error")
            }
        }
        withContext(Dispatchers.IO) {
            userReference.child(user.userID!!).child("group").addValueEventListener(valueEventListener)
            Log.d("selectGroupActivityCheckForGroupChange", "")
        }
    }
}