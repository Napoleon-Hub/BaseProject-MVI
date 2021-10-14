package com.baseproject.utils.api

import com.baseproject.utils.error.ApplicationError

/**
 * A discriminated union that encapsulates a successful outcome with a value of type [T]
 * or an error with a value of type [ApplicationError].
 */
sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val error: ApplicationError) : ResultWrapper<Nothing>()
}

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated value
 * if this instance represents [ResultWrapper.Success] or the original encapsulated [ApplicationError] value
 * if it is [ResultWrapper.Error].
 */
inline fun <R, T> ResultWrapper<T>.map(transform: (value: T) -> R): ResultWrapper<R> {
    return when (this) {
        is ResultWrapper.Success -> ResultWrapper.Success(transform(value))
        is ResultWrapper.Error   -> ResultWrapper.Error(this.error)
    }
}

/**
 * Performs the given [action] on the encapsulated value if this instance represents [ResultWrapper.Success].
 * Returns the original `Result` unchanged.
 */
inline fun <T> ResultWrapper<T>.onSuccess(action: (value: T) -> Unit): ResultWrapper<T> {
    if (this is ResultWrapper.Success) action(value)
    return this
}

/**
 * Performs the given [action] on the encapsulated [ApplicationError] value if this instance represents [ResultWrapper.Error].
 * Returns the original `Result` unchanged.
 */
inline fun <T> ResultWrapper<T>.onError(action: (exception: ApplicationError) -> Unit): ResultWrapper<T> {
    if (this is ResultWrapper.Error) action(error)
    return this
}