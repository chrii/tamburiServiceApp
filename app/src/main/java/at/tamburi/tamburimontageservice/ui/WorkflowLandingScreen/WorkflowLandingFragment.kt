package at.tamburi.tamburimontageservice.ui.WorkflowLandingScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel
import at.tamburi.tamburimontageservice.ui.ViewModels.State
import at.tamburi.tamburimontageservice.ui.composables.CustomLoadingIndicator
import at.tamburi.tamburimontageservice.ui.composables.ExpandableCard
import at.tamburi.tamburimontageservice.ui.composables.TwoLineItem
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme

private const val TAG = "WorkflowLandingFragment"

class WorkflowLandingFragment : Fragment() {
    val viewModel: MontageWorkflowViewModel by activityViewModels()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getTask(requireContext(), lifecycle)
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
                                        CompOwnerExpandable(owner = safeTask.locationOwner)
                                    }
                                    item {
                                        CompLockerExpandable(
                                            safeTask = safeTask,
                                            viewModel = viewModel,
                                            navigation = findNavController()
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