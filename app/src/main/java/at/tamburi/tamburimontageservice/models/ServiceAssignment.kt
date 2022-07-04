package at.tamburi.tamburimontageservice.models

import java.util.*

data class ServiceAssignment(
    val assignmentId: Int,
    val servicemanId: Int,
    val location: Location,
    val scheduledDate: Date
)