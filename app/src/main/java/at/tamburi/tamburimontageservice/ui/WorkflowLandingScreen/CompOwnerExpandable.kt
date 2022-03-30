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
        TwoLineItem(
            cell1 = stringResource(id = R.string.wf_owner_contact_person),
            cell2 = "${owner.name} ${owner.surname}"
        )
        TwoLineItem(
            cell1 = stringResource(id = R.string.wf_owner_contact_number),
            cell2 = owner.phoneNumber
        )
        TwoLineItem(
            cell1 = stringResource(id = R.string.wf_owner_address),
            cell2 = owner.address
        )
        TwoLineItem(
            cell1 = stringResource(id = R.string.wf_owner_zip),
            cell2 = "${owner.zipCode}, ${owner.city}"
        )
    }
}