package com.tarlanus.facerecognitionv2.presentation.camera.screen

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.tarlanus.facerecognitionv2.presentation.camera.viewmodel.ViewModelCamera
import com.tarlanus.facerecognitionv2.ui.theme.Accent
import com.tarlanus.facerecognitionv2.utils.Constants.keyManifestCamera

@Composable
fun CameraScreen(viewModelCamera : ViewModelCamera? = hiltViewModel(), onClose: (Any?) -> Unit) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current


    val scope = rememberCoroutineScope()

    val previewView = remember {
        PreviewView(context).apply {
            this.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    val permissionCamera =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                viewModelCamera?.initializeCamera(context, lifecycleOwner, previewView)

            } else {
                onClose(null)
            }
        }

    LaunchedEffect(Unit) {
        val checkCameraPermission = viewModelCamera?.checkCameraPermission()
        if (checkCameraPermission == true) {
            viewModelCamera.initializeCamera(context, lifecycleOwner, previewView)
        } else {
            permissionCamera.launch(keyManifestCamera)
        }
    }





    Card(modifier = Modifier
        .fillMaxSize()
        .padding(50.dp), shape = RoundedCornerShape(15.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(15.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            AndroidView(factory = {previewView}, modifier = Modifier.fillMaxSize() .weight(1f))






            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "",
                    color = Color.Black,
                    fontSize = 18.sp
                )

                Button(onClick = {
                  viewModelCamera?.captureImage() {
                      onClose(it)
                      Log.e("getCapturation", "$it")

                  }
                },colors = ButtonDefaults.buttonColors(
                    containerColor = Accent.copy(alpha = 0.3f),
                    contentColor = Color.Blue
                )) {
                    Text(text = "Capture")
                }
            }




        }


    }
    DisposableEffect(key1 = true) {
        onDispose {
            viewModelCamera?.setonCleared()
        }
    }

}


@Composable
@Preview(showBackground = true)
fun PreviewCameraScreen() {

    CameraScreen(null) {

    }

}
