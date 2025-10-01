package com.tarlanus.facerecognizerazure.presentation.resultscreen.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.tarlanus.facerecognizerazure.R
import com.tarlanus.facerecognizerazure.presentation.camera.screen.CameraScreen
import com.tarlanus.facerecognizerazure.presentation.gallery.screen.GalleryScreen
import com.tarlanus.facerecognizerazure.presentation.resultscreen.state.ResultState
import com.tarlanus.facerecognizerazure.presentation.resultscreen.viewmodel.ViewModelResult
import com.tarlanus.facerecognizerazure.ui.theme.Accent

@Composable
fun ResultScreen(viewModelResult: ViewModelResult = hiltViewModel()) {
    val isCameraSelected = remember { mutableStateOf(false) }
    val isGallerySelected = remember { mutableStateOf(false) }
    val uiState = viewModelResult.uiState.collectAsStateWithLifecycle()
    val state = uiState.value
    LaunchedEffect(Unit) {
        viewModelResult.setImageLoaded("")
    }


    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {


            when(state) {
                is ResultState.ERROR -> {
                    Column(modifier = Modifier.height(350.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Error,modifier = Modifier.size(150.dp),
                            tint = Red, contentDescription = "Registering is unsuccesfull."
                        )
                        Text(state.msg, color = Black, textAlign = TextAlign.Center, fontSize = 18.sp)
                    }
                }
                ResultState.IDLE -> {
                    Spacer(modifier = Modifier.height(350.dp))

                }
                is ResultState.IMAGELOADED -> {
                    Column(modifier = Modifier.height(350.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            contentDescription = "Result of inputImage",
                            model = R.drawable.ic_launcher_foreground,
                            modifier = Modifier.size(250.dp)

                        )
                        Text("User Details", fontSize = 18.sp, textAlign = TextAlign.Center, modifier = Modifier.height(150.dp).padding(horizontal = 10.dp))

                    }

                }
                is ResultState.SUCCESS -> {
                    Column(modifier = Modifier.height(350.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Done,modifier = Modifier.size(150.dp),
                            tint = Accent.copy(alpha = 0.8f), contentDescription = "Registering is succesfull."
                        )
                        Text("Success ${state.msg}", color = Black, textAlign = TextAlign.Center, fontSize = 18.sp)

                    }
                }
                ResultState.TESTING -> {
                    Box(modifier = Modifier.height(350.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(150.dp),
                            color = Accent.copy(alpha = 0.8f),
                        )
                    }

                }

            }






            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {

                Button(
                    modifier = Modifier
                        .height(130.dp)
                        .padding(15.dp),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        isGallerySelected.value = true

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent.copy(alpha = 0.3f),
                        contentColor = androidx.compose.ui.graphics.Color.Blue
                    ),
                ) {
                    Text("Test with gallery", fontSize = 16.sp, textAlign = TextAlign.Center)
                }

                Button(
                    modifier = Modifier
                        .height(130.dp)
                        .padding(15.dp),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        isCameraSelected.value = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent.copy(alpha = 0.3f),
                        contentColor = androidx.compose.ui.graphics.Color.Blue
                    ),
                ) {
                    Text("Test with camera", fontSize = 16.sp,  textAlign = TextAlign.Center)
                }

            }



            if (isCameraSelected.value) {

                Dialog(onDismissRequest = {
                    isCameraSelected.value = false
                }) {
                    CameraScreen() {
                        isCameraSelected.value = false
                        val path = it as? String
                        if (path != null) {

                            viewModelResult.startTestImage(path)
                        }

                    }
                }
            }
            if (isGallerySelected.value) {

                Dialog(onDismissRequest = {
                    isGallerySelected.value = false
                }) {
                    GalleryScreen()  {
                        isGallerySelected.value = false
                        val path = it as? String
                        if (path != null) {

                            viewModelResult.startTestImage(path)
                        }

                    }
                }
            }


        }

    }

}

@Composable
@Preview(showBackground = true)
fun PreviewResultScreen() {
    ResultScreen()
}