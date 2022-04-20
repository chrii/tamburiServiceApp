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

    @Query("UPDATE remote_location SET longitude = :longitude, latitude = :latitude WHERE location_id= :locationId")
    suspend fun setGPSData(
        longitude: Double,
        latitude: Double,
        locationId: Int
    ): Int

    @Query(
        "INSERT INTO remote_location (location_id, location_name, country_id, city_id, zip_code, street, number, qr_code, city_name, country_name, longitude, latitude) " +
                "VALUES (:locationId, :locationName,:countryId,:cityId,:zipCode,:street,:number,:qrCode,:cityName,:countryName, :longitude, :latitude)"
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
        countryName: String,
        longitude: Double,
        latitude: Double
    ): Long
}
