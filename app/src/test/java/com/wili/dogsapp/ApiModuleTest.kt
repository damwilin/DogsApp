package com.wili.dogsapp

import com.wili.dogsapp.di.ApiModule
import com.wili.dogsapp.model.DogsApiService

class ApiModuleTest(val mockService: DogsApiService): ApiModule() {
    override fun provideDogsService(): DogsApiService {
        return  mockService
    }
}