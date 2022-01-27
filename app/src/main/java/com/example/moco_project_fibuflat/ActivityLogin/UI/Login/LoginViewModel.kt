package com.example.moco_project_fibuflat.ActivityLogin.UI.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.LoginResult

class LoginViewModel : ViewModel() {
    private val _email = MutableLiveData("")
    val email: LiveData<String> get() = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> get() = _password

    private val _username = MutableLiveData("")
    val username: LiveData<String> get() = _username

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> get() = _loginResult //ToDo check if login was successful

    fun isTextInputEmpty(mail: String): Boolean {
        return mail.isBlank()
    }

    fun loginCredentialsCorrect(email: String, password: String): Boolean {
        return true
    }
}