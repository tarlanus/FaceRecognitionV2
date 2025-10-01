package com.tarlanus.facerecognitionv2.domain.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UseCaseFacialAnalyser @Inject constructor(@ApplicationContext private val context : Context) {

    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    val detector = FaceDetection.getClient(options)

    fun analyze(imgPath: String, onResult: (Any?) -> Unit) {
        try {



            val bitmap = loadScaledBitmap(imgPath, 800, 800)

            if (bitmap != null) {
                val inputImage = InputImage.fromBitmap(bitmap, 0)


                detector.process(inputImage)
                    .addOnSuccessListener { faces ->
                        if (faces.isNotEmpty()) {
                            onResult(faces)
                            Log.e("FaceAnalyzer", "Detected ${faces.size} faces")
                        } else {
                            onResult(null)
                            Log.e("FaceAnalyzer", "No faces found")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("FaceAnalyzer", "Detection failed", e)
                        onResult(null)
                    }
            }


        } catch (e: Exception) {
            Log.e("FaceAnalyzer", "Error loading image", e)
            onResult(null)
        }
    }
    fun loadScaledBitmap(path: String, maxWidth: Int, maxHeight: Int): Bitmap? {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(path, options)


        var scale = 1
        while (options.outWidth / scale > maxWidth || options.outHeight / scale > maxHeight) {
            scale *= 2
        }

        val opts = BitmapFactory.Options().apply { inSampleSize = scale }
        return BitmapFactory.decodeFile(path, opts)
    }



}