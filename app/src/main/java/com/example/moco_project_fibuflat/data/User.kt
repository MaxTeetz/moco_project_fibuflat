package com.example.moco_project_fibuflat.data

data class User(
    val userId: String? = null, //ToDo
    val username: String? = null,
    val email: String? = null,
    val groupId: String? = null,
    val groupOpenInvitations: MutableList<String>? = null, //invitations by groups -> id of group
    val groupOpenRequests: MutableList<String>? = null //requests to join group -> id of group
)

data class Group(
    val groupID: String? = null, //random UUID to string
    val groupName: String? = null,
    val users: MutableList<String>? = null, //ToDo without List?
    val openInvitations: MutableList<String>? = null, //invited users -> id of user
    val openRequests: MutableList<String>? = null //open requets by users -> id of user
)