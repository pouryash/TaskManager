package com.example.taskmanager.data.repository

import com.example.taskmanager.data.api.WebServices


class Repository constructor(
    private val api: WebServices
) : RepositoryInterface {


}