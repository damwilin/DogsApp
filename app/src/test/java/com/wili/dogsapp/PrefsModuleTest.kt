package com.wili.dogsapp

import android.app.Application
import com.wili.dogsapp.di.PrefsModule
import com.wili.dogsapp.util.SharedPreferencesHelper

class PrefsModuleTest(val mockPrefs: SharedPreferencesHelper): PrefsModule() {
    override fun provideSharedPreferences(app: Application): SharedPreferencesHelper {
        return mockPrefs
    }
}