package at.tamburi.tamburimontageservice.ui.WorkflowLandingScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel
import at.tamburi.tamburimontageservice.ui.composables.ExpandableCard

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompLockerExpandable(
    safeTask: MontageTask,
    viewModel: MontageWorkflowViewModel,
    navigation: NavController
) {
    ExpandableCard(title = "Kästen", description = "") {
        safeTask.lockerList.forEach { locker ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.activeLocker = locker
                        navigation.navigate(R.id.action_landing_fragment_to_qr_code_fragment)
                    },
                text = { Text(text = locker.typeName) },
                secondaryText = {
                    Column {
                        Text("Kasten ID: ${locker.lockerId}")
                        when (locker.gateway) {
                            true -> Text("Benutzt Gateway")
                            false -> Text("Ohne Gateway")
                        }

                        when (locker.qrCode.isEmpty()) {
                            true -> Text(text = "QR Code nicht registriert")
                            false -> Text(text = "QR Code registriert")
                        }
                    }
                },
                trailing = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = "Add"
                        )
                    }
                }
            )
            Divider()
        }
    }
}