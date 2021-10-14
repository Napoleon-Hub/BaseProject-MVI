package com.baseproject.domain.remote.user

import com.baseproject.data.rest.model.UserModel
import com.baseproject.utils.api.ResultWrapper

interface UserRepository {
    suspend fun getUsers(): ResultWrapper<List<UserModel>>
    suspend fun getUserByName(name: String): ResultWrapper<UserModel>
}