package com.wili.dogsapp.di

import android.app.Application
import android.content.Context
import com.wili.dogsapp.model.DogDao
import com.wili.dogsapp.model.DogDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class DogDatabaseModule {
    @Provides
    @Singleton
    open fun provideDogDao(app:Application): DogDao{
       return DogDatabase.invoke(app).getDao()
    }
}