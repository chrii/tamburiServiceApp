package at.tamburi.tamburimontageservice.ui.MainActivity.ServiceTaskScreen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainState
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainViewModel
import at.tamburi.tamburimontageservice.ui.composables.CustomLoadingIndicator
import at.tamburi.tamburimontageservice.ui.composables.ServiceListItem
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme

@Composable
fun ServiceTaskScreen(
    viewModel: MainViewModel,
    navigation: NavController
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var openErrorDialog by remember { mutableStateOf(false) }
    TamburiMontageServiceTheme() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            when (viewModel.mainState.value) {
                MainState.Loading -> CustomLoadingIndicator("Laden...")
                MainState.Error -> {
                    openErrorDialog = true
                    if (openErrorDialog) {
                        AlertDialog(
                            onDismissRequest = { openErrorDialog = false },
                            text = { Text(viewModel.errorMessage) },
                            confirmButton = {
                                Button(onClick = { viewModel.changeState(MainState.Ready) }) {
                                    Text("Ok")
                                }
                            }
                        )
                    }
                }
                MainState.Ready -> {
                    LazyColumn {
                        val locations = viewModel.serviceAssignmentList.value
                        if (locations.isEmpty()) {
                            item { Text(stringResource(R.string.service_list_empty)) }
                        } else {
                            locations.map { location ->
                                item {
                                    ServiceListItem(location) {
                                        viewModel.navigateToServiceLocation(
                                            it,
                                            navigation
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}