package at.tamburi.tamburimontageservice.models

data class Claim(
    val claimId: Int,
    val typeId: Int,
    val typeName: String,
    val tamburiCode: String,
    val tamburiPin: String,
    val compartmentWiring: Int,
    val compartmentSize: String,
    val deliveryId: Int,
    val deliveryCode: String?
)