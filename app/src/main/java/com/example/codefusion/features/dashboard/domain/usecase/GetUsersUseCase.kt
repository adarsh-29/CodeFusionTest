package com.example.codefusion.features.dashboard.domain.usecase

import com.example.codefusion.features.dashboard.domain.repository.UserRepository
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