package at.tamburi.tamburimontageservice.ui.LoginScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.composables.CustomLoadingIndicator
import at.tamburi.tamburimontageservice.ui.theme.Orange
import at.tamburi.tamburimontageservice.ui.theme.OrangeLight
import at.tamburi.tamburimontageservice.ui.theme.White

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(
    navigation: NavController,
    viewModel: LoginViewModel
) {
    val context = LocalContext.current
    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Orange,
        focusedLabelColor = Orange,
        cursorColor = OrangeLight,
        unfocusedBorderColor = Orange,
        unfocusedLabelColor = Orange,
        textColor = White
    )

    when (viewModel.loginState.value) {
        LoginState.Loading -> CustomLoadingIndicator()
        LoginState.Error -> {
            //TODO: Error handling
            Toast.makeText(
                context,
                stringResource(id = R.string.login_error),
                Toast.LENGTH_LONG
            ).show()
            viewModel.changeState(LoginState.Ready)
        }
        LoginState.Ready -> Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var userNameTextField by remember { mutableStateOf("") }
            var passwordTextField by remember { mutableStateOf("") }
            OutlinedTextField(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                value = userNameTextField,
                onValueChange = { userNameTextField = it },
                label = { Text(context.getString(R.string.username)) },
                colors = textFieldColors
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                value = passwordTextField,
                onValueChange = { passwordTextField = it },
                label = { Text(context.getString(R.string.password)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = textFieldColors
            )
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onClick = {
                    if (viewModel.onSubmit(userNameTextField, passwordTextField)) {
                        //TODO: Navigation
                        Toast.makeText(context, "Yay Logged in!", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d(TAG, "Error shown")
                        viewModel.changeState(LoginState.Error)
                    }
                }
            ) {
                Text(text = "Login", color = White)
            }
        }
    }
}