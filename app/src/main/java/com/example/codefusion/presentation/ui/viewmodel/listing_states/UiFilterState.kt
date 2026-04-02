package com.example.codefusion.presentation.ui.viewmodel.listing_states

data class UiFilterState(
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder.NONE,
    val gender: String? = null
)

enum class SortOrder {
    AZ, ZA, NONE
}