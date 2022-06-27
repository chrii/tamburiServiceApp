package at.tamburi.tamburimontageservice.ui.LoginActivity.LoginScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.ViewModels.LoginState
import at.tamburi.tamburimontageservice.ui.ViewModels.LoginViewModel
import at.tamburi.tamburimontageservice.ui.composables.CustomLoadingIndicator
import at.tamburi.tamburimontageservice.ui.theme.White

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current
    var errorDialog by remember { mutableStateOf(false) }

    when (viewModel.state.value) {
        LoginState.Loading ->
            CustomLoadingIndicator(if (!viewModel.loadingMessageString.isNullOrEmpty()) viewModel.loadingMessageString else null)
        LoginState.Error -> {
            viewModel.changeState(LoginState.Ready)
            errorDialog = true
        }
        LoginState.NEXT -> {
            viewModel.next(context)
        }
        LoginState.Ready -> Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    var userNameTextField by remember { mutableStateOf("") }
                    var passwordTextField by remember { mutableStateOf("") }
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        value = userNameTextField,
                        onValueChange = { userNameTextField = it },
                        label = { Text(context.getString(R.string.username)) },
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        value = passwordTextField,
                        onValueChange = { passwordTextField = it },
                        label = { Text(context.getString(R.string.password)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Button(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        onClick = {
                            viewModel.onSubmit(
                                userNameTextField,
                                passwordTextField,
                                lifecycle,
                                context
                            )
                        }
                    ) {
                        Text(text = "Login", color = White)
                    }
                }
            }
            if (errorDialog) {
                AlertDialog(
                    onDismissRequest = { errorDialog = false },
                    title = {
                        Row {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Warning Icon"
                            )
                            Text(text = "Error")
                        }
                    },
                    text = {
                        //TODO: Refactoring
                        Text(
                            text = viewModel.errorMessage ?: "Something went wrong"
                        )
                    },
                    confirmButton = {
                        Button(
                            modifier = Modifier.padding(4.dp),
                            onClick = {
                                errorDialog = false
                                viewModel.changeState(LoginState.Ready)
                            }
                        ) {
                            Text("ok")
                        }
                    }
                )
            }
        }
    }
}