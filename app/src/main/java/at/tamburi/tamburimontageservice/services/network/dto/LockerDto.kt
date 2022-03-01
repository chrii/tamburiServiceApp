package at.tamburi.tamburimontageservice.services.network.dto

data class LockerDto(
    var lockerId: Int,
    var locationId: Int,
    var lockerName: String,
    var lockerType: Int,
    var columnNumber: Int,
    var montageTaskId: Int,
    var typeName: String,
    var gateway: Boolean,
    var gatewaySerialnumber: Int,
    var qrCode: String?
)