package com.tarlanus.facerecognitionv2.domain.repositories


import com.tarlanus.facerecognitionv2.data.local.UserFaces

interface RoomRepository {

    suspend fun insertNewCandidate(details : UserFaces)

    suspend fun removeexistingCandidate(details : UserFaces)

    suspend fun getAllUserFaces() : List<UserFaces>?


}