package com.baseproject.utils.error

import com.baseproject.R

sealed class ApplicationError {

    // Common errors

    object Generic : ApplicationError()
    object NoInternetConnection : ApplicationError()

    // Http errors

    object BadRequest : ApplicationError()
    object Unauthorized : ApplicationError()
    object NotFound : ApplicationError()
    object TimeOut : ApplicationError()
    object Server : ApplicationError()

    // Client validation errors
    object AccountExists : ApplicationError()
    object AccountDoesntExist : ApplicationError()
    object EmptyFiled : ApplicationError()
    object InvalidField : ApplicationError()
    object ForbiddenMaxLength : ApplicationError()
    object ForbiddenNickname : ApplicationError()
}

fun ApplicationError.message() =

    when (this) {

        ApplicationError.Generic               -> R.string.generic_error
        ApplicationError.NoInternetConnection  -> R.string.no_internet_connection_error

        ApplicationError.BadRequest            -> R.string.bad_request_error
        ApplicationError.NotFound              -> R.string.not_found_error
        ApplicationError.Unauthorized          -> R.string.unauthorized_error
        ApplicationError.TimeOut               -> R.string.timeout_error
        ApplicationError.Server                -> R.string.server_error

        ApplicationError.AccountExists         -> R.string.account_already_exists_error
        ApplicationError.AccountDoesntExist    -> R.string.account_does_not_exist_error
        ApplicationError.EmptyFiled            -> R.string.empty_filed_error
        ApplicationError.InvalidField          -> R.string.invalid_email_error
        ApplicationError.ForbiddenMaxLength    -> R.string.forbidden_max_length_error
        ApplicationError.ForbiddenNickname     -> R.string.invalidate_name_error
    }
