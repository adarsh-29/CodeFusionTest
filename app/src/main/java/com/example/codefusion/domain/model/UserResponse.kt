package com.example.codefusion.domain.model

data class UserResponse(
    val users: List<User>,
    val total: Int
)