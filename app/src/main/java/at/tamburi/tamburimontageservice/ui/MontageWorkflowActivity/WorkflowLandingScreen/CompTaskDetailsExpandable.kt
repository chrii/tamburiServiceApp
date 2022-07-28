package at.tamburi.tamburimontageservice.ui.MontageWorkflowActivity.WorkflowLandingScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.ui.composables.ExpandableCard
import at.tamburi.tamburimontageservice.ui.composables.LineItemWithEllipsis
import at.tamburi.tamburimontageservice.utils.Utils

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompTaskDetailsExpandable(
    task: MontageTask
) {
    ExpandableCard(
        title = stringResource(id = R.string.wf_title),
        description = "",
        expanded = true
    ) {
        Column {
            LineItemWithEllipsis(
                title = stringResource(id = R.string.wf_location_name),
                content = task.location.locationName
            )
            LineItemWithEllipsis(
                title = stringResource(id = R.string.wf_task_id),
                content = task.montageTaskId.toString()
            )
            LineItemWithEllipsis(
                title = stringResource(id = R.string.wf_address),
                content = "${task.location.street} " +
                        task.location.number
            )
            LineItemWithEllipsis(
                title = stringResource(id = R.string.wf_city),
                content = "${task.location.zipCode}, ${task.location.cityName}"
            )
            LineItemWithEllipsis(
                title = stringResource(id = R.string.wf_location_id),
                content = "${task.location.locationId}"
            )
            LineItemWithEllipsis(
                title = stringResource(id = R.string.wf_power_connection),
                content = task.powerConnection
            )
            LineItemWithEllipsis(
                title = stringResource(id = R.string.wf_description),
                content = task.locationDescription
            )
            LineItemWithEllipsis(
                title = stringResource(id = R.string.wf_montage_ground),
                content = task.montageGroundName
            )
            LineItemWithEllipsis(
                title = stringResource(id = R.string.wf_scheduled_date),
                content = Utils.getReadableScheduleDate(task)
            )
        }
    }
}