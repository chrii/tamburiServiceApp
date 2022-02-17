package at.tamburi.tamburimontageservice.services.database.dao

import androidx.room.Dao
import androidx.room.Query
import at.tamburi.tamburimontageservice.services.database.entities.LockerEntity

@Dao
interface LockerDao {
    @Query("SELECT * FROM lockers WHERE locker_id = :lockerId")
    suspend fun getLockerById(lockerId: Int): LockerEntity?

    @Query(
        "INSERT INTO lockers (locker_id, type_id, type_name, qr_code, gateway)" +
                "VALUES (:lockerId, :typeId, :typeName, :qrCode, :gateway)"
    )
    suspend fun saveLocker(
        lockerId: Int,
        typeId: Int,
        typeName: String,
        qrCode: String,
        gateway: Boolean
    ): Long
}