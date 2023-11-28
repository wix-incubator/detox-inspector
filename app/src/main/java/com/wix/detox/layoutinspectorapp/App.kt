package com.wix.detox.layoutinspectorapp

import android.app.Application
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.i("App onCreate")
    }
}