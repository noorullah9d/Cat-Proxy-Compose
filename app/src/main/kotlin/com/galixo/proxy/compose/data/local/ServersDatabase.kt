package com.galixo.proxy.compose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.galixo.proxy.compose.data.local.entities.ServerEntity
import com.galixo.proxy.compose.data.local.dao.ServerDao

@Database(entities = [ServerEntity::class], version = 1, exportSchema = false)
abstract class ServersDatabase : RoomDatabase() {
    abstract val serverDao : ServerDao
}