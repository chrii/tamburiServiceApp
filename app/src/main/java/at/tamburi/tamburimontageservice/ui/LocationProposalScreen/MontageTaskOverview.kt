package at.tamburi.tamburimontageservice.ui.LocationProposalScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel
import at.tamburi.tamburimontageservice.ui.ViewModels.QrCodeScannerState
import at.tamburi.tamburimontageservice.ui.WorkflowLandingScreen.BusSlotDropDownMenu
import at.tamburi.tamburimontageservice.ui.composables.ExpandableCard
import at.tamburi.tamburimontageservice.ui.composables.TwoLineItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MontageTaskOverview(
    viewModel: MontageWorkflowViewModel
) {
    val safeTask = viewModel.task.value ?: throw Exception("No active Task found")
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // TODO: Strings zur Stringlist hinzufügen
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Standort Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                TwoLineItem(cell1 = "Standort ID:", cell2 = safeTask.location.locationId.toString())
                TwoLineItem(
                    cell1 = "Straße: ",
                    cell2 = "${safeTask.location.street} ${safeTask.location.number}"
                )
                TwoLineItem(
                    cell1 = "Stadt: ",
                    cell2 = "${safeTask.location.zipCode} ${safeTask.location.cityName}"
                )
                TwoLineItem(cell1 = "QR Code: ", cell2 = safeTask.location.qrCode)

                safeTask.lockerList.forEachIndexed { index, item ->
                    DetailExpandable(title = item.lockerName ?: "") {
                        TwoLineItem(cell1 = "Kasten ID:", cell2 = item.lockerId.toString())
                        TwoLineItem(cell1 = "QR Code:", cell2 = item.qrCode)
                        TwoLineItem(cell1 = "Bus Slot:", cell2 = item.busSlot.toString())
                        if (item.gateway) TwoLineItem(
                            cell1 = "Gateway ID:",
                            cell2 = item.gatewaySerialnumber
                        )
                    }
                }
            }
        }
    }
}