package com.v2ray.ang.compose.di

import android.app.Application
import android.content.Context
import com.v2ray.ang.compose.data.repository.ServersRepositoryImp
import com.v2ray.ang.compose.domain.repository.ServersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideServersRepository(
        @ApplicationContext context: Context
    ): ServersRepository {
        return ServersRepositoryImp(context)
    }
}