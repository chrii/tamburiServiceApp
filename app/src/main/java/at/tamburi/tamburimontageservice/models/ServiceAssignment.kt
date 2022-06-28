package at.tamburi.tamburimontageservice.models

import java.util.*

data class ServiceAssignment(
    val assignmentId: Int,
    val locationId: Int,
    val locationName: String,
    val locationAddress: String,
    val zipCode: String,
    val city: String,
    val datum: Date
)