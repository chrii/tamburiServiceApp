package at.tamburi.tamburimontageservice.ui.WorkflowLandingScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
    ExpandableCard(title = stringResource(R.string.wf_exp_title), description = "") {
        safeTask.lockerList.forEach { locker ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Log.d(TAG, "${viewModel.gatewaySerialnumber.isNotEmpty()}")
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
                    },
                text = { Text(text = locker.typeName) },
                secondaryText = {
                    Column {
                        Text("Kasten ID: ${locker.lockerId}")
                        when (locker.gateway) {
                            true -> Text(stringResource(R.string.wf_exp_uses_gateway))
                            false -> Text(stringResource(R.string.wf_exp_not_uses_gate))
                        }

                        when (locker.qrCode.isEmpty()) {
                            true -> Text(text = stringResource(R.string.wf_exp_qr_code_registered))
                            false -> Text(text = stringResource(R.string.wf_exp_qr_code_not_registered))
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