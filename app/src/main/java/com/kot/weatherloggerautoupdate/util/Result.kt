package com.kot.weatherloggerautoupdate.util

import java.lang.Exception

sealed class Result<out T: Any>(data: T? = null, message: String? = null) {
    class Success<out T : Any>(val data: T?) : Result<T>()
    class Loading : Result<Nothing>()
    class Error(exception: Exception) : Result<Nothing>(message = exception.message)
}