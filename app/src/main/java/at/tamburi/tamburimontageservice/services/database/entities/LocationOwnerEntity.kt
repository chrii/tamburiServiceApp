package at.tamburi.tamburimontageservice.services.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "owner")
data class LocationOwnerEntity(
    @PrimaryKey
    val id: Int,

    @NonNull
    @ColumnInfo(name = "owner_id")
    val ownerId: Int,

    @NonNull
    @ColumnInfo(name = "company_name")
    val companyName: String,

    @NonNull
    @ColumnInfo(name = "address")
    val address: String,

    @NonNull
    @ColumnInfo(name = "street_number")
    val streetNumber: String,

    @NonNull
    @ColumnInfo(name = "zip")
    val zipCode: String
)