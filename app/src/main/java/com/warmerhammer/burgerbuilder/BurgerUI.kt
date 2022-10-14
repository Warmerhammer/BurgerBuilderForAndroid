package com.warmerhammer.burgerbuilder


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import com.warmerhammer.burgerbuilder.ui.theme.DinerUndecided
import kotlinx.coroutines.launch

@Composable
fun BurgerUI(
    viewModel: MainActivityViewModel = viewModel(),
    frontScreen: String?,
    onSignIn: () -> Unit,
    onOrder: () -> Unit,
    onHistory: () -> Unit,
    onDelivery: () -> Unit,
    onCancel: () -> Unit
) {
    val currentIngredients by viewModel.selectedIngredients.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val signedIn by viewModel.signedIn.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val contactIsValid by viewModel.validContactInfo.collectAsState()


    Scaffold(
        modifier = Modifier
            .padding(PaddingValues(vertical = 5.dp, horizontal = 5.dp))
            .border(1.dp, MaterialTheme.colors.primary, shape = MaterialTheme.shapes.medium),
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.background,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(vertical = 4.dp, horizontal = 10.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = {
                        when (frontScreen) {
                            "IngredientBuilder" -> {
                                viewModel.clearBurger()
                            }

                            "ContactScreen" -> onCancel()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    when (frontScreen) {
                        "IngredientBuilder" -> Text("CLEAR")
                        "ContactScreen" -> Text("CANCEL")
                    }
                }

                Button(
                    onClick = {

                        when (frontScreen) {
                            "IngredientBuilder" -> {
                                if (!signedIn) {
                                    onSignIn()
                                } else {
                                    onOrder()
                                }
                            }

                            "ContactScreen" -> {
                                coroutineScope.launch {
                                    val snackbarResult =
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            message = "Thank you for your order!",
                                            actionLabel = "View Order History"
                                        )

                                    viewModel.addOrder()
                                    viewModel.clearBurger()

                                    when (snackbarResult) {
                                        SnackbarResult.Dismissed -> {
                                            onDelivery()
                                        }
                                        SnackbarResult.ActionPerformed -> {
                                            onHistory()
                                        }
                                    }
                                }
                            }

                        }


                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    enabled = (
                                (frontScreen == "IngredientBuilder" && currentIngredients.size > 1)
                                || (frontScreen == "ContactScreen" && contactIsValid.values.all { it })
                            ),
                ) {
                    when (frontScreen) {
                        "IngredientBuilder" -> Text("ORDER")
                        "ContactScreen" -> Text("CONFIRM DELIVERY")
                    }
                }
            }
        },
        topBar = {
            val price by viewModel.price.collectAsState()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text(
                    text = "Total: $${String.format("%.2f", price)}",
                    textAlign = TextAlign.Start
                )
            }
        }
    ) { padding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        ) {
            /* Top bun */
            Icon(
                painter = painterResource(id = R.drawable.burger_bun_top),
                contentDescription = "Burger bun top",
                modifier = Modifier
                    .width(80.dp)
                    .height(30.dp)
                    .padding(bottom = 0.dp),
                tint = Color.Unspecified
            )

            if (currentIngredients.size <= 1) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Please insert ingredients!", fontSize = 12.sp)
            } else {

                LazyColumn(
                    Modifier
                        .padding(PaddingValues(vertical = .5.dp))
                        .heightIn(max = 150.dp)
                ) {
                    items(currentIngredients.size) { idx ->
                        val ingredient = currentIngredients[currentIngredients.lastIndex - idx]

                        Image(
                            painter = painterResource(ingredient.image),
                            contentDescription = ingredient.name,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .height(15.dp)
                                .width(60.dp)
                        )
                    }

                }

            }

            Icon(
                Icons.Default.ExpandMore,
                contentDescription = null
            )
        }

    }
}


//@Preview(showBackground = true)
//@Composable
//fun BurgerUIPreview() {
//    BurgerBuilderTheme {
//        BurgerUI()
//    }
//}