package at.tamburi.tamburimontageservice.services.database

import at.tamburi.tamburimontageservice.models.*
import at.tamburi.tamburimontageservice.services.database.entities.*
import java.text.SimpleDateFormat
import java.util.*


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

val LocationOwnerEntity.toLocationOwner: LocationOwner
    get() = LocationOwner(
        buildingOwnerId = buildingOwnerId,
        companyName = companyName,
        name = name,
        surname = surname,
        address = address,
        address2 = address2,
        city = city,
        zipCode = zipCode,
        email = email,
        phoneNumber = phoneNumber,
    )

//
val RemoteLocationEntity.toRemoteLocation: RemoteLocation
    get() = RemoteLocation(
        locationId = locationId,
        locationName = locationName,
        countryId = countryId,
        cityId = cityId,
        zipCode = zipCode,
        street = street,
        number = number,
        qrCode = qrCode,
        cityName = cityName,
        countryName = countryName
    )

val LockerEntity.toLocker: Locker
    get() = Locker(
        lockerId = lockerId,
        locationId = locationId,
        lockerName = lockerName,
        lockerType = lockerType,
        columnNumber = columnNumber,
        montageTaskId = montageTaskId,
        typeName = typeName,
        gateway = gateway,
        gatewaySerialnumber = gatewaySerialnumber,
        qrCode = qrCode,
        busSlot = busSlot
    )

fun MontageTaskEntity.toMontageTask(
    owner: LocationOwner?,
    location: RemoteLocation,
    lockers: List<Locker>,
    serviceUserList: List<ServiceUser>
): MontageTask {
    return MontageTask(
        montageTaskId = montageTaskId,
        creationDate = Date(creationDate),
        location = location,
        locationOwner = owner,
        montageStatus = montageStatus,
        locationDescription = locationDescription,
        powerConnection = powerConnection,
        montageGroundName = montageGroundName,
        montageSketchUrl = montageSketchUrl,
        montageSketchImage = null,
        servicemanList = serviceUserList,
        scheduledInstallationDate = Date(scheduledInstallationDate),
        montageHint = montageHint,
        lockerList = lockers,
    )
}
