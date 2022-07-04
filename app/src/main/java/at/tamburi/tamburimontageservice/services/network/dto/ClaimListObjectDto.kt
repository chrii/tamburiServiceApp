package at.tamburi.tamburimontageservice.services.network.dto

import at.tamburi.tamburimontageservice.models.Location

data class ClaimListObjectDto(
    val assignmentId: Int?,
    val location: Location?,
    val scheduledDate: String?,
    val servicemanId: Int?
)