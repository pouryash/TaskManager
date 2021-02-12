package com.example.taskmanager.utils


data class RIM<RequestData>(
    var state: Status? = null,
    var data: RequestData? = null,
    var error: String? = null,
    var code: Int? = null,
    var percent: Int? = 100
)

enum class Status { SUCCESSFUL, ERROR, LOADING, EMPTY }