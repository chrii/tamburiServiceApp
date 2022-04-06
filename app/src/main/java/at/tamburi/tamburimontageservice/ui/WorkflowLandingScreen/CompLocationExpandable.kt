package at.tamburi.tamburimontageservice.ui.WorkflowLandingScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel
import at.tamburi.tamburimontageservice.ui.ViewModels.QrCodeScannerState
import at.tamburi.tamburimontageservice.ui.composables.ExpandableCard
import at.tamburi.tamburimontageservice.ui.composables.LineItemWithEllipsis
import at.tamburi.tamburimontageservice.ui.composables.TwoLineItemAbst

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompLocationExpandable(
    viewModel: MontageWorkflowViewModel,
    navigation: NavController
) {
    viewModel.task.value?.let { safeTask ->
        ExpandableCard(
            title = stringResource(id = R.string.wf_exp_location_title),
            description = "",
            expanded = false
        ) {
            // TODO: Hardcoded content
            LineItemWithEllipsis(
                title = stringResource(id = R.string.wf_exp_location_person_in_charge),
                content = "Hier könnte Ihr Name stehen"
            )
            //TODO: Hardcoded Number
            LineItemWithEllipsis(
                title = stringResource(id = R.string.wf_exp_location_p_in_charge_phone),
                content = "066420543627"
            )
            if (safeTask.location.qrCode.isEmpty()) {
                TwoLineItemAbst(title = stringResource(id = R.string.wf_exp_location_qr_code)) {
                    IconButton(onClick = {
                        viewModel.activeLocker = safeTask.lockerList.first()
                        viewModel.qrCodeScreenNavigator(navigation, QrCodeScannerState.Location)
                    }) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = "Map Icon")
                    }
                }
            } else {
                TwoLineItemAbst(title = stringResource(id = R.string.wf_exp_location_name)) {
                    //TODO: Hardcoded Text
                    Text(text = "Julia")
                }
                TwoLineItemAbst(title = stringResource(id = R.string.wf_exp_location_qr_code_registered)) {
                    Text(text = "Registriert")
                }
            }

        }
    }
}