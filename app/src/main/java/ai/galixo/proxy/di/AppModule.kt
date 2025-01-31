package ai.galixo.proxy.di

import ai.galixo.proxy.data.repository.AppsRepositoryImp
import ai.galixo.proxy.domain.repository.AppsRepository
import ai.galixo.proxy.domain.repository.FeedbackRepository
import ai.galixo.proxy.domain.repository.LanguageRepo
import android.content.Context
import ai.galixo.proxy.data.remote.ApiService
import ai.galixo.proxy.data.repository.FeedbackRepositoryImp
import ai.galixo.proxy.data.repository.LanguageRepoImpl
import ai.galixo.proxy.data.repository.RemoteConfigRepositoryImpl
import ai.galixo.proxy.domain.repository.RemoteConfigRepository
import ai.galixo.proxy.presentation.iap.BillingViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
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

    @Provides
    @Singleton
    fun provideFeedbackRepo(
        @ApplicationContext context: Context,
        apiService: ApiService
    ): FeedbackRepository {
        return FeedbackRepositoryImp(context, apiService)
    }

    @Provides
    fun provideLanguageRepo(): LanguageRepo = LanguageRepoImpl()

    @Provides
    @Singleton
    fun provideBillingViewModel(
        context: Context
    ): BillingViewModel {
        return BillingViewModel(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance()
    }

    @Singleton
    @Provides
    fun provideRemoteConfigRepository(
        @ApplicationContext context: Context,
        firebaseRemoteConfig: FirebaseRemoteConfig
    ): RemoteConfigRepository {
        return RemoteConfigRepositoryImpl(context, firebaseRemoteConfig)
    }
}