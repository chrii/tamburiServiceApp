package at.tamburi.tamburimontageservice.ui.MainActivity.ServiceTaskDetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainViewModel
import at.tamburi.tamburimontageservice.ui.composables.ExpandableCard
import at.tamburi.tamburimontageservice.ui.composables.LineItemWithEllipsis
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ServiceTaskDetailsScreen(
    viewModel: MainViewModel,
    nav: NavController
) {
    val context = LocalContext.current
    TamburiMontageServiceTheme() {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            val location = viewModel.activeServiceLocation.value!!

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                LineItemWithEllipsis(
                    title = stringResource(R.string.service_detail_name),
                    content = location.locationName
                )
                LineItemWithEllipsis(
                    title = stringResource(R.string.service_detail_address),
                    content = "${location.street} ${location.number}"
                )
                LineItemWithEllipsis(
                    title = stringResource(R.string.service_detail_city),
                    content = "${location.cityName}, ${location.zipCode}"
                )
                LineItemWithEllipsis(
                    title = stringResource(R.string.service_detail_contact_person),
                    content = location.contactPerson
                )
                LineItemWithEllipsis(
                    title = stringResource(R.string.service_detail_contact_phone),
                    content = location.contactPhone
                )
                val textSize = 14.sp
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.service_detail_battery),
                        fontWeight = FontWeight.Bold,
                        fontSize = textSize
                    )
                    Icon(
                        Icons.Default.BatteryFull,
                        contentDescription = "Battery Icon",
                        //tint = viewModel.checkBatteryStatus(location.batteryStatus)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.service_detail_gps_location),
                        fontWeight = FontWeight.Bold,
                        fontSize = textSize
                    )
                    IconButton(onClick = {
                        viewModel.openMaps(
                            context,
                            location.longitude,
                            location.latitude
                        )
                    }) {
                        Icon(
                            Icons.Default.Map,
                            contentDescription = "Map Icon"
                        )
                    }
                }
                ExpandableCard(
                    title = stringResource(R.string.service_detail_claims),
                    description = ""
                ) {
                    LazyColumn {
                        location.claimList?.map {
                            item {
                                ListItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { nav.navigate(R.id.action_service_to_claim) },
                                    icon = { Icons.Default.Delete },
                                    text = { Text(it.typeName) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}