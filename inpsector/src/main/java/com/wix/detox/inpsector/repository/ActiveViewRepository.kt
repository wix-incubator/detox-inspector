package com.wix.detox.inpsector.repository

import kotlinx.coroutines.flow.StateFlow

data class ViewNode(
    val id: String,
    val tag: String,
    val label: String,
    val width: Float,
    val height: Float,
    val x: Float,
    val y: Float,
    val className: String,
    val parent: ViewNode?,
    val children: List<ViewNode>
) {
    override fun equals(other: Any?): Boolean {
        if (other !is ViewNode) {
            return false
        }
        // All fields are immutable, so we can compare them directly except for the parent
        // We don't compare the parent because it would cause a stack overflow
        return id == other.id &&
                tag == other.tag &&
                label == other.label &&
                width == other.width &&
                height == other.height &&
                x == other.x &&
                y == other.y &&
                className == other.className &&
                children == other.children
    }

    override fun toString(): String {
        return "ViewNode(id='$id', tag='$tag', label='$label', width=$width, height=$height, x=$x, y=$y, className='$className', children=$children)"
    }
}

interface ActiveViewRepository {

    val activeViewRoot: StateFlow<ViewNode?>
}