package at.tamburi.tamburimontageservice.ui.MainActivity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.databinding.ActivityMainBinding
import at.tamburi.tamburimontageservice.ui.LoginScreen.MainViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConf: AppBarConfiguration

    override fun onStart() {
        super.onStart()
        viewModel.getActiveUser(this, lifecycle)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getActiveUser(this, lifecycle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel.getActiveUser(this, lifecycle)
        setContentView(binding.root)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        appBarConf = AppBarConfiguration(
            setOf(R.id.fragment_task_list, R.id.fragment_service_list),
            drawerLayout
        )

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConf)

        val headerView = binding.navView.getHeaderView(0)
        val composeHeader = headerView
            .findViewById<ComposeView>(R.id.sidebar_header_compose_view)
        composeHeader.setContent {
            SidebarHeaderCompose(viewModel)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConf) || super.onSupportNavigateUp()
    }
}