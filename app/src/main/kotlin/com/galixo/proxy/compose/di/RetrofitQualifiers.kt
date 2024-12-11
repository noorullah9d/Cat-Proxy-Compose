package com.galixo.proxy.compose.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseApiRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SearchApiRetrofit
