package com.warmerhammer.burgerbuilder

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.warmerhammer.burgerbuilder.ui.theme.BurgerBuilderTheme
import kotlinx.coroutines.launch
import kotlin.math.sign

class MainActivity : ComponentActivity() {
    private val viewModel = MainActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BurgerBuilderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Screen(viewModel)
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Screen(
    viewModel : MainActivityViewModel = viewModel()
) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val systemUiController : SystemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = true

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = {NavBar(
            viewModel,
            navController,
            onBurger = {
                navController.navigate("Homescreen.kt?screen=IngredientBuilder")
            },
            signIn = {
                navController.navigate("SignInScreen.kt/Homescreen.kt")
            },
            onHistory = {
                navController.navigate("OrderHistory.kt")
            },
            noHistory = {
                coroutineScope.launch {
                    val snackbarResult =
                    scaffoldState.snackbarHostState.showSnackbar(
                        "There are no orders in your history.",
                        "Okay"
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {}
                    }
                }
            }
        ) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "Homescreen.kt?screen={screen}",
            modifier = Modifier.padding(padding)
        ) {
            composable(
                route = "Homescreen.kt?screen={screen}",
                arguments = listOf(navArgument("screen"){ defaultValue = "IngredientBuilder" })
            ) { backStackEntry ->

                Homescreen(
                    viewModel,
                    backStackEntry.arguments?.getString("screen"),
                    onSignIn = {
                       navController.navigate("SignInScreen.kt/ContactScreen.kt")
                    },
                    onHistory = { navController.navigate("OrderHistory.kt") },
                    onDelivery = { navController.navigate("Homescreen.kt") }
                )
            }

            composable(
                route = "SignInScreen.kt/{destination}",
                arguments = listOf(navArgument("destination") {type = NavType.StringType})
            ){ backStackEntry ->

                SignInScreen(backStackEntry.arguments?.getString("destination")) { destination ->
                    viewModel.signIn()
                    if (
                        destination == "Homescreen.kt"
                    ) {
                        navController.navigate("Homescreen.kt")
                    } else if (destination == "ContactScreen.kt") {
                        navController.navigate("Homescreen.kt?screen=ContactScreen")
                    }
                }
            }

            composable(
                route = "OrderHistory.kt"
            ) {
                OrderHistory(viewModel)
            }
        }
    }



}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    BurgerBuilderTheme {
//        Screen()
//    }
//}