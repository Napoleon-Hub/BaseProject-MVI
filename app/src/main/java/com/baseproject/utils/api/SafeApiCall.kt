package com.baseproject.utils.api

import com.baseproject.data.rest.model.ErrorModel
import com.baseproject.utils.error.ApplicationError
import com.google.gson.Gson
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.net.HttpURLConnection.HTTP_BAD_REQUEST
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

suspend fun <T> safeApiCall(apiCall: suspend () -> T): ResultWrapper<T> {
    return try {
        ResultWrapper.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException   -> ResultWrapper.Error(ApplicationError.TimeOut)
            is ConnectException         -> ResultWrapper.Error(ApplicationError.NoInternetConnection)
            is UnknownHostException     -> ResultWrapper.Error(ApplicationError.NoInternetConnection)
            is HttpException            -> {

                val error = throwable.getCustomHttpError().getApplicationErrorOrNull()
                if (error != null) return ResultWrapper.Error(error)

                when (throwable.code()) {
                    HTTP_BAD_REQUEST    -> ResultWrapper.Error(ApplicationError.BadRequest)
                    HTTP_UNAUTHORIZED   -> ResultWrapper.Error(ApplicationError.Unauthorized)
                    HTTP_NOT_FOUND      -> ResultWrapper.Error(ApplicationError.NotFound)
                    HTTP_INTERNAL_ERROR -> ResultWrapper.Error(ApplicationError.Server)
                    else                -> ResultWrapper.Error(ApplicationError.Generic)
                }
            }
            else                        -> ResultWrapper.Error(ApplicationError.Generic)
        }
    }
}

// These methods need if you have errors with custom description
private fun HttpException.getCustomHttpError(): String? {
    return try {
        this.response()?.errorBody()?.charStream()?.let {
            Gson().fromJson(it, ErrorModel::class.java).errorDescription
        }
    } catch (exception: Exception) {
        null
    }
}

private fun String?.getApplicationErrorOrNull() = when (this) {
    "account already exists"                                   -> ApplicationError.AccountExists
    "account doesn't exist"                                    -> ApplicationError.AccountDoesntExist
    else                                                       -> null
}
