package at.tamburi.tamburimontageservice.ui.LocationProposalScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel
import at.tamburi.tamburimontageservice.ui.ViewModels.QrCodeScannerState
import at.tamburi.tamburimontageservice.ui.ViewModels.State
import at.tamburi.tamburimontageservice.ui.composables.CustomLoadingIndicator
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme

private const val TAG = "LocationPropFragment"

class LocationProposalFragment : Fragment() {
    val viewModel: MontageWorkflowViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                viewModel.qrCodeScannerState = QrCodeScannerState.Location
                Log.d(TAG, viewModel.task.toString())
                TamburiMontageServiceTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        when (viewModel.state.value) {
                            State.Loading -> CustomLoadingIndicator("Loading...")
                            State.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Something went wrong",
                                    Toast.LENGTH_SHORT
                                ).show()
                                viewModel.changeState(State.Ready)
                            }
                            State.Ready -> {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = stringResource(id = R.string.prop_information_text))
                                    Button(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        onClick = {
                                            findNavController().navigate(R.id.action_proposal_fragment_to_qr_code_fragment)
                                        }
                                    ) {
                                        Text(stringResource(id = R.string.prop_scan_qr_code))
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