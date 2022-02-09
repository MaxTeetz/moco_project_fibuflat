package com.example.moco_project_fibuflat.data

data class User(
    val userId: String? = null, //ToDo
    val username: String? = null,
    val email: String? = null,
    val groupId: String? = null
)

data class Group(
    val groupID: String? = null, //random UUID to string
    val groupName: String? = null,
    val users: MutableList<String>?
)