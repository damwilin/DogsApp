package com.wili.dogsapp.di

import com.wili.dogsapp.model.DogsApi
import com.wili.dogsapp.model.DogsApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class ApiModule {
    private val BASE_URL = "https://raw.githubusercontent.com"

    @Provides
    fun provideDogsApi():DogsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DogsApi::class.java)
    }

    @Provides
    open fun provideDogsService(): DogsApiService{
        return DogsApiService()
    }
}