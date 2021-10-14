package com.baseproject.utils.validation

import com.baseproject.utils.MAX_FIELD_LENGTH
import com.baseproject.utils.api.ResultWrapper
import com.baseproject.utils.error.ApplicationError
import org.intellij.lang.annotations.RegExp
import com.baseproject.utils.validation.RegExp.Companion.EMAIL
import com.baseproject.utils.validation.RegExp.Companion.NICKNAME


fun String.startCheck(): ResultWrapper<String> {
    return ResultWrapper.Success(this)
}

fun ResultWrapper<String>.checkEmpty(): ResultWrapper<String> {
    return if (this is ResultWrapper.Success) {
        if (this.value.isNotEmpty()) ResultWrapper.Success(this.value)
        else ResultWrapper.Error(ApplicationError.EmptyFiled)
    } else this
}

fun ResultWrapper<String>.checkLength(): ResultWrapper<String> {
    return if (this is ResultWrapper.Success) {
        if (this.value.length <= MAX_FIELD_LENGTH) ResultWrapper.Success(this.value)
        else ResultWrapper.Error(ApplicationError.ForbiddenMaxLength)
    } else this
}

fun ResultWrapper<String>.checkEmailFormat(): ResultWrapper<String> {
    return if (this is ResultWrapper.Success) {
        if (this.value.isEmailValid()) ResultWrapper.Success(this.value)
        else ResultWrapper.Error(ApplicationError.InvalidField)
    } else this
}

fun ResultWrapper<String>.checkNicknameFormat(): ResultWrapper<String> {
    return if (this is ResultWrapper.Success) {
        if (this.value.isNicknameValid())
            ResultWrapper.Success(this.value)
        else ResultWrapper.Error(ApplicationError.ForbiddenNickname)
    } else this
}


private fun String.matchTo(@RegExp regExp: String) = matches(regExp.toRegex())

private fun String.isEmailValid() = this.matchTo(EMAIL)

private fun String.isNicknameValid() = this.matchTo(NICKNAME)


