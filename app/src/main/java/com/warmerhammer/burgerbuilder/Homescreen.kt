package com.warmerhammer.burgerbuilder

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.reflect.KProperty

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Homescreen(
    viewModel : MainActivityViewModel = viewModel(),
    frontLayerContent : String?,
    onSignIn: () -> Unit,
    onHistory: () -> Unit,
    onDelivery: () -> Unit
) {

    var frontScreen by rememberSaveable { mutableStateOf(frontLayerContent) }

    BackdropScaffold(
        scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
        frontLayerScrimColor = Color.Unspecified,
        frontLayerBackgroundColor = Color.Unspecified,
        backLayerBackgroundColor = Color.Unspecified,
        peekHeight = 300.dp,
        backLayerContent = {
            Box(
                Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.background)
                ,
                contentAlignment = Alignment.Center
            ) {
                BurgerUI(
                    viewModel,
                    frontScreen,
                    onSignIn = onSignIn,
                    onOrder = {
                        frontScreen = "ContactScreen"
                    },
                    onHistory = onHistory,
                    onDelivery = onDelivery,
                    onCancel = {frontScreen = "IngredientBuilder"}
                )
            }
        },
        frontLayerContent = {
            Surface(
                elevation = 2.dp,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 2.dp, end = 2.dp, bottom = 0.dp)
            ) {
                when (frontScreen) {
                    "IngredientBuilder" -> IngredientBuilder(viewModel)
                    "ContactScreen" -> ContactScreen(viewModel)
                    else -> Log.i("Homescreen.kt", "$frontLayerContent")
                }
            }
        },
        appBar = {}
    )
}