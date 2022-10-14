package com.warmerhammer.burgerbuilder


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.warmerhammer.burgerbuilder.Ingredients.Ingredient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivityViewModel : ViewModel() {

    private val _selectedIngredients = MutableStateFlow<List<Ingredient>>(
        listOf(Ingredient("Bottom Bun", R.drawable.burger_bun_bottom, 0.00f))
    )
    val selectedIngredients: StateFlow<List<Ingredient>> = _selectedIngredients

    private val _price = MutableStateFlow(6.00f)
    val price: StateFlow<Float> = _price

    private val _signedIn = MutableStateFlow(false)
    val signedIn: StateFlow<Boolean> = _signedIn

    private val _orderHistory = MutableStateFlow<List<Order>>(emptyList())
    val orderHistory: StateFlow<List<Order>> = _orderHistory

    private val _quantityMap = MutableStateFlow<HashMap<String, Int>>(hashMapOf())
    val quantityMap: StateFlow<HashMap<String, Int>> = _quantityMap

    private val _validContactInfo = MutableStateFlow<Map<String, Boolean>>(hashMapOf())
    val validContactInfo: StateFlow<Map<String, Boolean>> = _validContactInfo

    fun clearBurger() {
        _selectedIngredients.value = listOf(
            Ingredient("Bottom Bun", R.drawable.burger_bun_bottom, 0.00f)
        )
        _price.value = 6.00f
        _quantityMap.value = hashMapOf()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun addIngredient(ingredient: Ingredient) {
        _selectedIngredients.value = selectedIngredients
            .value
            .toMutableList()
            .also {
                it.add(ingredient)
            }

        _price.value += ingredient.price

        _quantityMap.value[ingredient.name] =
            _quantityMap.value.getOrDefault(ingredient.name, 0) + 1
    }

    fun removeIngredient(ingredient: Ingredient) {
        _selectedIngredients.value = selectedIngredients
            .value
            .toMutableList()
            .also {
                it.remove(ingredient)
            }

        _price.value -= ingredient.price
        _quantityMap.value[ingredient.name] = _quantityMap.value[ingredient.name]!! - 1
    }

    fun signIn() {
        _signedIn.value = true
    }

    fun signOut() {
        _signedIn.value = false
        _orderHistory.value = emptyList()
    }

    fun addOrder() {

        val newOrder = Order(

            ingredients = selectedIngredients
                .value
                .map { it.name }
                .filter { it != "Bottom Bun" },

            price = price.value
        )

        _orderHistory.value = orderHistory
            .value
            .toMutableList()
            .also {
                it.add(newOrder)
            }
    }

    fun updateContactInfo(isValid: Boolean, name: String) {
        _validContactInfo.value = _validContactInfo
            .value
            .toMutableMap()
            .also {
                it[name] = isValid
            }
    }
}