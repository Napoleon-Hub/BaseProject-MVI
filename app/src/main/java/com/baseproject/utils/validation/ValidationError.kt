package com.baseproject.utils.validation

import com.baseproject.utils.error.ApplicationError

data class ValidationError(
    val viewTag: ViewTag,
    val error: ApplicationError
)

sealed class ViewTag {
    object ProfileName : ViewTag()
    object Email : ViewTag()
}