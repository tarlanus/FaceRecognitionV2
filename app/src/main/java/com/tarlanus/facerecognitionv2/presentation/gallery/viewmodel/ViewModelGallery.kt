package com.tarlanus.facerecognitionv2.presentation.gallery.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.tarlanus.facerecognitionv2.domain.common.UseCaseUritoImage
import com.tarlanus.facerecognitionv2.utils.Constants.keyManifestMedia33
import com.tarlanus.facerecognitionv2.utils.Constants.keyManifestReadExternal
import com.tarlanus.facerecognitionv2.utils.Constants.keyManifestWriteExternal
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ViewModelGallery @Inject constructor(@ApplicationContext private val context: Context, private val useCaseUritoImage: UseCaseUritoImage) :
    ViewModel() {






    fun checkGallery(): Boolean {
        val buildVersion = Build.VERSION.SDK_INT
        return when {
            buildVersion >= 33 -> {
                ContextCompat.checkSelfPermission(
                    context,
                    keyManifestMedia33
                ) == PackageManager.PERMISSION_GRANTED
            }
            buildVersion > 28 -> {
                ContextCompat.checkSelfPermission(
                    context,
                    keyManifestReadExternal
                ) == PackageManager.PERMISSION_GRANTED
            }
            else -> {
                ContextCompat.checkSelfPermission(
                    context,
                    keyManifestWriteExternal
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    fun getPermission(): String {
        val buildVersion = Build.VERSION.SDK_INT
        return when {
            buildVersion >= 33 -> {
                keyManifestMedia33
            }
            buildVersion > 28 -> {
                keyManifestReadExternal
            }
            else -> {
                keyManifestWriteExternal
            }
        }
    }

    fun setUriLikePath(getUri: Uri)  : String?{
        Log.e("getImagePath", "$getUri")

        val pathOfImage = useCaseUritoImage.getPath(context, getUri)
        Log.e("getImagePath", "$pathOfImage")

        return pathOfImage

    }
}