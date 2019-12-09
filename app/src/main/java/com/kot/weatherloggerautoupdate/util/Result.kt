package com.kot.weatherloggerautoupdate.util
sealed class Result<out T: Any> {
    class Success<out T : Any>(val data: T?) : Result<T>()
    class Error(val exception: Exception) : Result<Nothing>()
}