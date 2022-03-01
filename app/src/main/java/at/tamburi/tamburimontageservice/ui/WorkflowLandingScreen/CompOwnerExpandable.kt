package at.tamburi.tamburimontageservice.ui.WorkflowLandingScreen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.models.LocationOwner
import at.tamburi.tamburimontageservice.ui.composables.ExpandableCard
import at.tamburi.tamburimontageservice.ui.composables.TwoLineItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompOwnerExpandable(
    owner: LocationOwner
) {
    ExpandableCard(
        title = stringResource(id = R.string.wf_owner_title),
        description = ""
    ) {
        TwoLineItem(
            cell1 = stringResource(
                id = R.string.wf_owner_name
            ),
            cell2 = owner.companyName
        )
        // TODO: Hardcoded. The LocationOwner Model should contain this information
        TwoLineItem(
            cell1 = stringResource(id = R.string.wf_owner_contact_person),
            cell2 = "Luisa Mustermann"
        )
        // TODO: Hardcoded. The LocationOwner Model should contain this information
        TwoLineItem(
            cell1 = stringResource(id = R.string.wf_owner_contact_number),
            cell2 = "+43 664 23 45 34"
        )
        TwoLineItem(
            cell1 = stringResource(id = R.string.wf_owner_address),
            cell2 = "${owner.address} ${owner.address}"
        )
        TwoLineItem(
            cell1 = stringResource(id = R.string.wf_owner_zip),
            cell2 = owner.zipCode
        )
    }
}