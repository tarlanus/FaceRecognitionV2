package com.tarlanus.facerecognitionv2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserFaces::class], version = 1, exportSchema = false)
abstract class RoomBuilder : RoomDatabase() {
    abstract  fun getLocalDao() :  RoomDaoInterface
}