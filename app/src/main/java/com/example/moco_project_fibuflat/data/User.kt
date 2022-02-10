package com.example.moco_project_fibuflat.data

data class User(
    val userId: String? = null, //ToDo
    val username: String? = null,
    val email: String? = null,
    val groupId: String? = null,
    val groupOpenInvitations: String? = null, //invitations by groups -> id of group
    val groupOpenRequests: String? = null //requests to join group -> id of group
)

data class Group(
    val groupID: String? = null, //random UUID to string
    val groupName: String? = null,
    val users: String? = null,
    val openInvitations: String? = null, //invited users -> id of user
    val openRequests: String? = null //open requets by users -> id of user
)

data class OpenRequestGroup(
    var userID: String? = null,
    var username: String? = null
)

data class OpenRequestUser(
    var groupID: String? = null,
    var requestID: String? = null
)