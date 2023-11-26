package com.wix.detox.inpsector.repository

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.wix.detox.inpsector.di.DI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class ActiveViewRepositoryImpl : ActiveViewRepository {

    private val scope = CoroutineScope(Dispatchers.Main)

    private val activeActivityRepository: ActiveActivityRepository by lazy {
        DI.activeActivityRepository
    }

    override val activeViewRoot: StateFlow<ViewNode?>
        get() = activeActivityRepository.activeActivity.map {
            it.toViewNode()
        }.stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )


    private fun Activity?.toViewNode(): ViewNode? {
        Timber.d("About to extract view hierarchy from $this")

        if (this == null) {
            return null
        }

        val rootView = this.window.decorView.rootView as? ViewGroup

        if (rootView == null) {
            Timber.w("Root view is null")
            return null
        }

        val contentView = rootView.findViewById<ViewGroup>(android.R.id.content)
        val appContentView = contentView.getChildAt(0) as? ViewGroup

        return appContentView?.toViewNode(null)
    }


    private fun getAbsolutePosition(view: View): IntArray {
        val locationInWindow = IntArray(2)
        view.getLocationInWindow(locationInWindow)
        val x = locationInWindow[0]
        val y = locationInWindow[1]
        var statusBarHeight = 0
        val resourceId =
            view.context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = view.context.resources.getDimensionPixelSize(resourceId)
        }
        val absoluteY = y - statusBarHeight
        return intArrayOf(x, absoluteY)
    }


    private fun View.toViewNode(parent: ViewNode?, children: List<ViewNode>): ViewNode? {
        if (visibility != View.VISIBLE) {
            return null
        }

        val density = this.context.resources.displayMetrics.density
        val location = getAbsolutePosition(this)

        val id = if (this.id != View.NO_ID) {
            resources.getResourceEntryName(this.id);
        } else {
            ""
        }

        return ViewNode(
            id = id,
            tag = tagToString(this.tag),
            className = this::class.java.simpleName,
            width = this.width / density,
            height = this.height / density,
            x = location[0] / density,
            y = location[1] / density,
            parent = parent,
            children = children
        )
    }

    private fun ViewGroup.toViewNode(parent: ViewNode?): ViewNode? {
        val viewNodesChildren = mutableListOf<ViewNode>()
        val viewChildren = children.toList().sortedByDescending { it.z }

        val result = (this as View).toViewNode(parent, children = viewNodesChildren)

        for (child in viewChildren) {
            if (child is ViewGroup) {
                val node = child.toViewNode(result)
                node?.let { viewNodesChildren.add(it) }

            } else if (child is View) {
                val node = child.toViewNode(result, emptyList())
                node?.let { viewNodesChildren.add(it) }
            }
        }

        return result
    }

    private fun tagToString(tag: Any?): String {
        return when (tag) {
            null -> ""
            is String -> tag
            else -> tag.toString()
        }
    }
}