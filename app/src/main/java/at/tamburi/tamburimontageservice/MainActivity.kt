package at.tamburi.tamburimontageservice

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.inflate
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment
import at.tamburi.tamburimontageservice.ui.LoginScreen.LoginScreen
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme
import at.tamburi.tamburimontageservice.utils.isPermanentlyRevoked
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

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
                                        perm.hasPermission -> LoginScreen(
                                            navigation = navController,
                                        )
                                        perm.shouldShowRationale -> Text(text = stringResource(R.string.camera_permission_declined))
                                        perm.isPermanentlyRevoked -> Text(text = stringResource(id = R.string.camera_permission_permanently_revoked))
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

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun ObservePermissionRequests(
    lifecycleOwner: LifecycleOwner,
    permissionsState: MultiplePermissionsState
) {
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionsState.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )
}