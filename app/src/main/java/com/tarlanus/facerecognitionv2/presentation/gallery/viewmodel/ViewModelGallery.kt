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
import com.tarlanus.facerecognitionv2.utils.Constants.keyManifestMedia34
import com.tarlanus.facerecognitionv2.utils.Constants.keyManifestReadExternal
import com.tarlanus.facerecognitionv2.utils.Constants.keyManifestWriteExternal
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ViewModelGallery @Inject constructor(@ApplicationContext private val context: Context, private val useCaseUritoImage: UseCaseUritoImage) :
    ViewModel() {






    fun checkCameraPermission(): Boolean {
        val buildVersion = Build.VERSION.SDK_INT
        val permissionGallery = when {
            buildVersion >= 34 -> {
                keyManifestMedia34
            }

            buildVersion == 33 -> {
                keyManifestMedia33
            }

            buildVersion < 33 && buildVersion > 28 -> {
                keyManifestReadExternal
            }

            else -> {
                keyManifestWriteExternal
            }
        }
        val checkPermission = ContextCompat.checkSelfPermission(
            context,
            permissionGallery
        ) == PackageManager.PERMISSION_GRANTED
        return checkPermission




}
    fun getPermission() : String {
        val buildVersion = Build.VERSION.SDK_INT

        val permissionGallery = when {
            buildVersion >= 34 -> {
                keyManifestMedia34
            }

            buildVersion == 33 -> {
                keyManifestMedia33
            }

            buildVersion < 33 && buildVersion > 28 -> {
                keyManifestReadExternal
            }

            else -> {
                keyManifestWriteExternal
            }
        }
        return permissionGallery
    }

    fun setUriLikePath(getUri: Uri)  : String?{
        Log.e("getImagePath", "$getUri")

        val pathOfImage = useCaseUritoImage.getPath(context, getUri)
        Log.e("getImagePath", "$pathOfImage")

        return pathOfImage

    }
}