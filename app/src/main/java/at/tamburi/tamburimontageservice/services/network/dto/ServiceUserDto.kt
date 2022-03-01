package at.tamburi.tamburimontageservice.services.network.dto

data class ServiceUserDto(
    var servicemanId: Int,
    var username: String,
    var name: String,
    var surname: String,
    var phone: String?,
    var email: String?
)