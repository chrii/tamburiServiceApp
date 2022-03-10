package at.tamburi.tamburimontageservice.services.database.dao

import androidx.room.Dao
import androidx.room.Query
import at.tamburi.tamburimontageservice.services.database.entities.RemoteLocationEntity

@Dao
interface RemoteLocationDao {
    @Query("SELECT * FROM remote_location WHERE location_id = :locationId")
    suspend fun getLocationById(locationId: Int): RemoteLocationEntity?

    @Query("UPDATE remote_location SET qr_code = :qrCode WHERE location_id = :locationId")
    suspend fun setQrCodeForLocation(locationId: Int, qrCode: String): Int

    @Query(
        "INSERT INTO remote_location (location_id, location_name, country_id, city_id, zip_code, street, number, qr_code, city_name, country_name) " +
                "VALUES (:locationId, :locationName,:countryId,:cityId,:zipCode,:street,:number,:qrCode,:cityName,:countryName)"
    )
    suspend fun saveLocation(
        locationId: Int,
        locationName: String,
        countryId: Int,
        cityId: Int,
        zipCode: String,
        street: String,
        number: String,
        qrCode: String,
        cityName: String,
        countryName: String
    ): Long
}
