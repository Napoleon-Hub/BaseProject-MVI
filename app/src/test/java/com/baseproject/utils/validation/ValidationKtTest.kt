package com.baseproject.utils.validation


import com.baseproject.utils.api.ResultWrapper
import com.baseproject.utils.error.ApplicationError
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidationKtTest {

    @Test
    fun startCheck() {
        val result = "Pups".startCheck()
        assertThat(result).isEqualTo(ResultWrapper.Success("Pups"))
    }

    @Test
    fun checkEmpty() {
        val result1 = "".startCheck().checkEmpty()
        val result2 = "11".startCheck().checkEmpty()
        assertThat(result1).isEqualTo(ResultWrapper.Error(ApplicationError.EmptyFiled))
        assertThat(result2).isEqualTo(ResultWrapper.Success("11"))
    }

    @Test
    fun checkLength() {
        val result1 = "11111111111111111111111111111111111111111111111111111111".startCheck().checkLength()
        val result2 = "Pups".startCheck().checkLength()
        assertThat(result1).isEqualTo(ResultWrapper.Error(ApplicationError.ForbiddenMaxLength))
        assertThat(result2).isEqualTo(ResultWrapper.Success("Pups"))
    }

    @Test
    fun checkEmailFormat() {
        val result1 = "111111111111@mail.ru".startCheck().checkEmailFormat()
        val result2 = "Pups".startCheck().checkEmailFormat()
        assertThat(result1).isEqualTo(ResultWrapper.Success("111111111111@mail.ru"))
        assertThat(result2).isEqualTo(ResultWrapper.Error(ApplicationError.InvalidField))
    }

    @Test
    fun checkNicknameFormat() {
        val result1 = "111111111111@mail.ru".startCheck().checkNicknameFormat()
        val result2 = "pupspupspups".startCheck().checkNicknameFormat()
        assertThat(result1).isEqualTo(ResultWrapper.Error(ApplicationError.ForbiddenNickname))
        assertThat(result2).isEqualTo(ResultWrapper.Success("pupspupspups"))
    }
}