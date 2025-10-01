package com.tarlanus.facerecognitionv2.presentation.drawer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarlanus.facerecognitionv2.domain.models.UserDetails
import com.tarlanus.facerecognitionv2.domain.usecase.UseCaseGetAllRegisteredUsers
import com.tarlanus.facerecognitionv2.toListUserDetails
import com.tarlanus.facerecognitionv2.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ViewModelDrawer @Inject constructor(private val useCaseGetAllRegisteredUsers: UseCaseGetAllRegisteredUsers) : ViewModel() {
    private var getUsersJob: Job? = null
    private val _showToast : MutableSharedFlow<String> = MutableSharedFlow(0)
    val showToast get() = _showToast.asSharedFlow()
    private val _registeredUsers : MutableStateFlow<List<UserDetails>> = MutableStateFlow(emptyList())
    val registeredUsers get() = _registeredUsers.asStateFlow()
    private val controllerUserDetails : MutableList<UserDetails> = mutableListOf()

    init {
        getRegisteredUsers()
    }

    fun getRegisteredUsers() {
        getUsersJob?.cancel()
        getUsersJob =useCaseGetAllRegisteredUsers.invoke()
            .onEach { result ->
                Log.e("getResultUsers", "$result result")

                when(result) {
                    is Result.IDLE -> {

                    }

                    is Result.ERROR -> {
                        _showToast.emit(result.error ?: "")

                    }
                    is Result.SUCCESS -> {
                        val data = result.success
                        if (data != null) {
                            controllerUserDetails.clear()


                            controllerUserDetails.addAll(data.toListUserDetails())

                            _registeredUsers.value = controllerUserDetails.toList()
                            Log.e("getResultUsers", _registeredUsers.value.toString())

                        } else {
                            _showToast.emit("Not users found yet.")

                        }
                    }
                }

            }

            .launchIn(viewModelScope)
    }


    override fun onCleared() {
        super.onCleared()
        getUsersJob?.cancel()
    }


}