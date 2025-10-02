package com.tarlanus.facerecognitionv2.presentation.camera.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionSelector.PREFER_HIGHER_RESOLUTION_OVER_CAPTURE_RATE
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarlanus.facerecognitionv2.domain.common.UseCaseCaptureImage
import com.tarlanus.facerecognitionv2.domain.common.UseCaseUritoImage
import com.tarlanus.facerecognitionv2.utils.Constants.keyManifestCamera
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class ViewModelCamera @Inject constructor(
    private val uritoImage: UseCaseUritoImage,
    private val useCaseCaptureImage: UseCaseCaptureImage,
    @ApplicationContext private val context: Context
) : ViewModel() {


    private var camera: Camera? = null
    private val _cameraSelector = MutableStateFlow(CameraSelector.DEFAULT_FRONT_CAMERA)

    private var cameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null
    private lateinit var cameraExecutor: ExecutorService
    private var jobCamera: Job? = null
    private var imageCapture: ImageCapture? = null

    fun initializeCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView
    ) {
        val exhandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("getThrowAble", throwable.message.toString())

        }
        jobCamera?.cancel()

        jobCamera = viewModelScope.launch(exhandler) {
            cameraExecutor = Executors.newSingleThreadExecutor()
            cameraProvider = ProcessCameraProvider.getInstance(context).get()
            setupCamera(lifecycleOwner, previewView, context)

        }

    }

    private fun setupCamera(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        context: Context
    ) {
        cameraProvider?.let { provider ->
            provider.unbindAll()
            val resolutionSelector = ResolutionSelector.Builder()
                .setAllowedResolutionMode(PREFER_HIGHER_RESOLUTION_OVER_CAPTURE_RATE)
                .setResolutionStrategy(ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY)
                .setAspectRatioStrategy(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
                .build()
            preview = Preview.Builder()

                .setResolutionSelector(resolutionSelector)
                .build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)

                }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()
            try {

                camera = provider.bindToLifecycle(
                    lifecycleOwner,
                    _cameraSelector.value,
                    preview,
                    imageCapture

                )
                camera?.cameraControl?.setZoomRatio(1.1f)
            } catch (e: Exception) {
                Log.e("onErrorWhileSetup", e.message.toString())

            }
        }
    }

    fun setonCleared() {
        jobCamera?.cancel()
        cameraExecutor.shutdown()

    }


    fun checkCameraPermission(): Boolean {
        val checkPermission = ContextCompat.checkSelfPermission(
            context,
            keyManifestCamera
        ) == PackageManager.PERMISSION_GRANTED
        return checkPermission
    }


    fun captureImage(onResult : (String?) -> Unit) {
        var getPath : String? = null

        useCaseCaptureImage.captureImage(context, imageCapture) {  path ->
            if (path != null) {
                Log.e("getUriSaved", "Captured path: $path")
                getPath = path
                onResult(getPath)
            } else {
                Log.e("getUriSaved", "Capture failed")
                getPath = null
                onResult(getPath)
            }
        }





    }
}
