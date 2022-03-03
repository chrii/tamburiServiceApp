package at.tamburi.tamburimontageservice.ui.LoginScreen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.composables.CustomLoadingIndicator
import at.tamburi.tamburimontageservice.ui.theme.White

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(
    navigation: NavController,
    viewModel: MainViewModel
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current

    when (viewModel.loginState.value) {
        LoginState.Loading ->
            CustomLoadingIndicator(if (!viewModel.loadingMessageString.isNullOrEmpty()) viewModel.loadingMessageString else null)
        LoginState.Error -> {
            //TODO: Error handling
            Toast.makeText(
                context,
                stringResource(id = R.string.login_error, viewModel.errorMessage),
                Toast.LENGTH_LONG
            ).show()
            viewModel.changeState(LoginState.Ready)
        }
        LoginState.NEXT -> {
            Toast.makeText(context, "Willkommen zurück, ${viewModel.activeUser?.firstname}", Toast.LENGTH_LONG).show()
            navigation.navigate(R.id.action_loginFragment_to_task_list_fragment)
        }
        LoginState.Ready -> LazyColumn(
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
    }
}