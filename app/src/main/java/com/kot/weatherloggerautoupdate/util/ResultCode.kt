package com.kot.weatherloggerautoupdate.util

import java.lang.Exception

sealed class ResultCode<out T: Any>(data: T? = null, message: String? = null) {
    class Success<out T : Any>(val data: T?) : ResultCode<T>()
    class Loading : ResultCode<Nothing>()
    class Error(exception: Exception) : ResultCode<Nothing>(message = exception.message)
}