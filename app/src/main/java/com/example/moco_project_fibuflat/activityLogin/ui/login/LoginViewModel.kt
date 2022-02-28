package com.example.moco_project_fibuflat.activityLogin.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.data.ErrorMessage
import com.example.moco_project_fibuflat.data.ErrorMessageType
import com.example.moco_project_fibuflat.data.GroupAccess
import com.example.moco_project_fibuflat.data.UserDatabaseCase
import com.example.moco_project_fibuflat.data.database.DatabaseUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Users")

    private val _userDatabaseCase = MutableLiveData<UserDatabaseCase>()
    val userDatabaseCase: LiveData<UserDatabaseCase> get() = _userDatabaseCase

    private val _errorMessage = MutableLiveData<ErrorMessage>()
    val errorMessage: LiveData<ErrorMessage> get() = _errorMessage

    private val _groupAccess = MutableLiveData<GroupAccess>()
    val groupAccess: LiveData<GroupAccess> get() = _groupAccess


    fun onLogin(email: String, password: String, context: Context) {
        var check = true

        if (email.isBlank()) {
            _errorMessage.value = ErrorMessage(
                true,
                ErrorMessageType.EMAIL,
                context.getString(R.string.empty_username)
            )
            check = false
        } else
            _errorMessage.value = ErrorMessage(false, ErrorMessageType.EMAIL, null)

        if (password.isBlank()) {
            _errorMessage.value = ErrorMessage(
                true,
                ErrorMessageType.PASSWORD,
                context.getString(R.string.emptyPassword)
            )
            check = false
        } else {
            _errorMessage.value = ErrorMessage(false, ErrorMessageType.PASSWORD, null)
        }

        if (check)
            _errorMessage.value = ErrorMessage(null, null, null)
    }

    fun firebaseLogin(
        email: String,
        password: String,
        activated: Boolean,
        user: DatabaseUser?,
    ) {
        Log.d("loginFragmentFirebaseLogin", "$email + $password")
        CoroutineScope(Dispatchers.IO).launch {
            //maybe just use it in activity. Could work better
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    //sometimes it fails to login even with correct login credentials. Don't know why it happens.
                    //Requires app restart
                    CoroutineScope(Dispatchers.Default).launch {
                        if (activated) {
                            //update database
                            if ((user != null) && (user.email != email)) {
                                Log.d("loginFragment", "CHANGE")
                                _userDatabaseCase.postValue(UserDatabaseCase.CHANGE)
                            }
                            if (user?.email == null) {
                                Log.d("loginFragment", "ADD")
                                _userDatabaseCase.postValue(UserDatabaseCase.ADD)
                            }
                        }
                        if (!activated && user != null) {
                            Log.d("loginFragmentTask", "DELETE")
                            _userDatabaseCase.postValue(UserDatabaseCase.DELETE)
                        }
                    }
                    getDBGroupEntry()
                }.addOnFailureListener { task ->
                    Log.d("loginFragmentTaskFailed", "$task")
                    Log.d("loginFragmentTaskFailed", "$email + $password")
                }
        }
    }

    private fun getDBGroupEntry() {
        //fetch user info to decide which fragment to start
        CoroutineScope(Dispatchers.IO).launch {
            database.child(FirebaseAuth.getInstance().uid.toString()).child("group").get()
                .addOnSuccessListener {
                    if (it.value == null)
                        _groupAccess.value = GroupAccess.NOGROUP
                    else
                        _groupAccess.value = GroupAccess.INGROUP
                }
                .addOnFailureListener {
                    Log.d("mainActivity", "no Data retrieved")
                }.await()
        }
    }
}