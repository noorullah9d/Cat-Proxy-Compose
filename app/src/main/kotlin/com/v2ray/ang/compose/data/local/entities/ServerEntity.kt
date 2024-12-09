package com.v2ray.ang.compose.data.local.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey


@Keep
@Entity(tableName = "servers_list")
data class ServerEntity(
    @PrimaryKey val id: Int,
    val ipaddress: String,
    val password: String,
    val countryName: String,
    val cityName: String,
    val type: String,
    val flag: String,
    val protocol: String,
    val port: String,
    val encryption: String
)