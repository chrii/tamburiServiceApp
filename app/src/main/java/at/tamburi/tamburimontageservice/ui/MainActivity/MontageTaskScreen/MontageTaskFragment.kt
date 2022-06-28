package at.tamburi.tamburimontageservice.ui.MainActivity.MontageTaskScreen

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.MontageStatus
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainState
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainViewModel
import at.tamburi.tamburimontageservice.ui.MontageWorkflowActivity.MontageWorkflowActivity
import at.tamburi.tamburimontageservice.ui.composables.CustomLoadingIndicator
import at.tamburi.tamburimontageservice.ui.composables.MontageTaskListItem
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme

const val TAG = "MontageTaskFragm"

class MontageTaskFragment : Fragment() {
    val viewModel: MainViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        viewModel.initializeMontageData(requireContext(), lifecycle)
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
        return when (item.itemId) {
            R.id.refresh -> {
                viewModel.initializeMontageData(requireContext(), lifecycle)
                true
            }
            R.id.logout -> {
                viewModel.logout(lifecycle, requireContext())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.P)
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
                        when (viewModel.mainState.value) {
                            MainState.Loading -> CustomLoadingIndicator()
                            MainState.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    viewModel.errorMessage,
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                            MainState.Ready -> {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    val tasks = viewModel.filteredTasks.value
                                        .filter { it.statusId == MontageStatus.ASSIGNED }
                                        .sortedBy { it.scheduledInstallationDate }
                                    if (tasks.isEmpty()) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("Keine Aufträge vorhanden")
                                        }
                                    } else {
                                        LazyColumn(
                                            modifier = Modifier.weight(1.0f)
                                        ) {
                                            tasks.map { task ->
                                                item {
                                                    MontageTaskListItem(
                                                        viewModel = viewModel,
                                                        navigation = findNavController(),
                                                        task = task
                                                    )
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
                                            Text(
                                                text = stringResource(id = R.string.tl_header)
                                            )
                                            Text(
                                                text = stringResource(
                                                    R.string.tl_task_id,
                                                    viewModel.activeTask.value?.montageTaskId
                                                        ?: "No ID"
                                                )
                                            )
                                        }
                                    }
                                    Log.d(
                                        TAG,
                                        "hasActiveTask: ${viewModel.hasActiveTask.value}"
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
