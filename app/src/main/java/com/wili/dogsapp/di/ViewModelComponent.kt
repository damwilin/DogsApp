package com.wili.dogsapp.di

import com.wili.dogsapp.viewmodel.ListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, PrefsModule::class, AppModule::class, DogDatabaseModule::class])
interface ViewModelComponent {
    fun inject(viewModel:ListViewModel)
}