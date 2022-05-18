package at.tamburi.tamburimontageservice.ui.WorkflowLandingScreen

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel
import at.tamburi.tamburimontageservice.ui.ViewModels.QrCodeScannerState
import at.tamburi.tamburimontageservice.ui.composables.ExpandableCard
import at.tamburi.tamburimontageservice.ui.composables.LineItemWithEllipsis
import at.tamburi.tamburimontageservice.ui.composables.TwoLineItemAbst
import at.tamburi.tamburimontageservice.utils.NonNullString

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompLocationExpandable(
    viewModel: MontageWorkflowViewModel,
    navigation: NavController
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    viewModel.task.value?.let { safeTask ->
        ExpandableCard(
            title = stringResource(id = R.string.wf_exp_location_title),
            description = "",
            expanded = false
        ) {
            // TODO: Hardcoded content
            LineItemWithEllipsis(
                title = stringResource(id = R.string.wf_exp_location_person_in_charge),
                content = if (NonNullString.NO_PERSON == safeTask.location.contactPerson) {
                    stringResource(id = R.string.ds_no_person)
                } else {
                    safeTask.location.contactPerson
                }
            )
            if (safeTask.location.contactPhone != NonNullString.NO_PHONE) LineItemWithEllipsis(
                title = stringResource(id = R.string.wf_exp_location_p_in_charge_phone),
                content = safeTask.location.contactPhone
            )

            if (safeTask.location.longitude <= 0 || safeTask.location.latitude <= 0) {
                TwoLineItemAbst(title = stringResource(id = R.string.wf_exp_location_GPS_title)) {
                    IconButton(onClick = {
                        viewModel.setGPSLocation(context, lifecycle)
                    }) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "GPS Location Button",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            } else {
                LineItemWithEllipsis(
                    title = stringResource(id = R.string.wf_exp_location_GPS_title),
                    content = stringResource(id = R.string.wf_exp_location_GPS_content)
                )
            }

            if (safeTask.location.qrCode.isEmpty()) {
                TwoLineItemAbst(title = stringResource(id = R.string.wf_exp_location_qr_code)) {
                    IconButton(
                        onClick = {
                            viewModel.activeLocker = safeTask.lockerList.first()
                            viewModel.qrCodeScreenNavigator(navigation, QrCodeScannerState.Location)
                        }) {
                        Icon(
                            Icons.Default.QrCodeScanner,
                            contentDescription = "QR Code Icon",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            } else {
                TwoLineItemAbst(title = stringResource(id = R.string.wf_exp_location_name)) {
                    //TODO: Hardcoded Text
                    Text(text = safeTask.location.locationName)
                }
                TwoLineItemAbst(title = stringResource(id = R.string.wf_exp_location_qr_code_registered)) {
                    Text(text = "Registriert")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.resetLocationQrCode(lifecycle, safeTask.location.locationId)
                    }) {
                    //TODO: Hardcoded text
                    Text(text = "Reset Location")
                }
            }
        }
    }
}