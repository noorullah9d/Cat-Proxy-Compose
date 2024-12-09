package com.v2ray.ang.compose.di

import android.content.Context
import androidx.room.Room
import com.v2ray.ang.compose.data.local.ServersDatabase
import com.v2ray.ang.compose.data.repository.ServersDBRepoImpl
import com.v2ray.ang.compose.domain.repository.ServersDBRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideProxyDatabase(
        @ApplicationContext context: Context
    ): ServersDatabase {
        return Room.databaseBuilder(
            context,
            ServersDatabase::class.java,
            "servers_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideServersDbRepo(
        database: ServersDatabase
    ): ServersDBRepo = ServersDBRepoImpl(database)
}