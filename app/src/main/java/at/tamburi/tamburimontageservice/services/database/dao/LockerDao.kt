package at.tamburi.tamburimontageservice.services.database.dao

import androidx.room.Dao
import androidx.room.Query
import at.tamburi.tamburimontageservice.services.database.entities.LockerEntity

@Dao
interface LockerDao {
    @Query("SELECT * FROM lockers WHERE locker_id = :lockerId")
    suspend fun getLockerById(lockerId: Int): LockerEntity?

    @Query("UPDATE lockers SET qr_code = :qrCode WHERE locker_id = :lockerId")
    suspend fun setQrCode(qrCode: String, lockerId: Int): Int

    @Query(
        "INSERT INTO lockers (locker_id, location_id, locker_name, locker_type, column_number, montage_task_id, type_name, gateway, gateway_serial_number, qr_code)" +
                "VALUES (:lockerId,:locationId,:lockerName,:lockerType,:columnNumber,:montageTaskId,:typeName,:gateway,:gatewaySerialnumber,:qrCode)"
    )
    suspend fun saveLocker(
        lockerId: Int,
        locationId: Int,
        lockerName: String,
        lockerType: Int,
        columnNumber: Int,
        montageTaskId: Int,
        typeName: String,
        gateway: Boolean,
        gatewaySerialnumber: String,
        qrCode: String
    ): Long
}
