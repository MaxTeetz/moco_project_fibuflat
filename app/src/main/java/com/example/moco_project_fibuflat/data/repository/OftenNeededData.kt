package com.example.moco_project_fibuflat.data.repository

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private var _user: User? = null

    private var _group: Group? = null

    fun getUser(): User? {
        return _user
    }

    fun getGroup(): Group? {
        return _group
    }

    suspend fun setData() {

        viewModelScope.launch(Dispatchers.Default) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
            var group: Group?
            var user: User? = null
            Log.d("dataUser0", user.toString())

            user = fetchUser(uid)
            delay(500)
            Log.d("dataUser3", user.toString())

            _dataBaseUsers.child(uid).child("group").get().addOnSuccessListener {
                group = it.getValue(Group::class.java)!!
                _user = User(user?.userID,
                    user?.username,
                    user?.email,
                    group?.groupId,
                    group?.groupName)
                _group = group
                Log.d("dataUser", _user.toString())
                Log.d("dataUser", user.toString())
            }
        }
    }

    private suspend fun fetchUser(uid: String): User =
        withContext(Dispatchers.Default) { //ToDo I.O.?
            var user: User? = null

            var task1 =
                _dataBaseUsers.child(FirebaseAuth.getInstance().currentUser?.uid.toString()).get()
                    .addOnSuccessListener {
                        user = it.getValue(User::class.java)!!
                        Log.d("dataUser1", user.toString())
                    }

            if (task1.isComplete) {
                Log.d("dataUser2", user.toString())
            }
            return@withContext user!!
        }
}
