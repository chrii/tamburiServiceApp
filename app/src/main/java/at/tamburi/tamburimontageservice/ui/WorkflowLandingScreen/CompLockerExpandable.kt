package at.tamburi.tamburimontageservice.ui.WorkflowLandingScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel
import at.tamburi.tamburimontageservice.ui.ViewModels.QrCodeScannerState
import at.tamburi.tamburimontageservice.ui.composables.ExpandableCard
import at.tamburi.tamburimontageservice.ui.composables.TwoLineItemAbst

private const val TAG = "CompLockerExpandable"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompLockerExpandable(
    safeTask: MontageTask,
    viewModel: MontageWorkflowViewModel,
    navigation: NavController
) {
    viewModel.qrCodeScannerState =
        if (viewModel.gatewaySerialnumberList.isNotEmpty()) QrCodeScannerState.Locker
        else QrCodeScannerState.Gateway
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    ExpandableCard(title = stringResource(R.string.wf_exp_title), description = "") {
        safeTask.lockerList.forEach { locker ->
            var busSlot by remember { mutableStateOf(locker.busSlot) }
            ListItem(
                modifier = Modifier
                    .fillMaxWidth(),
                text = {
                    Text(
                        text = locker.typeName ?: "",
                        fontWeight = FontWeight.Bold
                    )
                },
                secondaryText = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (locker.gateway) {
                            busSlot = 1
                            viewModel.setBusSlotForLocker(busSlot!!, locker.lockerId, lifecycle)
                            TwoLineItemAbst(title = stringResource(id = R.string.wf_exp_locker_gateway_text)) {
                                if (locker.gatewaySerialnumber.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            viewModel.removeGatewayQrCode(
                                                lifecycle,
                                                context,
                                                locker.lockerId
                                            )
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            "",
                                            tint = MaterialTheme.colors.primary
                                        )
                                    }
                                } else {
                                    IconButton(
                                        onClick = {
                                            if (busSlot != 1) {
                                                Toast.makeText(
                                                    context,
                                                    context.getText(R.string.wf_exp_locker_gateway_bus_wrong_id),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                viewModel.activeLocker = locker
                                                viewModel.qrCodeScreenNavigator(
                                                    navigation,
                                                    QrCodeScannerState.Gateway
                                                )
                                            }
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.QrCodeScanner,
                                            "QR CODE SCANNER",
                                            tint = MaterialTheme.colors.primary
                                        )
                                    }
                                }
                            }
                        }
                        TwoLineItemAbst(title = stringResource(id = R.string.wf_exp_locker_qr_code_text)) {
                            if (locker.qrCode.isNotEmpty()) {
                                IconButton(
                                    onClick = {
//                                        viewModel.setQrCodeForLocker(
//                                            lifecycle,
//                                            locker.lockerId,
//                                            ""
//                                        )
                                        viewModel.removeLockerQrCode(
                                            lifecycle,
                                            context,
                                            locker.lockerId
                                        )
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        "QR CODE SCANNER",
                                        tint = MaterialTheme.colors.primary
                                    )
                                }
                            } else {
                                IconButton(onClick = {
                                    viewModel.activeLocker = locker
                                    viewModel.qrCodeScreenNavigator(
                                        navigation,
                                        QrCodeScannerState.Locker
                                    )
                                }) {
                                    Icon(
                                        Icons.Default.QrCodeScanner,
                                        "QR CODE SCANNER",
                                        tint = MaterialTheme.colors.primary
                                    )
                                }
                            }
                        }
                        if (busSlot == 0) {
                            TwoLineItemAbst(title = stringResource(id = R.string.wf_exp_locker_bus_text)) {
                                BusSlotDropDownMenu(
                                    itemList = listOf("1", "2", "3", "4", "5", "6"),
                                    selectedItem = busSlot!!
                                ) {
                                    try {
                                        busSlot = it.toInt()
                                        viewModel.setBusSlotForLocker(
                                            it.toInt(),
                                            locker.lockerId,
                                            lifecycle
                                        )
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            context.getText(R.string.wf_exp_order),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        } else {
                            TwoLineItemAbst(title = stringResource(id = R.string.wf_exp_locker_bus_text2)) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(busSlot.toString())
                                    if (!locker.gateway) {
                                        IconButton(onClick = {
                                            busSlot = 0
                                        }) {
                                            Icon(Icons.Default.Close, "Close Button")
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
            )
            Divider()
        }
    }
}

@Composable
fun BusSlotDropDownMenu(
    itemList: List<String>,
    selectedItem: Int = 0,
    onItemPressed: (busNumber: String) -> Unit
) {
    var exp by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(selectedItem) }
    var default by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.CenterEnd)
            .border(0.5.dp, color = Color.Black, shape = RectangleShape),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier.clickable(onClick = { exp = true }),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //TODO: ÜBERSETZUNG
            Text(
                if (default) "Wählen" else itemList[selectedIndex],
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .padding(16.dp)
            )
            Box(
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Arrow")
            }
        }
        DropdownMenu(
            expanded = exp,
            onDismissRequest = { exp = false },
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colors.background)
        ) {
            itemList.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        exp = false
                        default = false
                        onItemPressed(item)
                    }) {
                    Text(text = item, textAlign = TextAlign.End)
                }
            }

        }
    }
}