package at.tamburi.tamburimontageservice

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel
import at.tamburi.tamburimontageservice.ui.ViewModels.State
import at.tamburi.tamburimontageservice.utils.Constants
import com.google.accompanist.permissions.PermissionState
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MontageWorkflowActivity"

@AndroidEntryPoint
class MontageWorkflowActivity : AppCompatActivity() {
    val viewModel: MontageWorkflowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_montage_workflow)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_montage_workflow) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
    }
}