package at.tamburi.tamburimontageservice.models

enum class MontageStatus {
    CREATED,
    ASSIGNED,
    ACTIVE,
    CLOSED,
    MAINTENANCE
}

enum class PowerConnection {
    STATIONARY,
    BATTERY
}

enum class MontageGround(val type: String) {
    GRAS("Gras"),
    CONCRETE("Concrete"),
    LAVA("Lava")
}