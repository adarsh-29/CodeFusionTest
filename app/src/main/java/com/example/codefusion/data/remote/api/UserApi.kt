package com.example.codefusion.data.remote.api

import com.example.codefusion.data.remote.dto.UserResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("users")
    suspend fun getUsers(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
        @Query("key") key: String?,
        @Query("value") value: String?
    ): UserResponseDto
}