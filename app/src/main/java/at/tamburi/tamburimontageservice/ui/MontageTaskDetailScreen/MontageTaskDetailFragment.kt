package at.tamburi.tamburimontageservice.ui.MontageTaskDetailScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainViewModel
import at.tamburi.tamburimontageservice.ui.composables.LineItemWithEllipsis
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme
import at.tamburi.tamburimontageservice.utils.Utils


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
                                    Text(
                                        modifier = Modifier.padding(8.dp),
                                        text = stringResource(id = R.string.ds_task_details),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                item {
                                    LineItemWithEllipsis(
                                        title = stringResource(id = R.string.ds_montage_id),
                                        content = t.montageTaskId.toString()
                                    )
                                    LineItemWithEllipsis(
                                        title = stringResource(id = R.string.ds_street_name),
                                        content =
                                        "${t.location.street} ${t.location.number}"
                                    )
                                    LineItemWithEllipsis(
                                        title = stringResource(id = R.string.ds_montage_status),
                                        content = getString(MontageStatus.getStatusString(t.statusId))
                                    )
                                    LineItemWithEllipsis(
                                        title = stringResource(R.string.ds_description),
                                        content = t.locationDescription
                                    )
                                    LineItemWithEllipsis(
                                        title = stringResource(id = R.string.ds_montage_ground),
                                        content = t.montageGroundName
                                    )
                                    LineItemWithEllipsis(
                                        title = stringResource(id = R.string.ds_scheduled_date),
                                        content = Utils.getReadableScheduleDate(task)
                                    )
                                    LineItemWithEllipsis(
                                        title = stringResource(id = R.string.ds_locker_count),
                                        content = task.lockerList.size.toString()
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