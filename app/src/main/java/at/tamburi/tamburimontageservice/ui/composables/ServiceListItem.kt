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
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.ServiceAssignment
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ServiceListItem(
    serviceAssignment: ServiceAssignment,
    onClick: (locationId: Int) -> Unit
) {
    Column {
        ListItem(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .clickable { onClick(serviceAssignment.locationId) },
            text = {
                Text(serviceAssignment.locationName)
            },
            secondaryText = {
                Column {
                    Text(
                        stringResource(
                            R.string.service_list_address,
                            serviceAssignment.locationAddress
                        )
                    )
                    Text(
                        stringResource(
                            R.string.service_list_city,
                            "${serviceAssignment.city}, ${serviceAssignment.zipCode}"
                        )
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
        Divider(
            color = MaterialTheme.colors.primary,
            thickness = 1.dp
        )
    }
}