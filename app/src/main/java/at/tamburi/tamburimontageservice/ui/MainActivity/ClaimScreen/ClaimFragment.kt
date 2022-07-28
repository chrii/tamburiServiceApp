package at.tamburi.tamburimontageservice.ui.MainActivity.ClaimScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainState
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainViewModel
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme

class ClaimFragment : Fragment() {
    val viewModel: MainViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TamburiMontageServiceTheme {
                    Surface {
                        when (viewModel.mainState.value) {
                            MainState.Loading -> CircularProgressIndicator()
                            MainState.Ready -> ClaimScreen(
                                viewModel = viewModel,
                                findNavController()
                            )
                        }
                    }
                }
            }
        }
    }
}