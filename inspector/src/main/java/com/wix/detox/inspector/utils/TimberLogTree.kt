package com.wix.detox.inspector.utils

import timber.log.Timber

class TimberLogTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String {
        val tag = super.createStackElementTag(element)
        return "IN-${tag}"
    }
}