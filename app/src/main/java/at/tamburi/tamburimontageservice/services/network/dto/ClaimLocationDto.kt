package at.tamburi.tamburimontageservice.services.network.dto

data class ClaimLocationDto(
    val locationId: Int?,
    val locationName: String?,
    val countryId: Int?,
    val cityId: Int?,
    val zipCode: String?,
    val street: String?,
    val number: String?,
    val qrCode: String?,
    val cityName: String?,
    val countryName: String?,
    val latitude: Double?,
    val longitude: Double?,
    val contactPerson: String?,
    val contactPhone: String?,
    val assignmentId: Int?
)