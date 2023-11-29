package com.wix.detox.inspector.repository

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.wix.detox.inspector.di.DI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

data class BlackListItem(val id: String? = null, val className: String)

class ActiveViewRepositoryImpl : ActiveViewRepository {

    private val blackList = listOf(BlackListItem(className = "TextView"))

    private val scope = CoroutineScope(Dispatchers.Main)

    private val activeActivityRepository: ActiveActivityRepository by lazy {
        DI.activeActivityRepository
    }

    override val activeViewRoot: StateFlow<ViewNode?>
        get() = activeActivityRepository.activeActivity.map {
            it.toViewNode()
        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
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

        return rootView.toViewNode(null)
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

    private fun BlackListItem.matchesView(view: View): Boolean {
        val className = view::class.java.simpleName

        id?.let {
            if (it == view.getResourceName()) {
                return false
            }
        }

        if (this.className != className) {
            return false
        }

        return true
    }

    private fun isInBlackList(view: View): Boolean {
        return blackList.any { it.matchesView(view) }
    }

    private fun View.toViewNode(parent: ViewNode?, children: List<ViewNode>): ViewNode? {
        if (visibility != View.VISIBLE) {
            return null
        }

        if (isInBlackList(this)) {
            return null
        }

        val density = this.context.resources.displayMetrics.density
        val location = getAbsolutePosition(this)
        val id = getResourceName()

        return ViewNode(
            id = id,
            tag = tagToString(this.tag),
            label = this.contentDescription?.let { tagToString(it) } ?: NA,
            className = this::class.java.simpleName,
            width = this.width / density,
            height = this.height / density,
            x = location[0] / density,
            y = location[1] / density,
            parent = parent,
            children = children
        )
    }

    private fun View.getResourceName(): String {
        val id = if (this.id != View.NO_ID) {
            try {
                resources.getResourceEntryName(this.id)
            } catch (e: Exception) {
                Timber.w(e, "Failed to get resource name for id: ${this.id}")
                NA
            }
        } else {
            NA
        }
        return id
    }

    private fun ViewGroup.toViewNode(parent: ViewNode?): ViewNode? {
        val viewNodesChildren = mutableListOf<ViewNode>()
        val viewChildren = children.toList().sortedByDescending { it.z }

        val result = (this as View).toViewNode(parent, children = viewNodesChildren)

        for (child in viewChildren) {
            if (child is ViewGroup) {
                val node = child.toViewNode(result)
                node?.let { viewNodesChildren.add(it) }
            } else {
                val node = child.toViewNode(result, emptyList())
                node?.let { viewNodesChildren.add(it) }
            }
        }

        return result
    }

    private fun tagToString(tag: Any?): String {
        return when (tag) {
            null -> NA
            is String -> tag
            else -> tag.toString()
        }
    }
}

private const val NA = "N/A"