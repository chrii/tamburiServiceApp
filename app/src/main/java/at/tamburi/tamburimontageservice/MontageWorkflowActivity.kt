package at.tamburi.tamburimontageservice

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

private const val TAG = "MontageWorkflowActivity"

class MontageWorkflowActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContentView(R.layout.activity_motnage_workflow)
        Log.d(TAG, "Reached:")
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_montage_workflow) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
    }
}