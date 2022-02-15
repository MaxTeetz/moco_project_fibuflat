package com.example.moco_project_fibuflat.activityGroup.ui.home.poolEntry.addEntry

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.MoneyPoolEntry
import com.example.moco_project_fibuflat.data.User
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AddEntryViewModel : ViewModel() {

    private lateinit var group: Group
    private lateinit var user: User
    private lateinit var databaseReference: DatabaseReference

    fun isEntryValid(moneyAmount: String, message: String): Boolean {
        if (moneyAmount.isNotBlank() && moneyAmount.toInt() < 20001 && message.isNotBlank())
            return true
        return false
    }

    suspend fun addEntryToDB(moneyPoolEntry: MoneyPoolEntry) {
        withContext(Dispatchers.IO) {
            Log.d("addEntryViewModel1", "")
            databaseReference.child(group.groupId!!).child("moneyPoolEntries")
                .child(moneyPoolEntry.id!!).setValue(moneyPoolEntry)
            Log.d("addEntryViewModel2", "")
        }
    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy\nhh:mm:ss")
        return sdf.format(Date())
    }

    fun setData(user: User, group: Group, databaseReference: DatabaseReference) {
        this.user = user
        this.group = group
        this.databaseReference = databaseReference
    }
}