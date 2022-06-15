package at.tamburi.tamburimontageservice.services.network.dto

data class LockerRegistrationDto(
    var lockerId: Int?,
    var qrCode: String?,
    var gatewaySerialNumber: String?,
    var busSlot: Int?
)
