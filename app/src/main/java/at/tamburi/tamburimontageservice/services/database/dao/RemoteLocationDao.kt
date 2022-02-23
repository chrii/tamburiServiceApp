package at.tamburi.tamburimontageservice.services.database.dao

import androidx.room.Dao
import androidx.room.Query
import at.tamburi.tamburimontageservice.services.database.entities.RemoteLocationEntity

@Dao
interface RemoteLocationDao {
    @Query("SELECT * FROM remote_location WHERE location_id = :locationId")
    suspend fun getLocationById(locationId: Int): RemoteLocationEntity?

    @Query(
        "INSERT INTO remote_location (location_id,qr_code,location_name,country_id,city_id,zip,street_name,street_number,minimum_reservation_time,minimum_pause_time) " +
                "VALUES (:locationId,:qrCode,:locationName,:countryId,:cityId,:zipCode,:streetName,:streetNumber,:minimumReservationTime,:minimumPauseTime)"
    )
    suspend fun saveLocation(
        locationId: Int,
        qrCode: String?,
        locationName: String?,
        countryId: Int,
        cityId: Int,
        zipCode: String,
        streetName: String,
        streetNumber: String,
        minimumReservationTime: Int,
        minimumPauseTime: Int
    )
}