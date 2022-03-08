package at.tamburi.tamburimontageservice.repositories.database.implementation

import android.util.Log
import at.tamburi.tamburimontageservice.models.*
import at.tamburi.tamburimontageservice.repositories.database.IDatabaseMontageTaskRepository
import at.tamburi.tamburimontageservice.services.database.*
import at.tamburi.tamburimontageservice.services.database.dao.*
import at.tamburi.tamburimontageservice.utils.DataState

private const val TAG = "MontageTaskImpl"

class DatabaseMontageTaskImpl(
    private val montageTaskDao: MontageTaskDao,
    private val ownerDao: LocationOwnerDao,
    private val lockerDao: LockerDao,
    private val locationDao: RemoteLocationDao,
    private val serviceUserDao: UserDao
) : IDatabaseMontageTaskRepository {
    private suspend fun getOwnerByIdOrNull(id: Int): LocationOwner? {
        return if (id == -1) null else {
            ownerDao.getOwnerById(id)?.toLocationOwner
        }
    }

    override suspend fun getTaskById(id: Int): DataState<MontageTask> {
        return try {
            val taskEntity =
                montageTaskDao.getTaskByTaskId(id) ?: throw Exception("No Task found in database")
            val owner: LocationOwner? = getOwnerByIdOrNull(taskEntity.ownerId)
            val location: RemoteLocation =
                locationDao.getLocationById(taskEntity.locationId)?.toRemoteLocation
                    ?: throw Exception("No Location found in database")
            val lockerIdList = Locker.lockerIdList(taskEntity.lockerList)
            val lockerList: List<Locker> = lockerIdList.map {
                lockerDao.getLockerById(it)?.toLocker
                    ?: throw Exception("Locker not found in Database - ID: $it")
            }
            val userIdList = ServiceUser.serviceUserIdList(taskEntity.servicemanList)
            val assignedServiceUserList: List<ServiceUser> = userIdList.map {
                serviceUserDao.getUserById(it)?.toServiceUser
                    ?: throw Exception("User not found in database")
            }
            DataState(
                hasData = true,
                data = taskEntity.toMontageTask(
                    owner = owner,
                    location = location,
                    lockers = lockerList,
                    serviceUserList = assignedServiceUserList
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            DataState(hasData = false, data = null, message = "Failed to get Data")
        }
    }

    override suspend fun saveTasks(tasks: List<MontageTask>): DataState<List<MontageTask>> {
        try {
            val result = tasks.map {
                val task = getTaskById(it.montageTaskId)
                if (it.locationOwner != null) saveOwner(it.locationOwner)
                saveLocation(it.location)
                it.servicemanList.map { user -> saveUser(user) }
                it.lockerList.map { locker -> saveLocker(locker) }

                Log.d(TAG, "Result has data: ${task.hasData}")
                Log.d(TAG, "DATA: ${task.data}")
                if (!task.hasData) {
                    val saveResult = montageTaskDao.saveTask(
                        montageTaskId = it.montageTaskId,
                        creationDate = it.creationDate.time,
                        locationId = it.location.locationId,
                        ownerId = it.locationOwner?.buildingOwnerId ?: -1,
                        montageStatus = it.montageStatus,
                        locationDescription = it.locationDescription,
                        powerConnection = it.powerConnection,
                        montageGroundName = it.montageGroundName,
                        montageSketchUrl = it.montageSketchUrl ?: "",
                        montageHint = it.montageHint,
                        lockerList = Locker.lockerIdToString(it.lockerList),
                        servicemanList = ServiceUser.toIdList(it.servicemanList),
                        scheduledInstallationDate = it.scheduledInstallationDate.time,
                    )
                    if (saveResult > -1) {
                        DataState(hasData = true, data = it, message = "Saved Task successfully")
                    } else throw Exception("Couldn't save Montage Task")
                } else {
                    DataState(hasData = true, data = it, message = "Data already exists")
                }
            }
            val taskListSavingResult = result.map { it.hasData }.contains(false)
            return if (taskListSavingResult) {
                DataState(hasData = false, data = null, message = "Error saving Tasks")
            } else {
                DataState(hasData = true, data = tasks, message = "Saved Tasks successfully")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Saving Tasks was not successful - Stacktrace")
        }
    }

    private suspend fun saveOwner(owner: LocationOwner): DataState<LocationOwner> {
        return try {
            val result = ownerDao.getOwnerById(owner.buildingOwnerId)
            if (result != null) {
                DataState(hasData = true, owner, message = "Owner already in Database")
            } else {
                val saveResult = ownerDao.saveOwner(
                    buildingOwnerId = owner.buildingOwnerId,
                    companyName = owner.companyName,
                    name = owner.name,
                    surname = owner.surname,
                    address = owner.address,
                    address2 = owner.address2,
                    city = owner.city,
                    zipCode = owner.zipCode,
                    email = owner.email,
                    phoneNumber = owner.phoneNumber,
                )
                if (saveResult > -1) {
                    DataState(hasData = true, data = owner, message = "Saved owner successful")
                } else {
                    DataState(hasData = false, data = null, message = "Owner already exist")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Couldn't save Location Owner")
        }
    }

    private suspend fun saveLocation(location: RemoteLocation): DataState<RemoteLocation> {
        return try {
            val result = locationDao.getLocationById(location.locationId)
            if (result != null) {
                DataState(hasData = true, data = location, message = "Location already exist")
            } else {
                val savingResult = locationDao.saveLocation(
                    locationId = location.locationId,
                    countryId = location.countryId,
                    cityId = location.cityId,
                    zipCode = location.zipCode,
                    street = location.street,
                    number = location.number,
                    qrCode = location.qrCode,
                    cityName = location.cityName,
                    countryName = location.countryName,
                )
                if (savingResult > -1) {
                    DataState(
                        hasData = true,
                        data = location,
                        message = "Saving location successful"
                    )
                } else {
                    DataState(
                        hasData = false,
                        data = null,
                        message = "Couldn't save Location"
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Error saving location to database")
        }
    }

    private suspend fun saveLocker(locker: Locker): DataState<Locker> {
        return try {
            val result = lockerDao.getLockerById(locker.lockerId)
            if (result != null) {
                DataState(hasData = true, locker, message = "Locker already exist")
            } else {
                val saveResult = lockerDao.saveLocker(
                    lockerId = locker.lockerId,
                    locationId = locker.locationId,
                    lockerName = locker.lockerName,
                    lockerType = locker.lockerType,
                    columnNumber = locker.columnNumber,
                    montageTaskId = locker.montageTaskId,
                    typeName = locker.typeName,
                    gateway = locker.gateway,
                    gatewaySerialnumber = locker.gatewaySerialnumber,
                    qrCode = locker.qrCode,
                    busSlot = locker.busSlot ?: 0
                )
                if (saveResult > -1) {
                    DataState(hasData = true, data = locker, message = "Locker saved successfully ")
                } else {
                    DataState(hasData = false, data = null, "Couldn't save Locker")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Failed to save Locker")
        }
    }

    private suspend fun saveUser(serviceUser: ServiceUser): DataState<ServiceUser> {
        return try {
            val result = serviceUserDao.getUserById(serviceUser.servicemanId)
            if (result != null) {
                DataState(hasData = true, data = serviceUser, message = "User already exist")
            } else {
                val saveResult = serviceUserDao.saveUserEntry(
                    servicemanId = serviceUser.servicemanId,
                    username = serviceUser.username,
                    firstname = serviceUser.firstname,
                    surname = serviceUser.surname,
                    phone = serviceUser.phone,
                    email = serviceUser.email,
                    loginDate = serviceUser.loginDate
                )
                if (saveResult > 0) {
                    DataState(
                        hasData = true,
                        data = serviceUser,
                        message = "User saved successfully"
                    )
                } else {
                    DataState(hasData = false, data = null, message = "Couldn't save Service User")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Failed saving ServiceUser")
        }
    }

    override suspend fun setQrCode(qrCode: String, lockerId: Int): DataState<Boolean> {
        return try {
            lockerDao.setQrCode(qrCode, lockerId)
            DataState(
                hasData = true,
                data = true,
                message = "Success"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            DataState(
                hasData = false,
                data = false,
                message = "Error: Cant set QR Code for Locker"
            )
        }
    }

    override suspend fun setGatewaySerialnumber(
        serialnumber: String,
        lockerId: Int
    ): DataState<Boolean> {
        return try {
            lockerDao.setGatewaySerialNumber(serialnumber, lockerId)
            DataState(hasData = true, data = true, message = "Successful")
        } catch (e: Exception) {
            e.printStackTrace()
            DataState(hasData = false, data = false, message = "Setting Serialnumber failed")
        }
    }

    override suspend fun setBusSlot(lockerId: Int, busSlot: Int): DataState<Boolean> {
        return try {
            val result = lockerDao.setBusSlot(lockerId, busSlot)
            if (result >= 0) {
                DataState(hasData = true, data = true, message = "Successful")
            } else {
                DataState(hasData = false, data = false, message = "Error saving Bus Slot Number")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            DataState(hasData = false, data = false, message = "Cannot save Bus Slot Number")
        }
    }

    override suspend fun getAllTasks(): DataState<List<MontageTask>> {
        return DataState(hasData = false, data = null, message = "getAllTasks")
    }

    override suspend fun getLockersByLocationId(locationId: Int): DataState<List<Locker>> {
        return try {
            val response = lockerDao.getLockersByLocationId(locationId)
            if (response.isNotEmpty()) {
                val lockerList = response.map { it.toLocker }
                DataState(hasData = true, data = lockerList, message = "Request Successful")
            } else {
                DataState(
                    hasData = false,
                    data = null,
                    message = "No Lockers with this location id "
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            DataState(hasData = false, data = null, message = "Error getting Data from Database")
        }
    }
}