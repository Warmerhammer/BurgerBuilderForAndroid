package com.warmerhammer.burgerbuilder

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class Order(
    var ingredients: List<String>,
    var price: Float
)


@Composable
fun OrderHistory(
    viewModel: MainActivityViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val orderHistory by viewModel.orderHistory.collectAsState()

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        content = {
            items(orderHistory) { order ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.border(2.dp, color = MaterialTheme.colors.primary).padding(10.dp)
                ) {
                    Text("${orderHistory.indexOf(order) + 1}.", modifier = Modifier.weight(.33f))
                    Text(
                        "Ingredients : ${order.ingredients}",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(.33f)
                    )
                    Text(
                        "Total : $${String.format("%.2f", order.price)}",
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(.33f)
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}