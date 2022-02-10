package at.tamburi.tamburimontageservice

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var DEBUG = true

//    private val db by lazy { (application as BaseApplication).database }

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
    }

//    private fun setOwner() {
//        lifecycle.coroutineScope.launch {
//            val owner = db.ownerDao().getOwnerById(1)
//            if (owner == null) {
//                val result = db.ownerDao().saveOwner(
//                    ownerId = 1,
//                    companyName = "GESIBA",
//                    address = "Gesiba Straße",
//                    streetNumber = "14",
//                    zipCode = "1140"
//                )
//                Log.d(TAG, "Owner Saved state: $result")
//            }
//        }
//    }

    private fun setMontageTask() {
        lifecycle.coroutineScope.launch {

        }
    }
}