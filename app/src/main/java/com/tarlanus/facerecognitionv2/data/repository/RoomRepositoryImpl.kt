package com.tarlanus.facerecognitionv2.data.repository

import com.tarlanus.facerecognitionv2.data.local.RoomDaoInterface
import com.tarlanus.facerecognitionv2.data.local.UserFaces
import com.tarlanus.facerecognitionv2.domain.repositories.RoomRepository
import jakarta.inject.Inject

class RoomRepositoryImpl @Inject constructor(private val actualRepo : RoomDaoInterface)  : RoomRepository{
    override suspend fun insertNewCandidate(details: UserFaces) {
        actualRepo.insertNewCandidate(details)

    }

    override suspend fun removeexistingCandidate(details: UserFaces) {
        actualRepo.removeexistingCandidate(details)

    }

    override suspend fun getAllUserFaces(): List<UserFaces>? {
       return actualRepo.getAllUserFaces()
    }
}