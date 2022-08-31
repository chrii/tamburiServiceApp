package at.tamburi.tamburimontageservice.ui.MainActivity.ServiceTaskDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainState
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainViewModel
import at.tamburi.tamburimontageservice.ui.composables.CustomLoadingIndicator

private const val TAG = "ServiceDetailsFragment"

class ServiceTaskDetailsFragment : Fragment() {
    val viewModel: MainViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        viewModel.getLocationClaims(lifecycle, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                when (viewModel.mainState.value) {
                    MainState.Loading -> CustomLoadingIndicator("Get Details")
                    MainState.Ready -> {
                        if (viewModel.activeServiceLocation.value != null)
                            ServiceTaskDetailsScreen(viewModel, findNavController())
                        else {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }
}