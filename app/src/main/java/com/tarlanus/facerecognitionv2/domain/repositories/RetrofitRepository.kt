package com.tarlanus.facerecognitionv2.domain.repositories

import com.tarlanus.facerecognitionv2.data.remote.VerifyResponse
import okhttp3.MultipartBody
import retrofit2.Response

interface RetrofitRepository {
    suspend fun compareFaces(
         img1: MultipartBody.Part,
         img2: MultipartBody.Part
    ): Response<VerifyResponse>
}