package at.tamburi.tamburimontageservice.utils

import android.annotation.SuppressLint
import at.tamburi.tamburimontageservice.models.MontageTask
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        @SuppressLint
        fun getReadableScheduleDate(task: MontageTask): String {
            return DateFormat.getDateInstance().format(task.scheduledInstallationDate)
        }

        @SuppressLint("SimpleDateFormat")
        fun getDate(mil: Date = Date()): Date {
            val simple = SimpleDateFormat("MM-dd")
            return simple.parse(simple.format(mil)) ?: throw Exception("Cannot parse date")
        }

        fun getDateFromString(dateString: String): Date {
            val simple = SimpleDateFormat("MM-dd")
            return simple.parse(dateString) ?: throw Exception("Cannot parse date")
        }
    }
}