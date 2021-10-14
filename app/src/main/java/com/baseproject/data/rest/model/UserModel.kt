package com.baseproject.data.rest.model

import androidx.annotation.Keep

@Keep
data class UserModel(
    val name: String,
    val age: Int,
    val profession: String
)