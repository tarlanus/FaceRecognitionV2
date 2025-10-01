package com.tarlanus.facerecognizerazure.presentation.register.screen

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.tarlanus.facerecognizerazure.presentation.camera.screen.CameraScreen
import com.tarlanus.facerecognizerazure.presentation.gallery.screen.GalleryScreen
import com.tarlanus.facerecognizerazure.presentation.register.state.RegisterState
import com.tarlanus.facerecognizerazure.presentation.register.viewmodel.ViewModelRegister
import com.tarlanus.facerecognizerazure.ui.theme.Accent

@Composable
fun RegisterScreen(
    controller1: NavHostController,
    viewModelRegister: ViewModelRegister = hiltViewModel(),
) {
    val tf = remember { mutableStateOf("") }
    val isCameraSelected = remember { mutableStateOf(false) }
    val isGallerySelected = remember { mutableStateOf(false) }
    val uiState = viewModelRegister.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        viewModelRegister.restoreState()
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            val state =uiState.value
            when(state) {

                is RegisterState.ERROR -> {

                    Column(modifier = Modifier.height(350.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Error,modifier = Modifier.size(150.dp),
                            tint = Red, contentDescription = "Registering is unsuccesfull."
                        )
                        Text(state.errorMsg, color = Black, textAlign = TextAlign.Center, fontSize = 18.sp)
                    }

                }
                RegisterState.IDLE -> {
                    Spacer(modifier = Modifier.height(350.dp))

                }
                RegisterState.ISREGISTERING -> {
                    Box(modifier = Modifier.height(350.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(150.dp),
                            color = Accent.copy(alpha = 0.8f),
                        )
                    }

                }
                RegisterState.SUCCESS -> {
                    Column(modifier = Modifier.height(350.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Done,modifier = Modifier.size(150.dp),
                            tint = Accent.copy(alpha = 0.8f), contentDescription = "Registering is succesfull."
                        )
                        Text("Success", color = Black, textAlign = TextAlign.Center, fontSize = 18.sp)

                    }
                }

                is RegisterState.IMAGESELECTED -> {
                    Column(modifier = Modifier.height(350.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            contentDescription = "Result of inputImage",
                            model = state.path,
                            modifier = Modifier.size(200.dp)
                        )
                        OutlinedTextField(
                            tf.value, onValueChange = {tf.value = it}, singleLine = true,
                            label = {
                                Text("Set name:", fontSize = 16.sp, textAlign = TextAlign.Center)
                            },
                            textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                        )

                        Button(
                            modifier = Modifier
                                .padding(15.dp),
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                viewModelRegister.startRegisterImage(state.path, tf.value)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Accent.copy(alpha = 0.3f),
                                contentColor = androidx.compose.ui.graphics.Color.Blue
                            ),
                        ) {
                            Text("Save register", fontSize = 16.sp, textAlign = TextAlign.Center)
                        }
                    }

                }
            }







            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

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
                    Text("Register with gallery", fontSize = 16.sp, textAlign = TextAlign.Center)
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
                    Text("Register with camera", fontSize = 16.sp, textAlign = TextAlign.Center)
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
                            viewModelRegister.setImageSelection(path)

                        }

                    }
                }
            }
            if (isGallerySelected.value) {

                Dialog(onDismissRequest = {
                    isGallerySelected.value = false
                }) {
                    GalleryScreen() {
                        isGallerySelected.value = false
                        val path = it as? String
                        if (path != null) {

                            viewModelRegister.setImageSelection(path)
                        }

                    }
                }
            }


        }

    }

}

@Composable
@Preview(showBackground = true)
fun PreviewRegisterScreen() {
    val controller = rememberNavController()
    RegisterScreen(controller)
}