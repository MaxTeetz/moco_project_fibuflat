package com.example.moco_project_fibuflat.helperClasses

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.Connectivity
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

//data like User or Group etc.
class OftenNeededData : ViewModel() {

    //shortcuts
    private val _dataBaseUsers: DatabaseReference =
        FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Users")
    val dataBaseUsers get() = _dataBaseUsers

    private val _dataBaseGroups: DatabaseReference =
        FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Groups")
    val dataBaseGroups get() = _dataBaseGroups

    private var _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> get() = _user

    private var _group: MutableLiveData<Group> = MutableLiveData()
    val group: LiveData<Group> get() = _group

    private var _connectivityStatus: MutableLiveData<Connectivity> = MutableLiveData()
    val connectivityStatus : LiveData<Connectivity> get() = _connectivityStatus

    fun setConnectivity(connectivity: Connectivity){
        this._connectivityStatus.value = connectivity
    }

    suspend fun setData() {
        Log.d("GroupActivityCoroutine", "1")
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val task1 = fetchUser(uid)
        val task2 = fetchGroup(uid)
        Log.d("GroupActivityCoroutine", "4")
        _user.value = task1?.getValue(User::class.java)!!
        Log.d("GroupActivityCoroutine", "5")
        _group.value = task2?.getValue(Group::class.java)
    }

    private suspend fun fetchUser(uid: String): DataSnapshot? {
        Log.d("GroupActivityCoroutine", "2.0")
        return try {
                val user = _dataBaseUsers.child(uid).get().await()
                Log.d("GroupActivityCoroutine", "2.1")
                user
            } catch (e: Exception) {
                Log.d("dataUserUser", "$e")
                null
        }
    }

    private suspend fun fetchGroup(uid: String): DataSnapshot? {
        Log.d("GroupActivityCoroutine", "3.0")
        return try {
            val group = _dataBaseUsers.child(uid).child("group").get().await()
            Log.d("GroupActivityCoroutine", "3.1")
            group
        } catch (e: Exception) {
            Log.d("dataUserGroup", "$e")
            null
        }

    }
}
