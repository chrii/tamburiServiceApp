package at.tamburi.tamburimontageservice.services.database

import at.tamburi.tamburimontageservice.models.*
import at.tamburi.tamburimontageservice.services.database.entities.*
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
val RemoteLocationEntity.toMontageLocation: MontageLocation
    get() = MontageLocation(
        locationId = locationId,
        locationName = locationName ?: "",
        countryId = countryId,
        cityId = cityId,
        zipCode = zipCode ?: "No Zip Code",
        street = street ?: "No Street Name",
        number = number ?: "No Street Number",
        qrCode = qrCode ?: "",
        cityName = cityName ?: "No City Name",
        countryName = countryName ?: "No Country Name",
        longitude = longitude ?: 0.0,
        latitude = latitude ?: 0.0,
        contactPerson = contactPerson ?: "No contact Person",
        contactPhone = contactPhone ?: "No Phone"
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
    location: MontageLocation,
    lockers: List<Locker>,
    serviceUserList: List<ServiceUser>
): MontageTask {
    return MontageTask(
        montageTaskId = montageTaskId,
        creationDate = Date(creationDate),
        location = location,
        locationOwner = owner,
        statusId = statusId,
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
