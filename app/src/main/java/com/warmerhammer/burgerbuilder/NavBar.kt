package com.warmerhammer.burgerbuilder

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.warmerhammer.burgerbuilder.ui.theme.DinerUndecided
import kotlin.math.sign

@Composable
fun NavBar(
    viewModel: MainActivityViewModel = viewModel(),
    navController: NavController,
    onBurger: () -> Unit,
    signIn: () -> Unit,
    onHistory: () -> Unit,
    noHistory: () -> Unit
) {

    val signedIn by viewModel.signedIn.collectAsState()
    val orderHistory = viewModel.orderHistory.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Log.i("NavBar.kt", "${navBackStackEntry?.destination?.route}")

    Surface(
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary)
                .padding(PaddingValues(horizontal = 10.dp))
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(.3f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.burger_icon),
                    contentDescription = "Burger Icon",
                    tint = Color.DarkGray,
                    modifier = Modifier
                        .size(20.dp)
                )

                TextButton(
                    onClick = {
                        when (orderHistory.value.isEmpty()) {
                            true -> {
                                noHistory()
                            }

                            false -> {
                                onHistory()
                            }
                        }
                    },
                ) {
                    val historyButtonColor =
                        if (navBackStackEntry?.destination?.route == "OrderHistory.kt")
                            MaterialTheme.colors.primary
                        else
                            MaterialTheme.colors.background


                    if (orderHistory.value.isNotEmpty()) {
                        Text("Order History", fontSize = 12.sp, color = historyButtonColor)
                    } else {
                        Text("Order History", fontSize = 12.sp, color = Color.LightGray)
                    }
                }
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.weight(.5f)
            ) {
                TextButton(onClick = onBurger) {
                    if (navBackStackEntry?.destination?.route == "Homescreen.kt?screen={screen}")
                        Text("Burger Builder", color = MaterialTheme.colors.primary)
                    else
                        Text("Burger Builder", color = MaterialTheme.colors.background)

                }
                TextButton(
                    onClick = {
                        if (signedIn) {
                            viewModel.signOut()
                            onBurger()
                        } else {
                            signIn()
                        }
                    }) {
                    val buttonColor =
                        if (navBackStackEntry?.destination?.route == "SignInScreen.kt/{destination}")
                            MaterialTheme.colors.primary
                        else
                            MaterialTheme.colors.background

                    if (signedIn) {
                        Text("Sign Out", color = buttonColor)
                    } else {
                        Text("Sign In", color = buttonColor)
                    }
                }
            }

        }

    }

}


//@Preview
//@Composable
//fun NavBarPreview() {
//    NavBar()
//}