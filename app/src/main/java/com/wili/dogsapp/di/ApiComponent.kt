package com.wili.dogsapp.di

import com.wili.dogsapp.model.DogsApiService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: DogsApiService)
}