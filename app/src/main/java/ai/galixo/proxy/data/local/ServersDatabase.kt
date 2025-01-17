package ai.galixo.proxy.data.local

import ai.galixo.proxy.data.local.dao.ServerDao
import androidx.room.Database
import androidx.room.RoomDatabase
import ai.galixo.proxy.data.local.entities.ServerEntity

@Database(entities = [ServerEntity::class], version = 1, exportSchema = false)
abstract class ServersDatabase : RoomDatabase() {
    abstract val serverDao : ServerDao
}