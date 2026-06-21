package com.tcgms.moopicsync.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.tcgms.moopicsync.data.ConfigManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    initialData: ConfigManager.ConfigData,
    onSave: (String, String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var url by remember { mutableStateOf(initialData.url) }
    var port by remember { mutableStateOf(initialData.port) }
    var email by remember { mutableStateOf(initialData.email) }
    var apiKey by remember { mutableStateOf(initialData.apiKey) }

    var urlError by remember { mutableStateOf(false) }
    var portError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var apiKeyError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Moo Config") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = url,
                onValueChange = { url = it; urlError = false },
                label = { Text("Server URL/IP") },
                isError = urlError,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = port,
                onValueChange = { port = it; portError = false },
                label = { Text("Port Number") },
                isError = portError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = false },
                label = { Text("Email Address") },
                isError = emailError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it; apiKeyError = false },
                label = { Text("API Key") },
                isError = apiKeyError,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            if (urlError || portError || emailError || apiKeyError) {
                Text(
                    text = "Please enter values for the required fields.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onCancel) {
                    Text("Cancel")
                }
                Button(onClick = {
                    urlError = url.isBlank()
                    portError = port.isBlank()
                    emailError = email.isBlank()
                    apiKeyError = apiKey.isBlank()

                    if (!urlError && !portError && !emailError && !apiKeyError) {
                        onSave(url, port, email, apiKey)
                    }
                }) {
                    Text("Save")
                }
            }
        }
    }
}
