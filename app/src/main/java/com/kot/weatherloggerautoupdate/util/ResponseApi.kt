package com.kot.weatherloggerautoupdate.util

class ResponseApi<T> private constructor(val status: ResponseApi.Status, val data: T?, val apiError:Throwable?) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }
    companion object {
        fun <T> success(data: T?): ResponseApi<T> {
            return ResponseApi(Status.SUCCESS, data, null)
        }
        fun <T> error(apiError: Throwable?): ResponseApi<T> {
            return ResponseApi(Status.ERROR, null, apiError)
        }
        fun <T> loading(data: T?): ResponseApi<T> {
            return ResponseApi(Status.LOADING, data, null)
        }
    }
}