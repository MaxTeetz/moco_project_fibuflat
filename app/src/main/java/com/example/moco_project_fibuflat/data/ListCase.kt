package com.example.moco_project_fibuflat.data

enum class ListCase {
    EMPTY,
    ADDED,
    DELETED
}

enum class AdapterCase{
    GroupMember,
    Request
}

data class CaseAndIndex(
    val listCase: ListCase,
    val index: Int
)