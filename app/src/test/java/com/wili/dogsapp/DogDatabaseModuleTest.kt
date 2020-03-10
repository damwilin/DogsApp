package com.wili.dogsapp

import android.app.Application
import com.wili.dogsapp.di.DogDatabaseModule
import com.wili.dogsapp.model.DogDao
import com.wili.dogsapp.model.DogDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


class DogDatabaseModuleTest(val dogDao: DogDao): DogDatabaseModule() {
    override fun provideDogDao(app: Application): DogDao {
        return dogDao
    }
}