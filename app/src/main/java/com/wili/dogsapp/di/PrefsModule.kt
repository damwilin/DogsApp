package com.wili.dogsapp.di

import android.app.Activity
import android.app.Application
import androidx.core.app.ActivityCompat
import com.wili.dogsapp.util.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
open class PrefsModule {
    @Provides
    @Singleton
    @TypeOfContext(type = CONTEXT_APP)
    open fun provideSharedPreferences(app:Application):SharedPreferencesHelper{
        return SharedPreferencesHelper(app)
    }
}
const val CONTEXT_APP = "ApplicationContext"
const val CONTEXT_ACTIVITY = "ActivityContext"

@Qualifier
annotation class TypeOfContext(val type:String)