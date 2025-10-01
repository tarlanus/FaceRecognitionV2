package com.tarlanus.facerecognizerazure.presentation.register.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarlanus.facerecognizerazure.domain.common.UseCaseFacialAnalyser
import com.tarlanus.facerecognizerazure.presentation.register.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelRegister @Inject constructor(private val useCaseFacialAnalyser: UseCaseFacialAnalyser)  : ViewModel() {
    private val _uiState : MutableStateFlow<RegisterState> = MutableStateFlow(RegisterState.IDLE)
    val uiState  get() = _uiState.asStateFlow()
    fun startRegisterImage(path: String, tfvalue: String) {

        if (tfvalue.length == 0) {
            _uiState.value = RegisterState.ERROR("Value must be greater than 0")
            return
        }
        Log.e("getDataToRegister", "path $path")
        viewModelScope.launch {
            _uiState.value = RegisterState.ISREGISTERING
            delay(3000)
            _uiState.value = RegisterState.SUCCESS


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

}