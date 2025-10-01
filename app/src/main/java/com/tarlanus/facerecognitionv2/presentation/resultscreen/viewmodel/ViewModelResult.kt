package com.tarlanus.facerecognitionv2.presentation.resultscreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.tarlanus.facerecognitionv2.domain.common.UseCaseFacialAnalyser
import com.tarlanus.facerecognitionv2.presentation.resultscreen.state.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun setImageLoaded(path: String, name: String) {
        _uiState.value = ResultState.IDLE

        _uiState.value = ResultState.IMAGELOADED(path, name)
    }

}