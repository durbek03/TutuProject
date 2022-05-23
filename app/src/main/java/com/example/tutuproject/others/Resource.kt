package com.example.tutuproject.others

sealed class Resource<T>(val content: T?, val errorMessage: String = "") {
    class Loading<T>(content: T? = null) : Resource<T>(content)
    class Success<T>(content: T) : Resource<T>(content)
    class Error<T>(errorMessage: String) : Resource<T>(null, errorMessage)
}