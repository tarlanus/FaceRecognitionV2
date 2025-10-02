package com.tarlanus.facerecognitionv2.data.repository

import com.tarlanus.facerecognitionv2.data.remote.RetrofitDaoInterface
import com.tarlanus.facerecognitionv2.data.remote.VerifyResponse
import com.tarlanus.facerecognitionv2.domain.repositories.RetrofitRepository
import okhttp3.MultipartBody
import retrofit2.Response

class RetrofitRepositoryImpl(val retrofitDao : RetrofitDaoInterface) : RetrofitRepository {
    override suspend fun compareFaces(
        img1: MultipartBody.Part,
        img2: MultipartBody.Part
    ): Response<VerifyResponse> {
        return retrofitDao.compareFaces(img1, img2)
    }
}