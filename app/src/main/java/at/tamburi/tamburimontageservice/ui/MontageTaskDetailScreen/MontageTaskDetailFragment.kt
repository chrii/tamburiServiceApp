package at.tamburi.tamburimontageservice.ui.MontageTaskDetailScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.MontageStatus
import at.tamburi.tamburimontageservice.ui.LoginScreen.LoginViewModel
import at.tamburi.tamburimontageservice.ui.composables.TwoLineItem
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme

class MontageTaskDetailFragment : Fragment() {
    val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val task = viewModel.tasks.value.find { it.montageId == viewModel.taskDetailId }
        return ComposeView(requireContext()).apply {
            setContent {
                TamburiMontageServiceTheme {
                    Surface(
                        color = MaterialTheme.colors.background
                    ) {
                        task?.let { t ->
                            LazyColumn(Modifier.fillMaxSize()) {
                                item {
                                    Text(
                                        modifier = Modifier.padding(8.dp),
                                        text = "Auftragsdetails",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                item {
                                    TwoLineItem(
                                        cell1 = stringResource(id = R.string.ds_montage_id),
                                        cell2 = t.montageId.toString()
                                    )
                                    TwoLineItem(
                                        cell1 = stringResource(id = R.string.ds_street_name), cell2 =
                                        "${t.remoteLocation.streetName} ${t.remoteLocation.streetNumber}"
                                    )
                                    TwoLineItem(
                                        cell1 = stringResource(id = R.string.ds_qr_code),
                                        cell2 = t.remoteLocation.qrCode.toString()
                                    )
                                    TwoLineItem(
                                        cell1 = stringResource(id = R.string.ds_montage_status),
                                        cell2 = when (t.montageStatus) {
                                            MontageStatus.ASSIGNED -> "Zugewiesen"
                                            MontageStatus.CREATED -> "Erstellt"
                                            MontageStatus.ACTIVE -> "Aktiv"
                                            MontageStatus.CLOSED -> "Beendet"
                                            MontageStatus.MAINTENANCE -> "Wartungsauftrag"
                                        }
                                    )
                                    TwoLineItem(
                                        cell1 = stringResource(id = R.string.ds_description),
                                        cell2 = t.locationDesc
                                    )
                                    TwoLineItem(
                                        cell1 = stringResource(id = R.string.ds_montage_ground),
                                        cell2 = t.montageGround.type
                                    )
                                }
                                item {
                                    Button(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        onClick = {
                                            viewModel.onSubmitTask(requireContext(), lifecycle)
                                        }) {
                                        Text("Zuweisen")
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