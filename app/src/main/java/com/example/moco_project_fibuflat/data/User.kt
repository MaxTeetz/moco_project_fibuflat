package com.example.moco_project_fibuflat.data

data class User(
    val userID: String? = null,
    val username: String? = null,
    val email: String? = null,
    val groupId: String? = null,
    val groupOpenInvitations: String? = null, //invitations by groups -> id of group
    val groupOpenRequests: String? = null //requests to join group -> id of group
)

data class Group(
    val groupId: String? = null,
    val groupName: String? = null,
    val users: User? = null,
    val openInvitations: String? = null, //invited users -> id of user
    val openRequests: String? = null //open requets by users -> id of user
)


data class OpenRequestGroup( //used in Group
    var userID: String? = null,
    var username: String? = null
)

data class OpenRequestUser( //used in User
    var groupID: String? = null,
    var requestID: String? = null,
    var groupName: String? = null
)