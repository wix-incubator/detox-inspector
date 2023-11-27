package com.wix.detox.inspector.features.inspect.viewmodel

import androidx.lifecycle.ViewModel
import com.wix.detox.inspector.di.DI
import com.wix.detox.inspector.repository.ActiveViewRepository
import com.wix.detox.inspector.repository.ViewNode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InspectViewModel : ViewModel() {

    private val activeViewRepository: ActiveViewRepository by lazy {
        DI.activeViewRepository
    }

    val activeViewRoot: StateFlow<ViewNode?> = activeViewRepository.activeViewRoot

    private val _selectedNode: MutableStateFlow<ViewNode?> = MutableStateFlow(null)
    val selectedNode: StateFlow<ViewNode?>
        get() = _selectedNode

    fun onNodeClicked(node: ViewNode) {
        _selectedNode.value = node
    }

    fun onModalDismissed() {
        _selectedNode.value = null
    }
}