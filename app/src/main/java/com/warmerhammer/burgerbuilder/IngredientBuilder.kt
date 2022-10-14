package com.warmerhammer.burgerbuilder

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.warmerhammer.burgerbuilder.Ingredients.Ingredient
import androidx.lifecycle.viewmodel.compose.viewModel
import com.warmerhammer.burgerbuilder.ui.theme.DinerUndecided

val ingredientList =
    listOf(
        Ingredient("Lettuce", R.drawable.lettuce, 1.00f),
        Ingredient("Bacon", R.drawable.bacon, 2.00f),
        Ingredient("Cheese", R.drawable.american_cheese, 1.00f),
        Ingredient("Patty", R.drawable.burger_meat, 5.00f),
        Ingredient("Fried Egg", R.drawable.fried_egg, 2.00f),
        Ingredient("Ketchup", R.drawable.ketchup, .50f),
        Ingredient("Mustard", R.drawable.mustard, .50f),
        Ingredient("Onion Rings", R.drawable.onion_rings, 1.00f),
        Ingredient("Pickles", R.drawable.pickles, .75f),
        Ingredient("Sliced Onions", R.drawable.sliced_onions, .75f),
        Ingredient("Tomatoes", R.drawable.tomatoes, .75f)
    )

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun IngredientBuilder(
    viewModel: MainActivityViewModel = viewModel()
) {
    var offset by remember { mutableStateOf(0f) }
    val quantityMap by viewModel.quantityMap.collectAsState()

    LazyColumn(
        modifier = Modifier
            .scrollable(
                orientation = Orientation.Vertical,
                state = rememberScrollableState { delta ->
                    offset += delta
                    delta
                }
            )
            .background(color = DinerUndecided)
    ) {
        items(ingredientList.size) { idx ->
            val ingredient = ingredientList[idx]
            var quantity by rememberSaveable (quantityMap) { mutableStateOf(quantityMap[ingredient.name] ?: 0) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 2.dp)
                    .height(40.dp)
            ) {
                Text(ingredient.name, modifier = Modifier.weight(.08f))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .border(1.dp, Color.Black)
                        .weight(.05f)
                ) {
                    Text(text = quantity.toString())
                }

                Spacer(Modifier.weight(.1f))

                Button(
                    onClick = {
                        if (quantity > 0) {
                            quantity --
                            viewModel.removeIngredient(ingredient)
                        }
                    },
                    modifier = Modifier
                        .weight(.07f)
                        .padding(end = 5.dp)
                        .size(25.dp)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = null)
                }

                Button(
                    onClick = {
                        quantity ++
                        viewModel.addIngredient(ingredient)
                    },
                    modifier = Modifier
                        .weight(.07f)
                        .padding(start = 5.dp)
                        .size(25.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, Modifier.size(30.dp))
                }

            }
            Divider(color = MaterialTheme.colors.background)
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun IngredientBuilderPreview() {
//    IngredientBuilder()
//}