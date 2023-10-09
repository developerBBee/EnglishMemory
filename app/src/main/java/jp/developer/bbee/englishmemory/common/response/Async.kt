package jp.developer.bbee.englishmemory.common.response

sealed class Async<T>(
    val data: T? = null,
    val error: String? = null
) {
    class Success<T>(data: T) : Async<T>(data = data)
    class Failure<T>(error: String) : Async<T>(error = error)
    class Loading<T> : Async<T>()
}