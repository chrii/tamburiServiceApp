package at.tamburi.tamburimontageservice.utils

import android.annotation.SuppressLint
import at.tamburi.tamburimontageservice.models.MontageTask
import java.text.DateFormat

class Utils {
    companion object {
        @SuppressLint
        fun getReadableScheduleDate(task: MontageTask): String {
            return DateFormat.getDateInstance().format(task.scheduledInstallationDate)
        }
    }
}