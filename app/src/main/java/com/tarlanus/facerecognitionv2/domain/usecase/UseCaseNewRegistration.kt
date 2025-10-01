package com.tarlanus.facerecognitionv2.domain.usecase

import com.tarlanus.facerecognitionv2.data.local.UserFaces
import com.tarlanus.facerecognitionv2.domain.repositories.RoomRepository
import javax.inject.Inject

class UseCaseNewRegistration @Inject constructor(private val roomDao : RoomRepository) {


    suspend operator fun  invoke(newCandidate : UserFaces) {
        roomDao.insertNewCandidate(newCandidate)
    }
}