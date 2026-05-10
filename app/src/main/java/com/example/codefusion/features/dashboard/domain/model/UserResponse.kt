package com.example.codefusion.features.dashboard.domain.model

data class UserResponse(
    val users: List<User>,
    val total: Int
)