package com.tarlanus.facerecognizerazure.utils

sealed class NavDestinations(val route : String) {
    object  RegisterScreen : NavDestinations(route = "RegisterScreen")
    object  ResultScreen : NavDestinations(route = "ResultScreen")

}