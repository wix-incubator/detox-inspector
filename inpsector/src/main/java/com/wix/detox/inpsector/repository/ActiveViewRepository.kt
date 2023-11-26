package com.wix.detox.inpsector.repository

import kotlinx.coroutines.flow.StateFlow

data class ViewNode(
    val id: String,
    val tag: String,
    val width: Float,
    val height: Float,
    val x: Float,
    val y: Float,
    val className: String,
    val parent: ViewNode?,
    val children: List<ViewNode>
)

interface ActiveViewRepository {

    val activeViewRoot: StateFlow<ViewNode?>
}