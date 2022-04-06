package at.tamburi.tamburimontageservice.ui.MontageTaskScreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import at.tamburi.tamburimontageservice.MontageWorkflowActivity
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.MontageStatus
import at.tamburi.tamburimontageservice.ui.LoginScreen.LoginState
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainViewModel
import at.tamburi.tamburimontageservice.ui.composables.CustomLoadingIndicator
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme
import at.tamburi.tamburimontageservice.utils.Constants
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

const val TAG = "MontageTaskFragm"

class MontageTaskFragment : Fragment() {
    val viewModel: MainViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        viewModel.initializeData(requireContext(), lifecycle)
    }

    private fun navigateToActivity(taskId: Int) {
        val intent = Intent(requireActivity(), MontageWorkflowActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.magazine_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.refresh) {
            viewModel.initializeData(requireContext(), lifecycle)
            true
        } else super.onOptionsItemSelected(item)
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.P)
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        return ComposeView(requireContext()).apply {
            setContent {
                TamburiMontageServiceTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        when (viewModel.loginState.value) {
                            LoginState.Loading -> CustomLoadingIndicator()
                            LoginState.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    viewModel.errorMessage,
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                            LoginState.Ready -> {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    val tasks = viewModel.filteredTasks.value
                                        .filter { it.statusId == MontageStatus.ASSIGNED }
                                    if (tasks.isNullOrEmpty()) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("Keine Aufträge vorhanden")
                                        }
                                    } else {
                                        LazyColumn(
                                            modifier = Modifier.weight(1.0f)
                                        ) {
                                            items(tasks.size) { index ->
                                                Column {
                                                    ListItem(
                                                        modifier = Modifier
                                                            .padding(bottom = 8.dp)
                                                            .clickable {
                                                                viewModel.taskDetailId =
                                                                    tasks[index].montageTaskId
                                                                findNavController().navigate(R.id.action_task_list_to_details)
                                                            },
                                                        text = {
                                                            Text(text = "Auftragsnummer: ${tasks[index].montageTaskId}")
                                                        },
                                                        secondaryText = {
                                                            Column {
                                                                Text(
                                                                    text = stringResource(
                                                                        R.string.owner_string,
                                                                        tasks[index].locationOwner?.companyName
                                                                            ?: "Empty..."
                                                                    )
                                                                )
                                                                Text(
                                                                    text = stringResource(
                                                                        R.string.adress_string,
                                                                        tasks[index].location.street,
                                                                        tasks[index].location.number
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
                                    }
                                    if (viewModel.hasActiveTask.value) {
                                        Column(
                                            modifier = Modifier
                                                .background(MaterialTheme.colors.primary)
                                                .fillMaxWidth()
                                                .padding(8.dp)
                                                .clickable { navigateToActivity(viewModel.taskDetailId) }
                                        ) {
                                            Divider(
                                                thickness = 1.dp
                                            )
                                            Text(text = stringResource(id = R.string.tl_header))
                                            Text(
                                                text = stringResource(
                                                    R.string.tl_task_id,
                                                    viewModel.activeTask.value?.montageTaskId
                                                        ?: "No ID"
                                                )
                                            )
                                        }
                                    }
                                    Log.d(TAG, "hasActiveTask: ${viewModel.hasActiveTask.value}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
