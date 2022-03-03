package at.tamburi.tamburimontageservice.services.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "owner")
data class LocationOwnerEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "building_owner_id")
    val buildingOwnerId: Int,
    @NonNull
    @ColumnInfo(name = "company_name")
    val companyName: String,
    @NonNull
    @ColumnInfo(name = "name")
    val name: String,
    @NonNull
    @ColumnInfo(name = "surname")
    val surname: String,
    @NonNull
    @ColumnInfo(name = "address")
    val address: String,
    @NonNull
    @ColumnInfo(name = "address2")
    val address2: String,
    @NonNull
    @ColumnInfo(name = "city")
    val city: String,
    @NonNull
    @ColumnInfo(name = "zip_code")
    val zipCode: String,
    @NonNull
    @ColumnInfo(name = "email")
    val email: String,
    @NonNull
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String
)