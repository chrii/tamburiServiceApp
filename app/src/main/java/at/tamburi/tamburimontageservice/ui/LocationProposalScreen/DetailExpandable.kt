package at.tamburi.tamburimontageservice.ui.LocationProposalScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import at.tamburi.tamburimontageservice.ui.composables.ExpandableCard

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailExpandable(
    title: String,
    content: @Composable () -> Unit
) {
    ExpandableCard(title = title, description = "") {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
            Divider()
        }
    }
}
