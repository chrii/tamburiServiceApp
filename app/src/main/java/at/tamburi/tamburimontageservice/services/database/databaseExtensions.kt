package at.tamburi.tamburimontageservice.services.database

import at.tamburi.tamburimontageservice.models.*
import at.tamburi.tamburimontageservice.services.database.entities.LocationOwnerEntity
import at.tamburi.tamburimontageservice.services.database.entities.MontageTaskEntity
import at.tamburi.tamburimontageservice.services.database.entities.RemoteLocationEntity
import at.tamburi.tamburimontageservice.services.database.entities.UserEntity


//TODO: Do not forget to add magazine id if its here
val UserEntity.toServiceUser: ServiceUser
    get() = ServiceUser(
        servicemanId = servicemanId,
        username = username,
        firstname = firstname,
        surname = surname,
        phone = phone,
        email = email,
        loginDate = loginDate
    )

//val LocationOwnerEntity.toLocationOwner: LocationOwner
//    get() = LocationOwner(
//        buildingOwnerId = buildingOwnerId,
//        companyName = companyName,
//        name = name,
//        surname = surname,
//        address = address,
//        address2 = address2,
//        city = city,
//        zipCode = zipCode,
//        email = email,
//        phoneNumber = phoneNumber,
//    )
//
//val RemoteLocationEntity.toRemoteLocation: RemoteLocation
//    get() = RemoteLocation(
//        locationId = locationId,
//        countryId = countryId,
//        cityId = cityId,
//        zipCode = zipCode,
//        streetName = streetName,
//        streetNumber = streetNumber,
//        qrCode = qrCode,
//        locationName = locationName,
//        minimumReservationTime = minimumReservationTime,
//        minimumPauseTime = minimumPauseTime,
//    )
//
//val MontageTaskEntity.toMontageTask: MontageTask
//    get() = MontageTask(
//
//    )