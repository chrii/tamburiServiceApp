package at.tamburi.tamburimontageservice.ui.WorkflowLandingScreen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.LocationOwner
import at.tamburi.tamburimontageservice.ui.composables.ExpandableCard
import at.tamburi.tamburimontageservice.ui.composables.LineItemWithEllipsis

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompOwnerExpandable(
    owner: LocationOwner
) {
    ExpandableCard(
        title = stringResource(id = R.string.wf_owner_title),
        description = ""
    ) {
        LineItemWithEllipsis(
            title = stringResource(
                id = R.string.wf_owner_name
            ),
            content = owner.companyName
        )
        LineItemWithEllipsis(
            title = stringResource(id = R.string.wf_owner_contact_person),
            content = "${owner.name} ${owner.surname}"
        )
    }
}