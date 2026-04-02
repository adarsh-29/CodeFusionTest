package com.example.codefusion.presentation.ui.viewmodel.listing_states

import com.example.codefusion.domain.model.User

data class UserState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val endReached: Boolean = false
)