package at.tamburi.tamburimontageservice.models

data class LocationOwner(
    val companyId: Int,
    val companyName: String,
    val address: String,
    val streetNumber: String,
    val zipCode: String,
)
