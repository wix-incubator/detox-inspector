package com.wix.detox.inpsector.di

import com.wix.detox.inpsector.repository.ActiveActivityRepository
import com.wix.detox.inpsector.repository.ActiveActivityRepositoryImpl
import com.wix.detox.inpsector.repository.ActiveViewRepository
import com.wix.detox.inpsector.repository.ActiveViewRepositoryImpl

object DI {

    val activeActivityRepository: ActiveActivityRepository by lazy {
        ActiveActivityRepositoryImpl()
    }

    val activeViewRepository: ActiveViewRepository by lazy {
        ActiveViewRepositoryImpl()
    }
}