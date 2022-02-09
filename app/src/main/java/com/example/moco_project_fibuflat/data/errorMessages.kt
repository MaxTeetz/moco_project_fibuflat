package com.example.moco_project_fibuflat.data

enum class ErrorMessageType {
    EMAIL,
    USERNAME,
    PASSWORD,
    CONFIRMPASSWORD,
    PASSWORDCONFIRMPASSWORD
}

data class ErrorMessage(
    var error: Boolean?,
    var errorMessageType: ErrorMessageType?,
    var message: String?
)
