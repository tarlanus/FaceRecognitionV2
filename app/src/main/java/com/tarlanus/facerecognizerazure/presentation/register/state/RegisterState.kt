package com.tarlanus.facerecognizerazure.presentation.register.state

sealed class RegisterState {
    object ISREGISTERING : RegisterState()
    object IDLE : RegisterState()
    data class ERROR(val errorMsg : String) : RegisterState()
    object SUCCESS : RegisterState()
    data class IMAGESELECTED(val path : String) : RegisterState()





}