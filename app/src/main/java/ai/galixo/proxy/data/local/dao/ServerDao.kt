package ai.galixo.proxy.data.local.dao

import ai.galixo.proxy.data.local.entities.ServerEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ServerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServerList(servers: List<ServerEntity>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServer(servers: ServerEntity): Long


    @Update
    suspend fun updateServer(servers: ServerEntity): Int

    @Query("SELECT * FROM servers_list  WHERE id =:serverId LIMIT 1")
    suspend fun checkIfServerExists(serverId: Int): ServerEntity?


    @Query("SELECT * FROM servers_list")
    suspend fun getAllServers(): List<ServerEntity>

    @Query("DELETE FROM servers_list WHERE id = :id")
    suspend fun deleteServerByID(id: Int)

}