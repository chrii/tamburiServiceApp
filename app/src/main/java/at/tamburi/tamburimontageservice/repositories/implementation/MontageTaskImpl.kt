package at.tamburi.tamburimontageservice.repositories.implementation

import at.tamburi.tamburimontageservice.models.*
import at.tamburi.tamburimontageservice.repositories.IMontageTaskRepository
import at.tamburi.tamburimontageservice.services.database.dao.MontageTaskDao
import java.util.*

class MontageTaskImpl(
    private val montageTaskDao: MontageTaskDao
) : IMontageTaskRepository {
    override suspend fun getAllTasks(): List<MontageTask> {
        TODO("Not yet implemented")
    }

    override suspend fun saveMockMontageTask() {
        val task = MontageTask(
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

        try {
            montageTaskDao.saveTask(
                montageId = task.montageId,
                createdAt = task.createdAt,
                remoteLocationId = task.remoteLocationId,
                magazine = "",
                ownerId = task.ownerId,
                montageStatus = task.montageStatus.ordinal,
                locationDesc = task.locationDesc,
                powerConnection = task.powerConnection.ordinal,
                montageGround = task.montageGround.type,
                montageSketch = "",
                lockerCount = 3,
                lockerTypeList = task.lockerTypeList.map { it.typeID }.toString(),
                assignedMonteurs = task.assignedMonteurs.toString(),
                scheduledInstallation = task.scheduledInstallation
            )
        } catch (e: Exception) {
            throw Exception(e.stackTraceToString())
        }
    }
}