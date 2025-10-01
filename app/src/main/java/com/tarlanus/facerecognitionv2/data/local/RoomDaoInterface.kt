package com.tarlanus.facerecognitionv2.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface RoomDaoInterface {

    @Insert
    suspend fun insertNewCandidate(details : UserFaces)

    @Delete
    suspend fun removeexistingCandidate(details : UserFaces)

    @Query("SELECT * FROM userFaces")
    suspend fun getAllUserFaces() : List<UserFaces>?

}