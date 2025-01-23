package ai.galixo.proxy.di

import ai.galixo.proxy.data.remote.LocationApi
import ai.galixo.proxy.data.repository.LocationRepositoryImpl
import ai.galixo.proxy.domain.repository.LocationRepository
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun provideApiService(
        @LocationApiRetrofit retrofit: Retrofit
    ): LocationApi = retrofit.create(LocationApi::class.java)

    @Provides
    @Singleton
    fun provideLocationRepository(
        context: Context,
        apiService: LocationApi
    ): LocationRepository {
        return LocationRepositoryImpl(context, apiService)
    }
}
