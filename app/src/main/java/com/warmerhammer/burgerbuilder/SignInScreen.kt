package com.warmerhammer.burgerbuilder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SignInScreen(
    destination: String?,
    onSignIn: (destination : String?) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxWidth()
//            .padding(PaddingValues(vertical= 15.dp))
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingValues(20.dp)),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            var email by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            val valid by rememberSaveable (email) { mutableStateOf(validate(email)) }

            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = MaterialTheme.colors.primary) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Gray,
                        focusedBorderColor = MaterialTheme.colors.primary,
                        textColor = MaterialTheme.colors.primary,
                        focusedLabelColor = colors.primary
                    ),
                    trailingIcon = {
                        if (valid) {
                            Icon(Icons.Default.Done, tint = Color.Green, contentDescription = null)
                        } else Icon(Icons.Default.Close, tint = Color.Red, contentDescription = null)
                    }
                )

                if (!valid) {
                    Text(
                        "Please enter valid email with '.' and '@' .",
                        color = colors.secondary,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            }



            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = MaterialTheme.colors.primary) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MaterialTheme.colors.primary,
                    disabledTextColor = Color.Gray,
                    textColor = MaterialTheme.colors.primary,
                    focusedLabelColor = colors.primary
                )
            )

            Button(
                onClick = { onSignIn(destination) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red,
                ),
                enabled = valid
            ) {
                Text("Submit", color = Color.White)
            }

            Text("Please sign in to complete your order.", color = MaterialTheme.colors.secondary)
        }
    }
}

private fun validate(email: String) : Boolean {
    return email.contains('@') && email.contains('.')
}