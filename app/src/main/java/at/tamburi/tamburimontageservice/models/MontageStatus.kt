package at.tamburi.tamburimontageservice.models

import androidx.annotation.StringRes
import at.tamburi.tamburimontageservice.R

object MontageStatus {
    const val CREATED: Int = 1
    const val ASSIGNED: Int = 2
    const val ACTIVE: Int = 3
    const val CLOSED: Int = 4
    const val MAINTENANCE: Int = 5

    fun getStatusString(status: Int): Int = when (status){
        CREATED -> R.string.task_status_created
        ASSIGNED -> R.string.task_status_assigned
        ACTIVE -> R.string.task_status_active
        CLOSED -> R.string.task_status_closed
        MAINTENANCE -> R.string.task_status_maintenance
        else -> R.string.task_status_else
    }
}