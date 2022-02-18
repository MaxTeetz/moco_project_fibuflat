package com.example.moco_project_fibuflat.helperClasses

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class GetSnapshotSingleEvent(
    databaseReference: Query,
    private val giveSnapshot: (snapshot: DataSnapshot) -> Unit,
) {
    init {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                giveSnapshot(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("gatSnapshot", "$error")
            }
        }
        databaseReference.addListenerForSingleValueEvent(valueEventListener)
    }
}