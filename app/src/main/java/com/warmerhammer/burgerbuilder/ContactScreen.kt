package com.warmerhammer.burgerbuilder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.warmerhammer.burgerbuilder.ui.theme.DinerUndecided

val contactFields = listOf(
    "Full Name",
    "Street",
    "City",
    "State",
    "Zipcode"
)

@Composable
fun ContactScreen(
    viewModel: MainActivityViewModel = viewModel()
) {

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(color = DinerUndecided)
    ) {

        item {
            Text(
                "Contact Information",
                color = MaterialTheme.colors.background,
                textAlign = TextAlign.Center
            )
        }

        items(contactFields) { contactField ->
            var txt by rememberSaveable { mutableStateOf("") }
            val isValid = validate(txt, contactField)
            val currentColor =
                if (isValid) MaterialTheme.colors.background else MaterialTheme.colors.primary
            viewModel.updateContactInfo(isValid, contactField)

            OutlinedTextField(
                value = txt,
                onValueChange = {
                    txt = it
                },
                label = {
                    Text(contactField)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = currentColor,
                    focusedBorderColor = currentColor,
                    textColor = MaterialTheme.colors.background,
                    focusedLabelColor = currentColor,
                    unfocusedLabelColor = currentColor
                ),
                trailingIcon = {
                    if (isValid)
                        Icon(Icons.Default.Done, contentDescription = null, tint = MaterialTheme.colors.background)
                    else
                        Icon(Icons.Default.Cancel, contentDescription = null, tint = MaterialTheme.colors.primary)
                }
            )
        }
    }

}

fun validate(value: String, name: String): Boolean {
    return if (name == "Zipcode") {
        value.isDigitsOnly() && value.length > 4
    } else {
        value.isNotEmpty()
    }
}