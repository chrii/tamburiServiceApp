package at.tamburi.tamburimontageservice.ui.MainActivity.ServiceTaskFragment

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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainViewModel
import at.tamburi.tamburimontageservice.ui.MainActivity.ServiceTaskScreen.ServiceTaskScreen
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme

private const val TAG = "ServiceTaskFragment"


class ServiceTaskFragment : Fragment() {
    val viewModel: MainViewModel by activityViewModels()
    override fun onResume() {
        super.onResume()
        viewModel.initializeServiceData(requireContext(), lifecycle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        viewModel.initializeServiceData(requireContext(), lifecycle)
        Log.d(TAG, "got called")
        return ComposeView(requireContext()).apply {
            setContent {
                ServiceTaskScreen(viewModel, findNavController())
            }
        }
    }
}