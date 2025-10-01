package com.tarlanus.facerecognitionv2.presentation.drawer

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu

import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.tarlanus.facerecognitionv2.domain.models.UserDetails
import com.tarlanus.facerecognitionv2.presentation.drawer.viewmodel.ViewModelDrawer
import com.tarlanus.facerecognitionv2.presentation.register.screen.RegisterScreen
import com.tarlanus.facerecognitionv2.presentation.resultscreen.screen.ResultScreen
import com.tarlanus.facerecognitionv2.ui.theme.Accent
import com.tarlanus.facerecognitionv2.utils.NavDestinations
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerScreen(viewModelDrawer: ViewModelDrawer = hiltViewModel()) {

    val activity = LocalActivity.current as? ComponentActivity
    val context = LocalContext.current
    val controller = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val registeredUsers = viewModelDrawer.registeredUsers.collectAsStateWithLifecycle()



    LaunchedEffect(drawerState.targetValue) {
        Log.e("getDrawerState", "state ${drawerState.targetValue}")
        if (drawerState.targetValue == DrawerValue.Open) {
            Log.e("getDrawerState", "isopen ${drawerState.targetValue}")

            viewModelDrawer.getRegisteredUsers()
            viewModelDrawer.showToast.collect { message ->
                if (message.isNotEmpty()) {

                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

                }
            }
        }
    }



    val selectedIndex = remember { mutableIntStateOf(-1) }
    DismissibleNavigationDrawer(

        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet(
                drawerState = drawerState, modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp),
                drawerContainerColor = Accent.copy(alpha = 0.15f)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    IconButton(
                        onClick = {
                            scope.launch {


                                val navOps = NavOptions.Builder().setRestoreState(true).setLaunchSingleTop(true).setPopUpTo(
                                    NavDestinations.RegisterScreen.route, false).build()

                                controller.navigate(NavDestinations.RegisterScreen.route, navOps)

                                delay(50)
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.size(100.dp),
                    ) {

                        Icon(
                            contentDescription = "Test the new image",
                            imageVector = Icons.Default.AddCircle,
                            modifier = Modifier.fillMaxSize(),

                            tint = Color.White
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Accent)
                    )

                    Spacer(modifier = Modifier.height(20.dp))



                    LazyColumn {
                        itemsIndexed(items = registeredUsers.value, key = {index, user -> user.id}) { index, user ->
                            val name = user.candidateName
                            NavigationDrawerItem(
                                selected = selectedIndex.value == index,
                                onClick = {
                                    scope.launch {
                                        val targetRoute = NavDestinations.ResultScreen.route
                                        selectedIndex.value = index
                                        val jsonData = Gson().toJson(user)
                                        val navOps = NavOptions.Builder().setRestoreState(true).setLaunchSingleTop(true).setPopUpTo(
                                            NavDestinations.RegisterScreen.route, false).build()
                                        val encodedJson = Uri.encode(jsonData)
                                        controller.navigate("$targetRoute/$encodedJson", navOps)


                                        delay(50)

                                        drawerState.close()
                                    }
                                },
                                label = { Text(name) },
                                modifier = Modifier.padding(10.dp),
                                shape = RoundedCornerShape(5.dp),
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = Accent,
                                    unselectedContainerColor = Accent.copy(0.7f)
                                )
                            )
                        }

                  }



                }
            }

        }) {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Accent.copy(alpha = 0.4f)),
                    title = { "FaceRecognition with Azure" },
                    navigationIcon = {
                        Icon(
                            contentDescription = "Open the Drawer",
                            imageVector = Icons.Default.Menu,
                            modifier = Modifier.clickable {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                    },
                )
            },
        ) {


            NavHost(navController = controller, modifier = Modifier.fillMaxSize().padding(it), startDestination = NavDestinations.RegisterScreen.route) {
                composable(route = NavDestinations.RegisterScreen.route) {
                    RegisterScreen(controller)
                }
                val resultRoute = NavDestinations.ResultScreen.route

                composable(route = "$resultRoute/{userDetails}", arguments = listOf(
                    navArgument(name = "userDetails") {type = NavType.StringType},
                )) {
                    val getuserDetails = it.arguments?.getString("userDetails") ?: ""
                    val decodedUserDetails = Uri.decode(getuserDetails)
                    val getuserDetailstoData = Gson().fromJson(decodedUserDetails, UserDetails::class.java)

                    ResultScreen(getuserDetailstoData)
                }
            }
            Log.e("drawerValues", "currentOffset ${drawerState.currentOffset}")
            Log.e("drawerValues", "targetValue ${drawerState.targetValue}")

            val fraction = (abs(drawerState.currentOffset) / 525f).coerceIn(0f, 1f)
            val setAlpha = (1f - fraction) * 0.5f

            if (abs(drawerState.currentOffset) < 525) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(alpha = setAlpha))
                        .pointerInput(Unit) {
                            detectTapGestures {
                                scope.launch { drawerState.close() }
                            }
                        }
                )
            }




        }


    }

    BackHandler {
        when {
            drawerState.isOpen -> {
                scope.launch { drawerState.close() }
            }
            controller.previousBackStackEntry != null -> {
                controller.popBackStack()
            }
            else -> {
                activity?.finishAffinity()
            }
        }
    }

}


@Composable
@Preview(showBackground = true)
fun PreviewDrawerScreen() {
    DrawerScreen()

}
