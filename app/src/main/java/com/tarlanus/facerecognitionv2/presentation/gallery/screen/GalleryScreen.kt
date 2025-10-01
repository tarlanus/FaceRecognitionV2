package com.tarlanus.facerecognitionv2.presentation.gallery.screen

import android.content.Intent
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tarlanus.facerecognitionv2.presentation.gallery.viewmodel.ViewModelGallery
import com.tarlanus.facerecognitionv2.ui.theme.Accent

@Composable
fun GalleryScreen(viewModelGallery: ViewModelGallery = hiltViewModel(), onClose :(Any?) ->Unit) {
    val act = LocalActivity.current

    val permLauncherGallery = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (!it) {
            onClose(null)
        }

    }
    val activityUriResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        val  data = it.data?.data
        Log.e("getImage", "$data")
        if (data != null) {
           val getPath = viewModelGallery.setUriLikePath(data)
            onClose(getPath)
        }
    }
    LaunchedEffect(Unit) {

        val isGalleryGranted = viewModelGallery.checkCameraPermission()
        if (!isGalleryGranted) {
            permLauncherGallery.launch(viewModelGallery.getPermission())
        }

    }


    Card(modifier = Modifier
        .fillMaxSize()
        .padding(50.dp), shape = RoundedCornerShape(15.dp)) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Button(onClick = {
                val intent = Intent(Intent.ACTION_PICK, null)
                intent.type = "image/*"

                activityUriResult.launch(intent)


            },colors = ButtonDefaults.buttonColors(
                containerColor = Accent.copy(alpha = 0.3f),
                contentColor = Color.Blue
            )) {
                Text(text = "Choose")
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun PreviewGalleryScreen() {
    GalleryScreen() {
    }


}
