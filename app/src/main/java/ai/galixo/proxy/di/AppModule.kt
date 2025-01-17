package ai.galixo.proxy.di

import ai.galixo.proxy.data.repository.AppsRepositoryImp
import ai.galixo.proxy.domain.repository.AppsRepository
import ai.galixo.proxy.domain.repository.FeedbackRepository
import ai.galixo.proxy.domain.repository.LanguageRepo
import android.content.Context
import ai.galixo.proxy.data.remote.ApiService
import ai.galixo.proxy.data.repository.FeedbackRepositoryImp
import ai.galixo.proxy.data.repository.LanguageRepoImpl
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
}