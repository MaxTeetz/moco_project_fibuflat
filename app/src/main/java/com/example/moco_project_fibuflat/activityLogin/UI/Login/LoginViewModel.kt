package com.example.moco_project_fibuflat.activityLogin.UI.Login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.data.ErrorMessage
import com.example.moco_project_fibuflat.data.ErrorMessageType

class LoginViewModel : ViewModel() {

    private val _errorMessage = MutableLiveData<ErrorMessage>()
    val errorMessage: LiveData<ErrorMessage> get() = _errorMessage

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

        if(check)
            _errorMessage.value = ErrorMessage(null, null, null)
    }

}