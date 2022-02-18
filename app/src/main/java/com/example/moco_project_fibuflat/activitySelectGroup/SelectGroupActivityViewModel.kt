package com.example.moco_project_fibuflat.activitySelectGroup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class SelectGroupActivityViewModel : ViewModel() {
    private var _groupStatus: MutableLiveData<Group> = MutableLiveData()
    val groupStatus: LiveData<Group> get() = _groupStatus

    fun checkGroupStatus(databaseReference: DatabaseReference) { //ToDo ask prof if coroutine is needed
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                    _groupStatus.value = snapshot.getValue(Group::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("SelectGroupGroupStatus", "$error")
            }
        }
        databaseReference.child("group").addValueEventListener(valueEventListener)
    }
}