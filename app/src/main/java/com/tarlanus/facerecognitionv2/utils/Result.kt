package com.tarlanus.facerecognitionv2.utils

sealed class Result<T>(val success: T? = null, val error: String? = null) {

    class IDLE<T>() : Result<T>()
    class SUCCESS<T>(data: T) : Result<T>(success = data, error = null)
    class ERROR<T>(msg : String) : Result<T>(success = null, error = msg)

}