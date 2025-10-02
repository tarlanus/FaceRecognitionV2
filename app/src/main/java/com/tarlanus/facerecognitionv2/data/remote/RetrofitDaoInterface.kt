package com.tarlanus.facerecognitionv2.data.remote
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RetrofitDaoInterface {
    @Multipart
    @POST("verify")
    suspend fun compareFaces(
        @Part img1: MultipartBody.Part,
        @Part img2: MultipartBody.Part
    ): Response<VerifyResponse>
}