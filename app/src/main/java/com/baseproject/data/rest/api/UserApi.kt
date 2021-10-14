package com.baseproject.data.rest.api

import com.baseproject.data.rest.model.UserModel
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    @GET("users")
    suspend fun getUsers(): List<UserModel>

    @GET("users/{name}")
    suspend fun getUserByName(@Path("name") name: String): UserModel

}