package com.example.moco_project_fibuflat.data

import java.util.*

data class User(
    val userId: String? = null, //ToDo
    val username: String? = null,
    val email: String? = null,
    val groupId: UUID? = null
)

data class Group(
    val groupID: String? = null,
    val groupName: String? = null,
    val users: MutableList<String>
)