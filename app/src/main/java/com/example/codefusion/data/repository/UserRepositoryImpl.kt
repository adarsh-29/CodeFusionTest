package com.example.codefusion.data.repository

import com.example.codefusion.data.remote.api.UserApi
import com.example.codefusion.data.remote.dto.toDomain
import com.example.codefusion.domain.model.UserResponse
import com.example.codefusion.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {

    override suspend fun getUsers(
        limit: Int,
        skip: Int,
        gender: String?
    ): Result<UserResponse> {
        return try {
            val response = api.getUsers(
                limit,
                skip,
                if (gender != null) "gender" else null,
                gender
            )

            Result.success(
                UserResponse(
                    users = response.users.map { it.toDomain() },
                    total = response.total
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}