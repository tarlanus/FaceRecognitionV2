package com.tarlanus.facerecognitionv2.presentation.register.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarlanus.facerecognitionv2.data.local.UserFaces
import com.tarlanus.facerecognitionv2.domain.common.UseCaseFacialAnalyser
import com.tarlanus.facerecognitionv2.domain.usecase.UseCaseNewRegistration
import com.tarlanus.facerecognitionv2.presentation.register.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelRegister @Inject constructor(private val useCaseFacialAnalyser: UseCaseFacialAnalyser,
    private val useCaseRegisterNewRegistration: UseCaseNewRegistration)  : ViewModel() {
    private val _uiState : MutableStateFlow<RegisterState> = MutableStateFlow(RegisterState.IDLE)
    val uiState  get() = _uiState.asStateFlow()

    private val _tf = MutableStateFlow("")
    val tf = _tf.asStateFlow()
    fun startRegisterImage(path: String) {

        if (tf.value.length == 0) {
            _uiState.value = RegisterState.ERROR("Value must be greater than 0")
            return
        }
        _uiState.value = RegisterState.ISREGISTERING

        Log.e("getDataToRegister", "path $path")

        val exception = CoroutineExceptionHandler { _, throwable ->
            _tf.value = ""
            Log.e("getDataToRegister", "throwable ${throwable.message}")
            _uiState.value = RegisterState.ERROR(errorMsg = throwable.message.toString())
        }

        viewModelScope.launch(Dispatchers.IO + exception) {
            val newUser = UserFaces(id = 0, imagePath = path, candidateName = tf.value)
            useCaseRegisterNewRegistration.invoke(newUser)
            delay(1000)
            _uiState.value = RegisterState.SUCCESS
            delay(100)
            _tf.value = ""


        }
    }
    fun setImageSelection(path: String) {
        _uiState.value = RegisterState.ISREGISTERING

        useCaseFacialAnalyser.analyze(path) {
            if (it != null) {
                Log.e("getFace", "$it")

                _uiState.value = RegisterState.IMAGESELECTED(path)

            } else {
                _uiState.value = RegisterState.ERROR("No contains the face")

            }
        }


    }

    fun restoreState() {
        _uiState.value = RegisterState.IDLE
    }
    fun setTf(value : String) {
        _tf.value = value
    }

}