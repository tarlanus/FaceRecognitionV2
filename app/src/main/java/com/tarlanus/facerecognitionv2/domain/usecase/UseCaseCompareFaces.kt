package com.tarlanus.facerecognitionv2.domain.usecase

import com.tarlanus.facerecognitionv2.data.local.UserFaces
import com.tarlanus.facerecognitionv2.domain.models.ComparisonDetails
import com.tarlanus.facerecognitionv2.domain.repositories.RetrofitRepository
import com.tarlanus.facerecognitionv2.tocomparison
import com.tarlanus.facerecognitionv2.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UseCaseCompareFaces @Inject constructor(private val retrofitRepository: RetrofitRepository) {

    operator fun invoke(
        img1File: File,
        img2File: File
    ): Flow<Result<ComparisonDetails?>> = flow {
        val img1Part = MultipartBody.Part.createFormData(
            "img1", img1File.name, img1File.asRequestBody("image/*".toMediaTypeOrNull())
        )
        val img2Part = MultipartBody.Part.createFormData(
            "img2", img2File.name, img2File.asRequestBody("image/*".toMediaTypeOrNull())
        )
        emit(Result.IDLE())


        val getData = retrofitRepository.compareFaces(img1Part, img2Part)
        if (getData.isSuccessful) {
            val data = getData.body()?.tocomparison()
            emit(Result.SUCCESS(data = data))

        } else {
            val msg = getData.errorBody()?.string() ?: "An error occured!"
            emit(Result.ERROR(msg = msg))

        }


    }.flowOn(Dispatchers.IO)
        .catch {
            emit(Result.ERROR(msg = "Not found"))

        }
}