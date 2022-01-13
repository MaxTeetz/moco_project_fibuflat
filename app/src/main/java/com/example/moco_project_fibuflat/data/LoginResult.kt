package com.example.moco_project_fibuflat.data

import com.example.moco_project_fibuflat.data.model.LoggedInUser

data class LoginResult(
    val success: LoggedInUser? = null,
    val error: Int? = null
)
