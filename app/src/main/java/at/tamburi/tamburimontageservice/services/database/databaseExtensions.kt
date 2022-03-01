package at.tamburi.tamburimontageservice.services.database

import at.tamburi.tamburimontageservice.models.LocationOwner
import at.tamburi.tamburimontageservice.models.MontageTask
import at.tamburi.tamburimontageservice.models.RemoteLocation
import at.tamburi.tamburimontageservice.models.ServiceUser
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
//        companyId = id,
//        companyName = companyName,
//        address = address,
//        streetNumber = streetNumber,
//        zipCode = zipCode
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

//val MontageTaskEntity.toMontageTask: MontageTask
//    get() = MontageTask(
//
//    )