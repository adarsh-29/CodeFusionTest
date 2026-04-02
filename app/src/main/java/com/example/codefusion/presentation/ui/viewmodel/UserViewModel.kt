package com.example.codefusion.presentation.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codefusion.domain.model.User
import com.example.codefusion.domain.usecase.GetUsersUseCase
import com.example.codefusion.presentation.ui.viewmodel.listing_states.SortOrder
import com.example.codefusion.presentation.ui.viewmodel.listing_states.UiFilterState
import com.example.codefusion.presentation.ui.viewmodel.listing_states.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.onSuccess

@HiltViewModel
class UserViewModel @Inject constructor(private val getUsersUseCase: GetUsersUseCase) : ViewModel() {

    var state by mutableStateOf(UserState())
        private set

    // for filtering
    var filterState by mutableStateOf(UiFilterState())
        private set

    private var currentPage = 0
    private val pageSize = 10


    init {
        loadUsers()
    }

    fun loadUsers(reset: Boolean = false) {

        if (state.isLoading) return

        viewModelScope.launch {

            if (reset) {
                currentPage = 0
                state = UserState(isLoading = true)
            } else {
                state = state.copy(isLoading = true)
            }

            val skip = currentPage * pageSize

            val result = getUsersUseCase(
                limit = pageSize,
                skip = skip,
                gender = filterState.gender
            )

            result.onSuccess { response ->

                currentPage++

                val newList = if (reset) {
                    response.users
                } else {
                    state.users + response.users
                }

                state = state.copy(
                    users = newList,
                    isLoading = false,
                    error = null,
                    endReached = newList.size >= response.total
                )

            }.onFailure {
                state = state.copy(
                    isLoading = false,
                    error = it.message
                )
            }
        }
    }

    fun refresh() {
        loadUsers(reset = true)
    }
    fun shouldLoadMore(lastVisibleIndex: Int): Boolean {
        return lastVisibleIndex >= state.users.size - 1 &&
                !state.isLoading &&
                !state.endReached
    }






                      /** For filtering and sorting **/

    val filteredUsers: List<User>
        get() {
            val baseList = state.users

            var list = baseList

            // Searching
            if (filterState.searchQuery.isNotBlank()) {
                list = list.filter {
                    it.fullName.contains(filterState.searchQuery, ignoreCase = true)
                }
            }

            // Sorting
            list = when (filterState.sortOrder) {
                SortOrder.AZ -> list.sortedBy { it.fullName }
                SortOrder.ZA -> list.sortedByDescending { it.fullName }
                else -> list
            }

            return list
        }

    fun updateSearch(query: String) {
        filterState = filterState.copy(searchQuery = query)
    }

    fun updateSort(order: SortOrder) {
        filterState = filterState.copy(sortOrder = order)
    }

    fun updateGender(gender: String?) {
        filterState = filterState.copy(gender = gender)
        loadUsers(reset = true)
    }

     // For user detail screen
    fun getUserById(id: Int): User? {
        return state.users.find { it.id == id }
    }

}