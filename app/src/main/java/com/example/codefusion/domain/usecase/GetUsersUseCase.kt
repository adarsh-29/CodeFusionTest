package com.example.codefusion.domain.usecase

import com.example.codefusion.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        limit: Int,
        skip: Int,
        gender: String?
    ) = repository.getUsers(limit, skip, gender)
}