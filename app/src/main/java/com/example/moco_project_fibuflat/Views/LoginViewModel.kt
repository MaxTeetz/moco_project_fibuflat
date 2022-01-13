package com.example.moco_project_fibuflat.Views

import android.util.Log
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
    val loginResult: LiveData<LoginResult> get() = _loginResult

    fun isMailEmpty(mail: String): Boolean {
        return mail.isBlank()
    }

    fun isPasswordEmpty(password: String): Boolean {
        return password.isBlank()
    }

    fun isUsernameEmpty(username: String): Boolean {
        return username.isBlank()
    }

    fun isEmailValidRegister(email: String): Boolean { //Todo check with emailInUse database
        return true
    }

    fun loginCredentialsCorrect(email: String, password: String): Boolean{
        return true
    }

    fun setData(email: String, password: String, username: String) {
        _email.value = email
        _password.value = password
        _username.value = username

        Log.d("LoginRegisterDataSet", this.email.value!!.toString())
    }

}