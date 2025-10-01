package com.tarlanus.facerecognizerazure.presentation.resultscreen.state

import com.tarlanus.facerecognizerazure.presentation.register.state.RegisterState

sealed class ResultState {

    object TESTING : ResultState()
    data class IMAGELOADED(val path : String) : ResultState()
    object IDLE : ResultState()
    data class ERROR(val msg : String) : ResultState()
    data class SUCCESS(val msg : String) : ResultState()
}