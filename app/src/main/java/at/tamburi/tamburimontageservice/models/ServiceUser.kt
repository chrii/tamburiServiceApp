package at.tamburi.tamburimontageservice.models

data class ServiceUser(
    val userId: Int,
    val username: String,
    val loginDate: Int,
    val magazineId: Int?,
    val assignedMontageTaskId: Int?
    )