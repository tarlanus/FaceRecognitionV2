package com.tarlanus.facerecognitionv2.presentation.resultscreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarlanus.facerecognitionv2.domain.common.UseCaseFacialAnalyser
import com.tarlanus.facerecognitionv2.domain.usecase.UseCaseCompareFaces
import com.tarlanus.facerecognitionv2.presentation.resultscreen.state.ResultState
import com.tarlanus.facerecognitionv2.toListUserDetails
import com.tarlanus.facerecognitionv2.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ViewModelResult @Inject constructor(private val useCaseFacialAnalyser: UseCaseFacialAnalyser, private val useaseCompareFace : UseCaseCompareFaces)  : ViewModel() {

    private val _uiState : MutableStateFlow<ResultState> = MutableStateFlow(ResultState.IDLE)
    val uiState  get() = _uiState.asStateFlow()

    private var currentPath : String = ""
    private var currentName = ""
    private var candidatePath : String = ""
    private var jobComparison : Job? = null


    fun startTestImage(path: String) {
        Log.e("getDataToRegister", "path $path")
            _uiState.value = ResultState.TESTING
            useCaseFacialAnalyser.analyze(path) {
                if (it != null) {


                    Log.e("getAnalyzedResult", "$it")
                    candidatePath = path
                   // _uiState.value = ResultState.SUCCESS("Same Person")
                    startComparison()

                } else {
                    _uiState.value = ResultState.ERROR("No contains the face")

                }
            }






    }

    private fun startComparison() {
        val img1 = File(currentPath)
        val img2 = File(candidatePath)

        jobComparison?.cancel()
        jobComparison = useaseCompareFace.invoke(img1, img2).onEach { result ->
            when(result) {
                is Result.IDLE -> {

                }

                is Result.ERROR -> {
                    _uiState.value = ResultState.ERROR(result.error ?:  "An error occured")


                }
                is Result.SUCCESS -> {
                    val data = result.success
                    if (data != null) {

                        val verified = data.verified

                        if (verified == true) {
                            _uiState.value = ResultState.SUCCESS("Same Person")
                        } else {

                            val stringBuilder = StringBuilder()
                            stringBuilder.append("Different candidates\n")
                            stringBuilder.append("Expected: $currentName")

                            _uiState.value = ResultState.ERROR(stringBuilder.toString())

                        }

                    } else {
                        _uiState.value = ResultState.ERROR("An error occured")

                    }
                }
            }
        }.launchIn(viewModelScope)

    }

    fun setImageLoaded(path: String, name: String) {
        _uiState.value = ResultState.IDLE

        _uiState.value = ResultState.IMAGELOADED(path, name)

        currentPath = ""
        currentPath = path
        currentName =""
        currentName = name
    }

    override fun onCleared() {
        super.onCleared()
        jobComparison?.cancel()
    }

}