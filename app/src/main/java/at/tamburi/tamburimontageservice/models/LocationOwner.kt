package at.tamburi.tamburimontageservice.models

data class LocationOwner(
    val buildingOwnerId: Int,
    val companyName: String,
    val name: String,
    val surname: String,
    val address: String,
    val address2: String,
    val city: String,
    val zipCode: String,
    val email: String,
    val phoneNumber: String
)
