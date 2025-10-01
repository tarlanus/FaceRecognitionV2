package com.tarlanus.facerecognizerazure.utils

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@OptIn(ExperimentalGetImage::class)
class FacialAnalyser @Inject constructor(@ApplicationContext private val context : Context)  : ImageAnalysis.Analyzer {
    private var isProcessing = false
    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()
    override fun analyze(imageProxy: ImageProxy) {
        if (isProcessing) {
            imageProxy.close()
            return
        }

        isProcessing = true

        val mediaImage = imageProxy.image

        if (mediaImage != null) {
            val proxybitmap = imageProxy.toBitmap()
            val detector = FaceDetection.getClient(options)
            val image = InputImage.fromBitmap(proxybitmap, 0)

            detector.process(image)
                .addOnSuccessListener { faces ->
                    if (faces.isNotEmpty()) {
                        val face = faces[0]
                        Log.e("getcameraFaces", "$face")
                    }
                } .addOnFailureListener { e ->
                    Log.e("getFaces", "onFailure: ${e.message}")
                    isProcessing = false
                }
                .addOnCompleteListener {
                    imageProxy.close()
                    isProcessing = false
                }

        } else {
            imageProxy.close()
            isProcessing = false
        }


    }



}