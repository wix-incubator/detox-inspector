package com.wix.detox.inpsector.features.inspect.viewmodel

import androidx.lifecycle.ViewModel
import com.wix.detox.inpsector.di.DI
import com.wix.detox.inpsector.repository.ActiveViewRepository
import com.wix.detox.inpsector.repository.ViewNode
import kotlinx.coroutines.flow.StateFlow

class InspectViewModel : ViewModel() {

    private val activeViewRepository: ActiveViewRepository by lazy {
        DI.activeViewRepository
    }

    val activeViewRoot: StateFlow<ViewNode?> = activeViewRepository.activeViewRoot
}