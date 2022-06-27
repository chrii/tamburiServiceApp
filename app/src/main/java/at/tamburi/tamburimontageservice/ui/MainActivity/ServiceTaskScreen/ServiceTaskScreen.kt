package at.tamburi.tamburimontageservice.ui.MainActivity.ServiceTaskScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme

private const val TAG = "ServiceTaskScreen"

class ServiceTaskScreen : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        Log.d(TAG, "got called")
        return ComposeView(requireContext()).apply {
            setContent {
                TamburiMontageServiceTheme() {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.primary
                    ) {
                        Text("Hello", color = Color.White)
                    }
                }
            }
        }
    }
}