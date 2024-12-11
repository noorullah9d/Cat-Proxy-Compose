package com.galixo.proxy.compose.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.galixo.proxy.compose.data.local.entities.ServerEntity

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