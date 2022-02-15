package at.tamburi.tamburimontageservice

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

private const val TAG = "MontageWorkflowActivity"

class MontageWorkflowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "Reached")
        setContentView(R.layout.activity_montage_workflow)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_montage_workflow) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
    }
}