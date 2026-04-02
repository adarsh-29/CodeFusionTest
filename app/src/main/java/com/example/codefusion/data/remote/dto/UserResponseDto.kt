package com.example.codefusion.data.remote.dto

import com.example.codefusion.domain.model.User

data class UserResponseDto(
    val users: List<UserDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

data class UserDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val gender: String,
    val email: String,
    val phone: String,
    val image: String,
    val birthDate: String,
    val address: AddressDto
)

data class AddressDto(
    val city: String,
    val state: String,
    val country: String
)

fun UserDto.toDomain(): User {
    return User(
        id = id,
        fullName = "$firstName $lastName",
        age = age,
        gender = gender,
        email = email,
        phone = phone,
        image = image,
        birthDate = birthDate,
        city = address.city,
        state = address.state,
        country = address.country
    )
}