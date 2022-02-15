package com.example.moco_project_fibuflat.activityGroup.ui.home.poolEntry.entryDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EntryDetailViewModel : ViewModel() {

    suspend fun getMoneyPoolEntry(
        id: String,
        groupId: String,
        dataBaseGroup: DatabaseReference,
    ): DataSnapshot? {
        return try {
            val moneyPoolEntry =
                dataBaseGroup.child(groupId).child("moneyPoolEntries").child(id).get().await()
            moneyPoolEntry
        } catch (e: Exception) {
            Log.d("entryDetail", "$e")
            null
        }
    }

    suspend fun deleteEntry(id: String, groupId: String, dataBaseGroup: DatabaseReference) {
        withContext(Dispatchers.IO) {
            dataBaseGroup.child(groupId).child("moneyPoolEntries").child(id).removeValue()
        }
    }

}