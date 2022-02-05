package com.example.moco_project_fibuflat.activityLogin.UI.Login

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    fun isTextInputEmpty(mail: String): Boolean {
        return mail.isBlank()
    }

}