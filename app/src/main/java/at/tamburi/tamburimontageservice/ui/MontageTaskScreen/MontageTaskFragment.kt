package at.tamburi.tamburimontageservice.ui.MontageTaskScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.LoginScreen.LoginState
import at.tamburi.tamburimontageservice.ui.LoginScreen.LoginViewModel
import at.tamburi.tamburimontageservice.ui.composables.CustomLoadingIndicator
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme

class MontageTaskFragment : Fragment() {
    val viewModel: LoginViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()
        viewModel.getTaskList(lifecycle)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTaskList(lifecycle)
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TamburiMontageServiceTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        val lifecycleOwner = LocalLifecycleOwner.current
                        val context = LocalContext.current
                        when (viewModel.loginState.value) {
                            LoginState.Loading -> CustomLoadingIndicator()
                            LoginState.Error -> {
                                Toast.makeText(context, viewModel.errorMessage, Toast.LENGTH_LONG)
                                    .show()
                            }
                            LoginState.Ready -> if (viewModel.tasks.value.isEmpty()) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(text = stringResource(id = R.string.no_task_data))
                                }
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    LazyColumn(
                                    ) {
                                        val tasks = viewModel.tasks.value
                                        viewModel.getActiveTask(context, lifecycleOwner.lifecycle)
                                        items(tasks.size) { index ->
                                            Column {
                                                ListItem(
                                                    modifier = Modifier
                                                        .padding(bottom = 8.dp)
                                                        .clickable {
                                                            viewModel.taskDetailId =
                                                                tasks[index].montageId
                                                            findNavController().navigate(R.id.action_task_list_to_details)
                                                        },
                                                    text = {
                                                        Text(text = "Auftragsnummer: ${tasks[index].montageId}")
                                                    },
                                                    secondaryText = {
                                                        Column {
                                                            Text(
                                                                text = stringResource(
                                                                    R.string.owner_string,
                                                                    tasks[index].locationOwner.companyName
                                                                )
                                                            )
                                                            Text(
                                                                text = stringResource(
                                                                    R.string.adress_string,
                                                                    tasks[index].remoteLocation.streetName,
                                                                    tasks[index].remoteLocation.streetNumber
                                                                ),
                                                            )
                                                        }
                                                    },
                                                    trailing = {
                                                        Icon(
                                                            imageVector = Icons.Default.KeyboardArrowRight,
                                                            contentDescription = "Pfeil nach rechts"
                                                        )
                                                    }
                                                )
                                                Divider()
                                            }
                                        }
                                    }
                                    if (viewModel.hasActiveTask.value) {
                                        Column() {
                                            Divider(
                                                thickness = 1.dp
                                            )
                                            ListItem(
                                                modifier = Modifier.background(Color.Gray),
                                                text = { Text(text = "Aktiver Auftrag") },
                                                secondaryText = { Text(text = "Auftragsnummer: ${viewModel.activeTask.value?.montageId}") }
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
}