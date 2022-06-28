package at.tamburi.tamburimontageservice.ui.MainActivity

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainViewModel
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme

@Composable
fun SidebarHeaderCompose(
    viewModel: MainViewModel
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current
    TamburiMontageServiceTheme() {
        Surface(
            color = MaterialTheme.colors.primary,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(176.dp)
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                viewModel.activeUser.value?.let { user ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.side_nav_header,
                                user.firstname
                            ),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.surface
                            )
                        )
                        IconButton(
                            onClick = {
                                viewModel.logout(lifecycle, context)
                            }
                        ) {
                            Icon(
                                Icons.Default.Logout,
                                contentDescription = "Logout",
                                tint = MaterialTheme.colors.surface
                            )
                        }
                    }
                }
            }
        }
    }
}