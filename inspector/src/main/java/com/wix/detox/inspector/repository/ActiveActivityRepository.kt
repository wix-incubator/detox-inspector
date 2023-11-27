package com.wix.detox.inspector.repository

import android.app.Activity
import android.app.Application
import kotlinx.coroutines.flow.StateFlow

interface ActiveActivityRepository : Application.ActivityLifecycleCallbacks {

    val activeActivity: StateFlow<Activity?>
}