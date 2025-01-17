package ai.galixo.proxy.di

import ai.galixo.proxy.data.local.ServersDatabase
import ai.galixo.proxy.domain.repository.ServersDBRepo
import android.content.Context
import androidx.room.Room
import ai.galixo.proxy.data.repository.ServersDBRepoImpl
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