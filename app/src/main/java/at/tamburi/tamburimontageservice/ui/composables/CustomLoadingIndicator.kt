package at.tamburi.tamburimontageservice.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.theme.Orange
import at.tamburi.tamburimontageservice.ui.theme.OrangeLight

@Composable
fun CustomLoadingIndicator(text: String = stringResource(id = R.string.progress_indicator_default_text)) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = Orange)
            Text(
                modifier =Modifier.padding(top = 8.dp),
                text = text,
                color = OrangeLight)
        }
    }
}