package com.galixo.proxy.compose.di

import android.content.Context
import com.galixo.proxy.compose.data.repository.AppsRepositoryImp
import com.galixo.proxy.compose.domain.repository.AppsRepository
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
    @Singleton
    fun provideAppsRepository(
        @ApplicationContext context: Context
    ): AppsRepository {
        return AppsRepositoryImp(context)
    }
}