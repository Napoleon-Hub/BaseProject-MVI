package com.baseproject.domain.remote.user

import com.baseproject.data.rest.api.UserApi
import com.baseproject.data.rest.model.UserModel
import com.baseproject.utils.api.ResultWrapper
import com.baseproject.utils.api.safeApiCall
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val api: UserApi) : UserRepository {

    override suspend fun getUsers(): ResultWrapper<List<UserModel>>  = safeApiCall { api.getUsers() }

    override suspend fun getUserByName(name: String): ResultWrapper<UserModel> = safeApiCall { api.getUserByName(name) }

}