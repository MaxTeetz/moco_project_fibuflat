package com.example.moco_project_fibuflat.activityLogin.UI.Login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.data.ErrorMessage
import com.example.moco_project_fibuflat.data.ErrorMessageType
import com.example.moco_project_fibuflat.data.GroupAccess
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Users")

    private val _errorMessage = MutableLiveData<ErrorMessage>()
    val errorMessage: LiveData<ErrorMessage> get() = _errorMessage

    private val _groupAccess = MutableLiveData<GroupAccess>()
    val groupAccess: LiveData<GroupAccess> get() = _groupAccess

    private fun isTextInputEmpty(mail: String): Boolean {
        return mail.isBlank()
    }

    fun onLogin(email: String, password: String, context: Context) {
        var check = true

        if (isTextInputEmpty(email)) {
            _errorMessage.value = ErrorMessage(
                true,
                ErrorMessageType.EMAIL,
                context.getString(R.string.empty_username)
            )
            check = false
        } else
            _errorMessage.value = ErrorMessage(false, ErrorMessageType.EMAIL, null)
        if (isTextInputEmpty(password)) {
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

    suspend fun getDBGroupEntry() {
        withContext(Dispatchers.IO) {
            database.child(FirebaseAuth.getInstance().uid.toString()).child("group").get()
                .addOnSuccessListener {
                    if (it.value == null)
                        _groupAccess.value = GroupAccess.NOGROUP
                    else
                        _groupAccess.value = GroupAccess.INGROUP
                }
                .addOnFailureListener {
                    Log.d("mainActivity", "no Data retrieved")
                }
        }
    }
}