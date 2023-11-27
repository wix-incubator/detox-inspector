package com.wix.detox.layoutinspectorapp

import android.app.Application
import com.wix.detox.inspector.utils.TimberLogTree
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.i("App onCreate")
    }
}