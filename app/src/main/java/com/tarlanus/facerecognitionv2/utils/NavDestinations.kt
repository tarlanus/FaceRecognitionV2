package com.tarlanus.facerecognitionv2.utils

sealed class NavDestinations(val route : String) {
    object  RegisterScreen : NavDestinations(route = "RegisterScreen")
    object  ResultScreen : NavDestinations(route = "ResultScreen")

}