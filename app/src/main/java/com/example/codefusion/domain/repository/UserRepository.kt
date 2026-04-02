package com.example.codefusion.domain.repository

import com.example.codefusion.domain.model.UserResponse

interface UserRepository {
    suspend fun getUsers(
        limit: Int,
        skip: Int,
        gender: String?
    ): Result<UserResponse>
}