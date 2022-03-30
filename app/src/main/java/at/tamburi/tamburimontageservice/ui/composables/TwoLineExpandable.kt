package at.tamburi.tamburimontageservice.ui.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TwoLineExpandable(
    title: String,
    content: String,
    titleFontWeight: FontWeight = FontWeight.Bold,
    fontSize: TextUnit = 14.sp,
    ellipsis: Int = 15
) {
    var expandedState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 30,
                    easing = LinearOutSlowInEasing
                )
            )
            .padding(8.dp)
            .clickable { expandedState = !expandedState },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontWeight = titleFontWeight, fontSize = fontSize)
            if (content.length > ellipsis) {
                if (!expandedState) {
                    val ellipsisContent = cutString(content, ellipsis)
                    Text(ellipsisContent, fontSize = fontSize)
                }
            } else {
                Text(text = content, fontSize = fontSize)
            }
        }
        if (expandedState) {
            Text(content, fontSize = fontSize)
        }
    }
}

fun cutString(string: String, index: Int): String = "${string.substring(0, index)}..."