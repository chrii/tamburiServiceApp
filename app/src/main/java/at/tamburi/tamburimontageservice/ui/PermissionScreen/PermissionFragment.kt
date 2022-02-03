package at.tamburi.tamburimontageservice.ui.PermissionScreen

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.composables.ObservePermissionRequests
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme
import at.tamburi.tamburimontageservice.utils.isPermanentlyRevoked
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class PermissionFragment : Fragment() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TamburiMontageServiceTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        val permissionsState = rememberMultiplePermissionsState(
                            permissions = listOf(
                                Manifest.permission.CAMERA
                            )
                        )
                        val lifecycleOwner = LocalLifecycleOwner.current
                        ObservePermissionRequests(
                            lifecycleOwner = lifecycleOwner,
                            permissionsState = permissionsState
                        )
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            permissionsState.permissions.forEach { perm ->
                                when (perm.permission) {
                                    Manifest.permission.CAMERA -> {
                                        when {
                                            perm.hasPermission -> next()
                                            perm.shouldShowRationale -> Text(text = stringResource(R.string.camera_permission_declined))
                                            perm.isPermanentlyRevoked -> Text(
                                                text = stringResource(
                                                    id = R.string.camera_permission_permanently_revoked
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun next() {
        findNavController().navigate(R.id.action_permission_to_login_screen)
    }
}

