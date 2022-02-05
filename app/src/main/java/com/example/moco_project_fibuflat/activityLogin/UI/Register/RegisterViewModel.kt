package com.example.moco_project_fibuflat.activityLogin.UI.Register

import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    fun isTextInputEmpty(textInput: String): Boolean {
        return textInput.isBlank()
    }

}