package com.tarlanus.facerecognitionv2.domain.usecase

import com.tarlanus.facerecognitionv2.data.local.UserFaces
import com.tarlanus.facerecognitionv2.domain.repositories.RoomRepository
import com.tarlanus.facerecognitionv2.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UseCaseGetAllRegisteredUsers @Inject constructor(private val roomDao : RoomRepository) {
     operator fun invoke() : Flow<Result<List<UserFaces>>> = flow {
        emit(Result.IDLE())


         val getData = roomDao.getAllUserFaces()

         if (!getData.isNullOrEmpty()) {
             emit(Result.SUCCESS(data = getData))
         } else {
             emit(Result.ERROR(msg = "No users found yet"))
         }


    }.flowOn(Dispatchers.IO)
         .catch {
             emit(Result.ERROR(msg = "Not found"))

         }
}