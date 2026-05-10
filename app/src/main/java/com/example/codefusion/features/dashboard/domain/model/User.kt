package com.example.codefusion.features.dashboard.domain.model

data class User(
    val id: Int,
    val fullName: String,
    val age: Int,
    val gender: String,
    val email: String,
    val phone: String,
    val image: String,
    val birthDate: String,
    val city: String,
    val state: String,
    val country: String
)