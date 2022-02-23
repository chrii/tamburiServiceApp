package at.tamburi.tamburimontageservice.ui.MontageTaskDetailScreen

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel

@Composable
fun CompTopAppBar(
    viewModel: MontageWorkflowViewModel
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text(stringResource(id = R.string.wf_top_menu_title)) },
        actions = {
            IconButton(
                onClick = { showMenu = !showMenu }
            ) { Icon(Icons.Default.MoreVert, "3 Punkte für Gryffindor") }
            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    Text(text = stringResource(id = R.string.wf_top_menu_revoke_task))
                }
            }
        }
    )
}
