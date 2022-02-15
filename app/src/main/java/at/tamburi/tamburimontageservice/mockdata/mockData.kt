package at.tamburi.tamburimontageservice.mockdata

import at.tamburi.tamburimontageservice.models.*
import java.util.*


val taskListMockData = listOf(
    MontageTask(
        montageId = 1,
        createdAt = Date().time,
        remoteLocationId = 1,
        remoteLocation = RemoteLocation(
            locationId = 2,
            countryId = 1,
            cityId = 1,
            zipCode = "1219",
            streetName = "Kärntner Straße",
            streetNumber = "23",
            qrCode = "a1b2",
            minimumReservationTime = 5,
            minimumPauseTime = 3
        ),
        magazine = "",
        ownerId = 1,
        locationOwner = LocationOwner(
            companyId = 1,
            companyName = "GESIBA",
            address = "Gesiba Straße",
            streetNumber = "14",
            zipCode = "1140"
        ),
        montageStatus = MontageStatus.ASSIGNED,
        locationDesc = "This is a test description",
        powerConnection = PowerConnection.BATTERY,
        montageGround = MontageGround.GRAS,
        montageSketch = null,
        lockerCount = 2,
        lockerTypeList = listOf(
            LockerType(1),
            LockerType(1)
        ),
        assignedMonteurs = listOf(1),
        scheduledInstallation = Date().time,
    ),
    MontageTask(
        montageId = 2,
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
        magazine = "",
        ownerId = 1,
        locationOwner = LocationOwner(
            companyId = 1,
            companyName = "GESIBA",
            address = "Gesiba Straße",
            streetNumber = "14",
            zipCode = "1140"
        ),
        montageStatus = MontageStatus.ASSIGNED,
        locationDesc = "This is a test description",
        powerConnection = PowerConnection.STATIONARY,
        montageGround = MontageGround.LAVA,
        montageSketch = null,
        lockerCount = 2,
        lockerTypeList = listOf(
            LockerType(1),
            LockerType(1)
        ),
        assignedMonteurs = listOf(1),
        scheduledInstallation = Date().time,
    )
)

//Imitates the owner request
val ownerMockList = listOf(
    LocationOwner(
        companyId = 1,
        companyName = "GESIBA",
        address = "Gesiba Straße",
        streetNumber = "14",
        zipCode = "1140"
    )
)