package ai.galixo.proxy.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseApiRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SearchApiRetrofit
