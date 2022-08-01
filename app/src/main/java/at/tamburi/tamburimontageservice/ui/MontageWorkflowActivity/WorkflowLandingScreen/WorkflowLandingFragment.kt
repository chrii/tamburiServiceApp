package at.tamburi.tamburimontageservice.ui.MontageWorkflowActivity.WorkflowLandingScreen

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel
import at.tamburi.tamburimontageservice.ui.ViewModels.State
import at.tamburi.tamburimontageservice.ui.ViewModels.WorkflowState
import at.tamburi.tamburimontageservice.ui.composables.CustomLoadingIndicator
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi

private const val TAG = "WorkflowLandingFragment"

class WorkflowLandingFragment : Fragment() {
    val viewModel: MontageWorkflowViewModel by activityViewModels()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.workflow_main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_revoke -> {
                viewModel.revokeTask(requireContext(), lifecycle)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.wf_top_menu_revoke_toast_text),
                    Toast.LENGTH_SHORT
                ).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    override fun onResume() {
//        super.onResume()
//        viewModel.getWorkflowState(lifecycle, requireContext())
//    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        viewModel.getTask(requireContext(), lifecycle)
//        if(viewModel.workflowState.value == WorkflowState.Finished) {
//            findNavController().navigate(R.id.action_landing_fragment_to_final_fragment)
//        }

        return ComposeView(requireContext()).apply {
            setContent {
                TamburiMontageServiceTheme() {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        when (viewModel.state.value) {
                            State.Loading -> CustomLoadingIndicator()
                            State.Error -> Text(text = "Error View")
                            State.Ready -> LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                viewModel.task.value?.let { safeTask ->
                                    item {
                                        CompTaskDetailsExpandable(task = safeTask)
                                    }
                                    item {
                                        safeTask.locationOwner?.let { CompOwnerExpandable(owner = it) }
                                    }
//                                    item {
//                                        CompLocationExpandable(
//                                            viewModel,
//                                            findNavController()
//                                        )
//                                    }
                                    item {
                                        CompLockerExpandable(
                                            safeTask = safeTask,
                                            viewModel = viewModel,
                                            navigation = findNavController()
                                        )
                                    }
                                    Log.d(TAG, viewModel.hasEmptyQrCodes().toString())
                                    if (!viewModel.hasEmptyQrCodes()) {
                                        x
                                        item {
                                            Button(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(8.dp),
                                                onClick = {
                                                    viewModel.submitTaskData(
                                                        lifecycle,
                                                        requireContext(),
                                                        findNavController()
                                                    )
                                                }
                                            ) {
                                                Text(text = stringResource(id = R.string.wf_button))
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
}