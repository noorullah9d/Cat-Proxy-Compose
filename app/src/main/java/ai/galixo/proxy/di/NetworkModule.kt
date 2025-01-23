package ai.galixo.proxy.di

import ai.galixo.proxy.R
import ai.galixo.proxy.domain.repository.CypherRepository
import ai.galixo.proxy.domain.repository.PingRepository
import ai.galixo.proxy.domain.repository.ServersDBRepo
import ai.galixo.proxy.domain.repository.ServersRepository
import android.app.Application
import android.content.Context
import ai.galixo.proxy.data.remote.ApiService
import ai.galixo.proxy.data.repository.CypherRepositoryImpl
import ai.galixo.proxy.data.repository.PingRepositoryImpl
import ai.galixo.proxy.data.repository.ServersRepositoryImp
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
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

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

    @LocationApiRetrofit
    @Provides
    @Singleton
    fun provideLocationRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.bigdatacloud.net/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
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