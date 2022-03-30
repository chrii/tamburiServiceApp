package at.tamburi.tamburimontageservice.ui.WorkflowFinalScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import at.tamburi.tamburimontageservice.ui.LocationProposalScreen.MontageTaskOverview
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel

class WorkflowFinalFragment : Fragment() {
    val viewModel: MontageWorkflowViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MontageTaskOverview(viewModel)
            }
        }
    }
}