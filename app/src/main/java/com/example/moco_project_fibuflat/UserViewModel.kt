package com.example.moco_project_fibuflat

import androidx.lifecycle.*
import com.example.moco_project_fibuflat.data.database.DatabaseUser
import com.example.moco_project_fibuflat.data.database.UserInfoDao
import kotlinx.coroutines.launch

class UserViewModel(private val userInfoDao: UserInfoDao) : ViewModel() {
    val allUsers: LiveData<List<DatabaseUser>> = userInfoDao.getItems().asLiveData()

    fun addNewUserInfo(email: String, password: String) {
        val newUserInfo = getNewUserInfoEntry(email, password)
        insertUserInfo(newUserInfo)
    }

    private fun insertUserInfo(newUserInfo: DatabaseUser) {
        viewModelScope.launch {
            userInfoDao.insert(newUserInfo)
        }
    }

    fun isEntryValid(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank())
            return false
        return true
    }

    private fun getNewUserInfoEntry(email: String, password: String): DatabaseUser {
        return DatabaseUser(
            email = email,
            password = password
        )
    }

    fun retrieveUserInfo(id: Int): LiveData<DatabaseUser> {
        return userInfoDao.getItem(id).asLiveData()
    }

    fun updateUserInfo(databaseUser: DatabaseUser) {
        viewModelScope.launch {
            userInfoDao.update(databaseUser)
        }
    }

    fun deleteItem(databaseUser: DatabaseUser) {
        viewModelScope.launch {
            userInfoDao.delete(databaseUser)
        }
    }

    private fun getUpdatedUserInfo(
        id: Int,
        email: String,
        password: String,
    ): DatabaseUser {
        return DatabaseUser(id = id, email = email, password = password)
    }

    fun updateUserInfo( //later, it should be possible to update the user email and password.
        id: Int,
        email: String,
        password: String
    ){
        val updatedUserInfo = getUpdatedUserInfo(id, email, password)
        updateUserInfo(updatedUserInfo)
    }
}

class UserViewModelFactory(private val userInfoDao: UserInfoDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userInfoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
