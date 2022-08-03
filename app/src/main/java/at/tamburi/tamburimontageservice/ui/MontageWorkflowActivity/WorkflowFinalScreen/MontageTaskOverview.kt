package at.tamburi.tamburimontageservice.ui.LocationProposalScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel
import at.tamburi.tamburimontageservice.ui.composables.LineItemWithEllipsis

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MontageTaskOverview(
    viewModel: MontageWorkflowViewModel
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val safeTask = viewModel.task.value ?: throw Exception("No active Task found")
    Log.d(
        "Overview", safeTask.location.locationId.toString()
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // TODO: ÜBERSETZUNG
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Standort Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                //TODO: ÜBERSETZUNG
                LineItemWithEllipsis(
                    title = "Standort ID",
                    content = safeTask.location.locationId.toString()
                )
                //TODO: ÜBERSETZUNG
                LineItemWithEllipsis(
                    title = "Straße",
                    content = "${safeTask.location.street} ${safeTask.location.number}"
                )
                //TODO: ÜBERSETZUNG
                LineItemWithEllipsis(
                    title = "Stadt",
                    content = "${safeTask.location.zipCode} ${safeTask.location.cityName}"
                )
                //TODO: ÜBERSETZUNG
                LineItemWithEllipsis(
                    title = "QR Code",
                    content = safeTask.location.qrCode
                )
                //TODO: ÜBERSETZUNG
                if (viewModel.registrationQrCode.isNotEmpty()) {
                    DetailExpandable(title = "Standort registrieren") {
                        LineItemWithEllipsis(
                            title = "Bitte halten Sie den QR Code an den Scanner des Kasten",
                            content = ""
                        )
                        Image(
                            bitmap = viewModel
                                .createQrCode("${safeTask.location.locationId}:${viewModel.registrationQrCode}")
                                .asImageBitmap(),
                            contentDescription = "Bla"
                        )
                    }
                }
                safeTask.lockerList.forEachIndexed { index, item ->
                    DetailExpandable(title = "Kasten ${item.busSlot.toString()}") {
                        //TODO: ÜBERSETZUNG
                        LineItemWithEllipsis(
                            title = "Kasten ID",
                            content = index.toString()
                        )
                        //TODO: ÜBERSETZUNG
                        LineItemWithEllipsis(
                            title = "QR Code",
                            content = item.qrCode
                        )
                        //TODO: ÜBERSETZUNG
                        LineItemWithEllipsis(
                            title = "Kasten Nummer",
                            content = item.busSlot.toString()
                        )
                        //TODO: ÜBERSETZUNG
                        if (item.gateway) LineItemWithEllipsis(
                            title = "Gateway ID",
                            content = item.gatewaySerialnumber
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {
                        viewModel.closeTask(context, lifecycle)
                    }
                ) {
                    Text(text = context.getString(R.string.prop_submit_button_text))
                }
            }
        }
    }
}