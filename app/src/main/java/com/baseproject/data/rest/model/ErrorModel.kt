package com.baseproject.data.rest.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

// Just example, you need check your api description and change this model
@Keep // Tag each model @Keep so that it should not be removed when the code is minified at build time
data class ErrorModel(
    var error: String?,
    @SerializedName("error_description")
    var errorDescription: String?
)
