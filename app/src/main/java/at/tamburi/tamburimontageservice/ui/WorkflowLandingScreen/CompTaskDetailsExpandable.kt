package at.tamburi.tamburimontageservice.ui.WorkflowLandingScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.ui.composables.ExpandableCard
import at.tamburi.tamburimontageservice.ui.composables.TwoLineExpandable
import at.tamburi.tamburimontageservice.ui.composables.TwoLineItem
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
            TwoLineItem(
                cell1 = stringResource(id = R.string.wf_task_id),
                cell2 = task.montageTaskId.toString()
            )
            TwoLineItem(
                cell1 = stringResource(id = R.string.wf_address),
                cell2 = "${task.location.street} " +
                        task.location.number
            )
            TwoLineItem(
                cell1 = stringResource(id = R.string.wf_zip),
                cell2 = task.location.zipCode
            )
            TwoLineExpandable(
                title = stringResource(id = R.string.wf_power_connection),
                content = task.powerConnection
            )
            TwoLineExpandable(
                title = stringResource(id = R.string.wf_description),
                content = task.locationDescription
            )
            TwoLineItem(
                cell1 = stringResource(id = R.string.wf_montage_ground),
                cell2 = task.montageGroundName
            )
            TwoLineItem(
                cell1 = stringResource(id = R.string.wf_scheduled_date),
                cell2 = Utils.getReadableScheduleDate(task)
            )
        }
    }
}