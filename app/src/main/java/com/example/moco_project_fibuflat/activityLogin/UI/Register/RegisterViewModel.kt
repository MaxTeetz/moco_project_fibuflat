package com.example.moco_project_fibuflat.activityLogin.UI.Register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel(){
    private val _email = MutableLiveData("")
    val email: LiveData<String> get() = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> get() = _password

    private val _username = MutableLiveData("")
    val username: LiveData<String> get() = _username

    fun isTextInputEmpty(textInput: String): Boolean {
        return textInput.isBlank()
    }

    fun setData(email: String, password: String, username: String) {//ToDo set data in data Package LoggedInUser or Repository
        _email.value = email
        _password.value = password
        _username.value = username
    }
}