package at.tamburi.tamburimontageservice.ui.composables

import androidx.compose.runtime.Composable

@Composable
fun LineItemWithEllipsis(title: String, content: String, ellipsis: Int = 15) {
    if(content.length > ellipsis) {
        TwoLineExpandable(title = title, content = content, ellipsis = ellipsis)
    } else {
        TwoLineItem(cell1 = title, cell2 = content)
    }
}