package com.tarlanus.facerecognitionv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tarlanus.facerecognitionv2.presentation.drawer.DrawerScreen
import com.tarlanus.facerecognitionv2.ui.theme.FaceRecognizerAzueTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FaceRecognizerAzueTheme {
                DrawerScreen()

            }
        }
    }
}

