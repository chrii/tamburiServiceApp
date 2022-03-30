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
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainViewModel
import at.tamburi.tamburimontageservice.ui.composables.TwoLineItem
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme

class MontageTaskDetailFragment : Fragment() {
    val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val task = viewModel.filteredTasks.value.find { it.montageTaskId == viewModel.taskDetailId }
        return ComposeView(requireContext()).apply {
            setContent {
                TamburiMontageServiceTheme {
                    Surface(
                        color = MaterialTheme.colors.background
                    ) {
                        task?.let { t ->
                            LazyColumn(Modifier.fillMaxSize()) {
                                item {
                                    //TODO: String zur Stringlist hinzufügen
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
                                        cell2 = t.montageTaskId.toString()
                                    )
                                    TwoLineItem(
                                        cell1 = stringResource(id = R.string.ds_street_name),
                                        cell2 =
                                        "${t.location.street} ${t.location.number}"
                                    )
                                    TwoLineItem(
                                        cell1 = stringResource(id = R.string.ds_montage_status),
                                        cell2 = getString(MontageStatus.getStatusString(t.statusId))
                                    )
                                    TwoLineItem(
                                        cell1 = stringResource(id = R.string.ds_description),
                                        cell2 = t.locationDescription
                                    )
                                    TwoLineItem(
                                        cell1 = stringResource(id = R.string.ds_montage_ground),
                                        cell2 = t.montageGroundName
                                    )
                                    TwoLineItem(
                                        cell1 = stringResource(id = R.string.ds_scheduled_date),
                                        cell2 = viewModel.getReadableScheduleDate(task)
                                    )
                                    TwoLineItem(
                                        cell1 = stringResource(id = R.string.ds_locker_count),
                                        cell2 = task.lockerList.size.toString()
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
                        } ?: Text(text = "Keine Aufträge gefunden")
                    }
                }
            }
        }
    }
}