package at.tamburi.tamburimontageservice

import at.tamburi.tamburimontageservice.models.*
import java.util.*

val montageMock = listOf(
    MontageTask(
        montageId = 1,
        createdAt = Date().time,
        remoteLocationId = 1,
        remoteLocation = RemoteLocation(
            locationId = 2,
            countryId = 1,
            cityId = 1,
            zipCode = "1219",
            streetName = "Floridusgasse",
            streetNumber = "50",
            qrCode = "a1b2",
            minimumReservationTime = 5,
            minimumPauseTime = 3
        ),
        magazine = "1",
        ownerId = 1,
        locationOwner = LocationOwner(
            companyId =1,
            companyName = "GESIBA",
            address = "Gesiba Straße",
            streetNumber = "14",
            zipCode = "1140",
        ),
        montageStatus = MontageStatus.CREATED,
        locationDesc = "Eigenartiger Boden. Bitte festes Schuhwerk tragen",
        powerConnection = PowerConnection.STATIONARY,
        montageGround = MontageGround.LAVA,
        montageSketch = null,
        lockerCount = 3,
        lockerTypeList = listOf(
            LockerType(1),
            LockerType(1)
        ),
        assignedMonteurs = listOf(1, 2),
        scheduledInstallation = Date().time,
    )
)