package com.example.codefusion.features.dashboard.domain.repository

import com.example.codefusion.features.dashboard.domain.model.UserResponse


interface UserRepository {
    suspend fun getUsers(
        limit: Int,
        skip: Int,
        gender: String?
    ): Result<UserResponse>
}