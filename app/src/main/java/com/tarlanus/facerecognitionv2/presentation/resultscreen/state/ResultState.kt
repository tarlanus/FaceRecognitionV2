package com.tarlanus.facerecognitionv2.presentation.resultscreen.state

sealed class ResultState {

    object TESTING : ResultState()
    data class IMAGELOADED(val path : String, val name : String) : ResultState()
    object IDLE : ResultState()
    data class ERROR(val msg : String) : ResultState()
    data class SUCCESS(val msg : String) : ResultState()
}