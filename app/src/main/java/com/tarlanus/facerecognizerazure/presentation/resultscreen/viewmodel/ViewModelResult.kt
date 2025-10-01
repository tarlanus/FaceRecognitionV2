package com.tarlanus.facerecognizerazure.presentation.resultscreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarlanus.facerecognizerazure.domain.common.UseCaseFacialAnalyser
import com.tarlanus.facerecognizerazure.presentation.register.state.RegisterState
import com.tarlanus.facerecognizerazure.presentation.resultscreen.state.ResultState
import com.tarlanus.facerecognizerazure.utils.FacialAnalyser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelResult @Inject constructor(private val useCaseFacialAnalyser: UseCaseFacialAnalyser)  : ViewModel() {

    private val _uiState : MutableStateFlow<ResultState> = MutableStateFlow(ResultState.IDLE)
    val uiState  get() = _uiState.asStateFlow()

    fun startTestImage(path: String) {
        Log.e("getDataToRegister", "path $path")
            _uiState.value = ResultState.TESTING
            useCaseFacialAnalyser.analyze(path) {
                if (it != null) {


                    Log.e("getAnalyzedResult", "$it")
                    _uiState.value = ResultState.SUCCESS("Same Person")

                } else {
                    _uiState.value = ResultState.ERROR("No contains the face")

                }
            }






    }

    fun setImageLoaded(path: String) {
        _uiState.value = ResultState.IMAGELOADED(path)
    }
}