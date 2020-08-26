package com.wafflecopter.sampleapp

import android.app.Application
import com.wafflecopter.contactspicker.BuildConfig
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}