package com.kotlin.helpers.models

sealed class ResponseResult<out T> {
    data class Success<out T>(val data: T) : ResponseResult<T>()
    data class Failure(val message: Message) : ResponseResult<Nothing>()
}