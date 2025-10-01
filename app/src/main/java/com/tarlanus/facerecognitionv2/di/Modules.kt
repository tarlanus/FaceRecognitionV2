package com.tarlanus.facerecognitionv2.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tarlanus.facerecognitionv2.data.local.RoomBuilder
import com.tarlanus.facerecognitionv2.data.repository.RoomRepositoryImpl
import com.tarlanus.facerecognitionv2.domain.repositories.RoomRepository
import com.tarlanus.facerecognitionv2.utils.Constants.dbname
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Modules {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context) : RoomBuilder {
        val room = Room.databaseBuilder(context, RoomBuilder::class.java, dbname).setJournalMode(
            RoomDatabase.JournalMode.TRUNCATE)
            .build()
        return room
    }
    @Provides
    @Singleton
    fun provideRoomDao(roomBuilder : RoomBuilder) : RoomRepository {
        return RoomRepositoryImpl(roomBuilder.getLocalDao())
    }


}