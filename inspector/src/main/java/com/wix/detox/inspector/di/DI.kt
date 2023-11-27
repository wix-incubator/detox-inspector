package com.wix.detox.inspector.di

import com.wix.detox.inspector.repository.ActiveActivityRepository
import com.wix.detox.inspector.repository.ActiveActivityRepositoryImpl
import com.wix.detox.inspector.repository.ActiveViewRepository
import com.wix.detox.inspector.repository.ActiveViewRepositoryImpl

object DI {

    val activeActivityRepository: ActiveActivityRepository by lazy {
        ActiveActivityRepositoryImpl()
    }

    val activeViewRepository: ActiveViewRepository by lazy {
        ActiveViewRepositoryImpl()
    }
}