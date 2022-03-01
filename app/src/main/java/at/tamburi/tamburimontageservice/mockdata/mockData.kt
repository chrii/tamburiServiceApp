//package at.tamburi.tamburimontageservice.mockdata
//
//import at.tamburi.tamburimontageservice.models.*
//import java.util.*
//
//
//val taskListMockData = listOf(
//    MontageTask(
//        montageId = 1,
//        createdAt = Date().time,
//        remoteLocation = RemoteLocation(
//            locationId = 1,
//            countryId = 1,
//            cityId = 1,
//            zipCode = "1219",
//            streetName = "Kärntner Straße",
//            streetNumber = "23",
//            qrCode = "",
//            locationName = "",
//            minimumReservationTime = 5,
//            minimumPauseTime = 3
//        ),
//        magazine = "Döblerhof Straße",
//        locationOwner = LocationOwner(
//            companyId = 1,
//            companyName = "GESIBA",
//            address = "Gesiba Straße",
//            streetNumber = "14",
//            zipCode = "1140"
//        ),
//        montageStatus = MontageStatus.ASSIGNED,
//        locationDesc = "This is a test description of Kärntner Straße",
//        powerConnection = PowerConnection.BATTERY,
//        montageGround = MontageGround.GRAS,
//        montageSketch = null,
//        lockerList = listOf(
//            Locker(
//                lockerId = 1,
//                typeId = 1,
//                typeName = "18-er",
//                qrCode = "",
//                gateway = false
//            ),
//            Locker(
//                lockerId = 2,
//                typeId = 1,
//                typeName = "12-er",
//                qrCode = "",
//                gateway = false
//            ),
//            Locker(
//                lockerId = 3,
//                typeId = 1,
//                typeName = "18-er",
//                qrCode = "",
//                gateway = true
//            )
//        ),
//        assignedMonteurs = listOf(1),
//        scheduledInstallation = Date().time,
//    ),
//    MontageTask(
//        montageId = 2,
//        createdAt = Date().time,
//        remoteLocation = RemoteLocation(
//            locationId = 2,
//            countryId = 1,
//            cityId = 1,
//            zipCode = "1219",
//            streetName = "Floridusgasse",
//            streetNumber = "50",
//            qrCode = "",
//            locationName = "",
//            minimumReservationTime = 5,
//            minimumPauseTime = 3
//        ),
//        magazine = "Floridusgasse",
//        locationOwner = LocationOwner(
//            companyId = 1,
//            companyName = "GESIBA",
//            address = "Gesiba Straße",
//            streetNumber = "14",
//            zipCode = "1140"
//        ),
//        montageStatus = MontageStatus.ASSIGNED,
//        locationDesc = "This is a test description of Floridusgasse",
//        powerConnection = PowerConnection.STATIONARY,
//        montageGround = MontageGround.LAVA,
//        montageSketch = null,
//        lockerList = listOf(
//            Locker(
//                lockerId = 4,
//                typeId = 1,
//                typeName = "18-er",
//                qrCode = "",
//                gateway = false
//            ),
//            Locker(
//                lockerId = 5,
//                typeId = 1,
//                typeName = "12-er",
//                qrCode = "",
//                gateway = false
//            ),
//            Locker(
//                lockerId = 6,
//                typeId = 1,
//                typeName = "18-er",
//                qrCode = "",
//                gateway = true
//            )
//        ),
//        assignedMonteurs = listOf(1),
//        scheduledInstallation = Date().time,
//    )
//)
//
////Imitates the owner request
//val ownerMockList = listOf(
//    LocationOwner(
//        companyId = 1,
//        companyName = "GESIBA",
//        address = "Gesiba Straße",
//        streetNumber = "14",
//        zipCode = "1140"
//    )
//)
//
//val remoteLocationMockList = listOf(
//    RemoteLocation(
//        locationId = 1,
//        countryId = 1,
//        cityId = 1,
//        zipCode = "1219",
//        streetName = "Kärntner Straße",
//        streetNumber = "23",
//        qrCode = "",
//        locationName = "",
//        minimumReservationTime = 5,
//        minimumPauseTime = 3
//    ),
//    RemoteLocation(
//        locationId = 2,
//        countryId = 1,
//        cityId = 1,
//        zipCode = "1219",
//        streetName = "Floridusgasse",
//        streetNumber = "50",
//        qrCode = "",
//        locationName = "",
//        minimumReservationTime = 5,
//        minimumPauseTime = 3
//    )
//)