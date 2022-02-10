package com.example.moco_project_fibuflat.activityLogin.UI.Register

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.data.ErrorMessage
import com.example.moco_project_fibuflat.data.ErrorMessageType

class RegisterViewModel : ViewModel() {

    private val _errorMessage = MutableLiveData<ErrorMessage>()
    val errorMessage: LiveData<ErrorMessage> get() = _errorMessage

    private fun isTextInputEmpty(textInput: String): Boolean {
        return textInput.isBlank()
    }

    //check if password and confirm password are empty and compare them
    private fun checkPassword(
        registerPassword: String,
        confirmPassword: String,
        context: Context
    ): Boolean {
        var check = true

        if (isTextInputEmpty(registerPassword)) {
            _errorMessage.value = ErrorMessage(
                true,
                ErrorMessageType.PASSWORD,
                context.getString(R.string.emptyPassword)
            )
            check = false
        } else
            _errorMessage.value = ErrorMessage(false, ErrorMessageType.PASSWORD, null)

        if (isTextInputEmpty(confirmPassword)) {
            _errorMessage.value = ErrorMessage(
                true,
                ErrorMessageType.CONFIRMPASSWORD,
                context.getString(R.string.empty_confirm_password)
            )
            check = false
        } else {
            _errorMessage.value = ErrorMessage(false, ErrorMessageType.CONFIRMPASSWORD, null)
        }

        if (confirmPassword != registerPassword && check) {
            _errorMessage.value = ErrorMessage(
                true,
                ErrorMessageType.PASSWORDCONFIRMPASSWORD,
                context.getString(R.string.password_confirm_password_unequal)
            )
            check = false
        }
        return check
    }

    fun onRegister(
        email: String,
        username: String,
        registerPassword: String,
        confirmPassword: String,
        context: Context
    ) {
        var anyFieldEmpty = false

        //check if email or username is empty
        if (isTextInputEmpty(email)) {
            _errorMessage.value =
                ErrorMessage(true, ErrorMessageType.EMAIL, context.getString(R.string.emptyMail))
            anyFieldEmpty = true
        } else {
            _errorMessage.value = ErrorMessage(false, ErrorMessageType.EMAIL, null)
        }
        if (isTextInputEmpty(username)) {
            _errorMessage.value = ErrorMessage(
                true,
                ErrorMessageType.USERNAME,
                context.getString(R.string.empty_username)
            )
            anyFieldEmpty = true
        } else {
            _errorMessage.value = ErrorMessage(false, ErrorMessageType.USERNAME, null)
        }

        if (checkPassword(registerPassword, confirmPassword, context) && !anyFieldEmpty)
            _errorMessage.value = ErrorMessage(null, null, null)
    }


}