package com.avelycure.authorization.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avelycure.authorization.domain.state.OfficeEvents
import com.avelycure.authorization.domain.state.OfficeState
import com.avelycure.resources.BaseScreen

@Composable
fun LoginScreen(
    state: OfficeState,
    events: (OfficeEvents) -> Unit
) {
    BaseScreen(
        queue = state.errorQueue,
        onRemoveHeadFromQueue = {},
        progressBarState = state.progressBarState
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            var emailInput by remember { mutableStateOf("") }
            OutlinedTextField(
                value = emailInput,
                onValueChange = { emailInput = it },
                label = { Text(text = "Email") },
                modifier = Modifier
                    .padding(8.dp)
            )

            var passwordInput by remember { mutableStateOf("") }
            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text(text = "Password") },
                modifier = Modifier
                    .padding(8.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Button(
                modifier = Modifier
                    .padding(4.dp),
                onClick = {
                    events(
                        OfficeEvents.UserClickedLogin(
                            email = emailInput.trim(),
                            password = passwordInput.trim()
                        )
                    )
                }
            ) {
                Text("Login")
            }

            Text(
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                text = buildAnnotatedString {
                    append("Not a member yet?")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("\n Register")
                    }
                },
                modifier = Modifier
                    .clickable {

                    }
            )
        }
    }
}