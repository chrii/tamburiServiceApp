package at.tamburi.tamburimontageservice.ui.MainActivity.ClaimScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainViewModel

private const val COMPARTMENT_DEFECT = "DEFEKT"

@Composable
fun ClaimScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    if (viewModel.activeClaim.value != null) {
        val claim = viewModel.activeClaim.value!!
        var deliveryCode by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    bitmap = viewModel
                        .createQrCode(claim.tamburiCode)
                        .asImageBitmap(),
                    contentDescription = "Bla"
                )
                Text(stringResource(R.string.claim_screen_text_field, claim.tamburiPin))
                Text(claim.tamburiCode)
                Text("Fachnummer: ${claim.compartmentWiring}")
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                if (claim.typeName == COMPARTMENT_DEFECT) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = deliveryCode,
                        onValueChange = { deliveryCode = it },
                        label = { Text(stringResource(R.string.claim_screen_defect_text_field)) },
                        placeholder = {
                            Text(stringResource(R.string.claim_screen_defect_text_placeholder))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                } else {
                    Text(claim.deliveryCode ?: "No delivery code")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (claim.typeName == COMPARTMENT_DEFECT) {
                            viewModel.confirmDefectRepaired(
                                claim.claimId,
                                lifecycle,
                                navController
                            )
                        } else navController.popBackStack()
                    }
                ) {
                    if (claim.typeName == COMPARTMENT_DEFECT) {
                        Text(stringResource(R.string.claim_screen_defect_button_text))
                    } else {
                        Text(stringResource(R.string.claim_screen_button_back))
                    }
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                ) {
                    Text(stringResource(id = R.string.claim_screen_button_problem))
                }
            }
        }
    }
}