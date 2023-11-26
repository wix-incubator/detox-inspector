package com.wix.detox.inpsector

import android.app.Application
import android.content.Context
import com.wix.detox.inpsector.features.inspect.InspectActivity
import com.wix.detox.inpsector.di.DI
import com.wix.detox.inpsector.utils.TimberLogTree
import timber.log.Timber

object Inspector {
    private lateinit var application: Application

    fun init(context: Context) {
        application = context.applicationContext as Application
        initLogs()
        Timber.i("Inspector init...")
        registerActivityLifecycleCallbacks()
    }

    fun inspect(context: Context) {
        InspectActivity.startActivity(context = context)
    }

    private fun initLogs() {
        Timber.plant(TimberLogTree())
    }

    private fun registerActivityLifecycleCallbacks() {
        Timber.i("about to register activity lifecycle callbacks")
        application.registerActivityLifecycleCallbacks(DI.activeActivityRepository)
    }
}
