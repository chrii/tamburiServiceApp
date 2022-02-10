package at.tamburi.tamburimontageservice.ui.LoginScreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import at.tamburi.tamburimontageservice.BaseApplication
import at.tamburi.tamburimontageservice.repositories.IUserRepository
import javax.inject.Inject

private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        viewModel.checkUserState(lifecycle)
        return ComposeView(requireContext()).apply {
            setContent {
                LoginScreen(
                    navigation = findNavController(),
                    viewModel = viewModel
                )
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        viewModel.checkUserState(lifecycle)
//    }

    override fun onStart() {
        super.onStart()
        viewModel.checkUserState(lifecycle)
    }
}