package at.tamburi.tamburimontageservice.ui.WorkflowLandingScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel
import at.tamburi.tamburimontageservice.ui.ViewModels.State
import at.tamburi.tamburimontageservice.ui.composables.CustomLoadingIndicator
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme

private const val TAG = "WorkflowLandingFragment"

class WorkflowLandingFragment : Fragment() {
    val viewModel: MontageWorkflowViewModel by activityViewModels()

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
                            State.Ready -> Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                viewModel.task.value?.let { saveTask ->
                                    Text(text = saveTask.locationDesc)
                                    Button(onClick = {
                                        findNavController().navigate(R.id.action_landing_fragment_to_qr_code_fragment)
                                    }) {
                                        Text(text = "Scan QR Code")
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