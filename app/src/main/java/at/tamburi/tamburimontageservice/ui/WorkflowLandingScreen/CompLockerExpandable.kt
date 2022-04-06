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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel
import at.tamburi.tamburimontageservice.ui.ViewModels.QrCodeScannerState
import at.tamburi.tamburimontageservice.ui.composables.ExpandableCard

private const val TAG = "CompLockerExpandable"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompLockerExpandable(
    safeTask: MontageTask,
    viewModel: MontageWorkflowViewModel,
    navigation: NavController
) {
    viewModel.qrCodeScannerState =
        if (viewModel.gatewaySerialnumber.isNotEmpty()) QrCodeScannerState.Locker
        else QrCodeScannerState.Gateway
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    ExpandableCard(title = stringResource(R.string.wf_exp_title), description = "") {
        safeTask.lockerList.forEach { locker ->
            var busSlot by remember { mutableStateOf(0) }
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Log.d(TAG, "${viewModel.gatewaySerialnumber.isNotEmpty()}")
                        if (busSlot <= 0) {
                            Toast
                                .makeText(
                                    context,
                                    context.getText(R.string.wf_exp_order),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        } else {
                            if (viewModel.gatewaySerialnumber.isNotEmpty() || locker.gateway) {
                                viewModel.activeLocker = locker
                                navigation.navigate(R.id.action_landing_fragment_to_qr_code_fragment)
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        context.getText(R.string.wf_exp_no_registered_gateway),
                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        }
                    },
                text = { Text(text = locker.typeName ?: "") },
                secondaryText = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        //TODO: Übersetzung
                        Text("Kasten ID: ${locker.lockerId}")
                        when (locker.gateway) {
                            true -> Text(stringResource(R.string.wf_exp_uses_gateway))
                            false -> Text(stringResource(R.string.wf_exp_not_uses_gate))
                        }

                        when (locker.qrCode.isEmpty()) {
                            true -> Text(text = stringResource(R.string.wf_exp_qr_code_registered))
                            false -> Text(text = stringResource(R.string.wf_exp_qr_code_not_registered))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(context.getString(R.string.wf_exp_bus))
                            BusSlotDropDownMenu(
                                itemList = listOf("1", "2", "3", "4", "5", "6"),
                                selectedItem = locker.busSlot ?: 0
                            ) {
                                try {
                                    busSlot = it.toInt()
                                    viewModel.setBusSlotForLocker(
                                        busSlot, locker.lockerId, lifecycle
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