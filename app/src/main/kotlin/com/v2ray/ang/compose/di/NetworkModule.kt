package com.v2ray.ang.compose.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.v2ray.ang.R
import com.v2ray.ang.compose.data.remote.ApiService
import com.v2ray.ang.compose.data.repository.CypherRepositoryImpl
import com.v2ray.ang.compose.data.repository.PingRepositoryImpl
import com.v2ray.ang.compose.data.repository.ServersRepositoryImp
import com.v2ray.ang.compose.domain.repository.CypherRepository
import com.v2ray.ang.compose.domain.repository.PingRepository
import com.v2ray.ang.compose.domain.repository.ServersDBRepo
import com.v2ray.ang.compose.domain.repository.ServersRepository
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .addInterceptor(authInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    @BaseApiRetrofit
    @Provides
    @Singleton
    fun provideRetrofit(
        @ApplicationContext context: Context,
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }


    @Provides
    @Singleton
    fun provideApiService(
        @BaseApiRetrofit retrofit: Retrofit
    ): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideCypherRepo(): CypherRepository {
        return CypherRepositoryImpl()
    }

    @Provides
    @Singleton
    fun providePingRepo(): PingRepository {
        return PingRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideServersRepository(
        @ApplicationContext context: Context,
        apiService: ApiService,
        cypherRepository: CypherRepository,
        pingRepository: PingRepository,
        serversDBRepo: ServersDBRepo
    ): ServersRepository {
        return ServersRepositoryImp(context, apiService, cypherRepository, pingRepository, serversDBRepo)
    }
}