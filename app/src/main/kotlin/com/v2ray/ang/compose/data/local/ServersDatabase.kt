package com.v2ray.ang.compose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.v2ray.ang.compose.data.local.entities.ServerEntity
import com.v2ray.ang.compose.data.local.dao.ServerDao

@Database(entities = [ServerEntity::class], version = 1, exportSchema = false)
abstract class ServersDatabase : RoomDatabase() {
    abstract val serverDao : ServerDao
}