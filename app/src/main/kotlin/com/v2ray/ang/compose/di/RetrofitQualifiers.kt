package com.v2ray.ang.compose.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseApiRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SearchApiRetrofit
