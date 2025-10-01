package com.tarlanus.facerecognitionv2.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userFaces")
data class UserFaces(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id : Int,
    @ColumnInfo("candidateName") val candidateName : String,
    @ColumnInfo("imagePath") val imagePath : String

)