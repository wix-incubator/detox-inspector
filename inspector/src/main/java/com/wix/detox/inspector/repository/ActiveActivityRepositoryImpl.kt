package com.wix.detox.inspector.repository

import android.app.Activity
import android.os.Bundle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

/**
 * This class manages the last active activity in the application before the inspector was launched.
 */
class ActiveActivityRepositoryImpl : ActiveActivityRepository {

    private val _activeActivity: MutableStateFlow<Activity?> = MutableStateFlow(null)

    override val activeActivity: StateFlow<Activity?>
        get() = _activeActivity

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Timber.d("onActivityCreated: $activity")
    }

    override fun onActivityStarted(activity: Activity) {
        Timber.d("onActivityStarted: $activity")
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
        Timber.i("onActivityPaused: $activity")
        _activeActivity.value = activity
    }

    override fun onActivityStopped(activity: Activity) {
        Timber.d("onActivityStopped: $activity")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        Timber.d("onActivityDestroyed: $activity")
    }
}