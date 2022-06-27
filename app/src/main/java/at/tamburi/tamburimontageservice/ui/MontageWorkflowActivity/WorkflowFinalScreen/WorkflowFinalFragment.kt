package at.tamburi.tamburimontageservice.ui.MontageWorkflowActivity.WorkflowFinalScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import at.tamburi.tamburimontageservice.ui.LocationProposalScreen.MontageTaskOverview
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme

class WorkflowFinalFragment : Fragment() {
    val viewModel: MontageWorkflowViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TamburiMontageServiceTheme() {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        MontageTaskOverview(viewModel)
                    }
                }
            }
        }
    }
}