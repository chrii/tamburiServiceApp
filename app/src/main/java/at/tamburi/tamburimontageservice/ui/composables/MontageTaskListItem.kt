package at.tamburi.tamburimontageservice.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainViewModel
import at.tamburi.tamburimontageservice.utils.Utils

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MontageTaskListItem(
    viewModel: MainViewModel,
    task: MontageTask,
    navigation: NavController
) {
    Column {
        ListItem(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable {
                    viewModel.taskDetailId = task.montageTaskId
                    navigation.navigate(R.id.action_task_list_to_details)
                },
            text = {
                Text(text = task.location.locationName)
            },
            secondaryText = {
                Column {
                    Text(text = "Auftragsnummer: ${task.montageTaskId}")
                    Text(
                        text = stringResource(
                            R.string.owner_string,
                            task.locationOwner?.companyName
                                ?: "Empty..."
                        )
                    )
                    Text(
                        text = stringResource(
                            R.string.adress_string,
                            task.location.street,
                            task.location.number
                        ),
                    )
                    Text(
                        text = stringResource(
                            R.string.schedule_string,
                            Utils.getReadableScheduleDate(task)
                        ),
                    )
                }
            },
            trailing = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Pfeil nach rechts"
                )
            }
        )
        Divider()
    }
}