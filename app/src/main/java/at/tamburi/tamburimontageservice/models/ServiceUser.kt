package at.tamburi.tamburimontageservice.models

data class ServiceUser(
    val userId: Int,
    val username: String,
    val loginDate: Long,
    val magazineId: Int?,
    val assignedMontageTaskId: Int?
    )