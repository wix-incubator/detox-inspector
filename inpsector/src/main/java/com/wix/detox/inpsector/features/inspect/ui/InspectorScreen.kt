package com.wix.detox.inpsector.features.inspect.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wix.detox.inpsector.features.inspect.viewmodel.InspectViewModel
import com.wix.detox.inpsector.repository.ViewNode
import com.wix.detox.inpsector.ui.theme.DetoxInspectorTheme
import timber.log.Timber

private interface Callback {
    fun onNodeClicked(node: ViewNode)
}

@Composable
fun InspectScreen(viewModel: InspectViewModel) {

    val activeViewRootState = viewModel.activeViewRoot.collectAsState()
    val activeViewRoot = activeViewRootState.value

    if (activeViewRoot != null) {
        InspectScreen(activeViewRoot, object : Callback {
            override fun onNodeClicked(node: ViewNode) {
                Timber.i("Node clicked: ${node.className} ${node.id} ${node.tag}")
                //viewModel.onNodeClicked(node)
            }
        })
    } else {
        EmptyInspectScreen()
    }
}

@Composable
private fun EmptyInspectScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Text(text = "View not found")
    }
}

@Composable
private fun InspectScreen(activeViewRoot: ViewNode, callback: Callback) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ViewNodeRectangle(activeViewRoot, callback)
        }
    }
}

@Composable
private fun ViewNodeRectangle(activeViewRoot: ViewNode, callback: Callback) {
    Box(modifier = Modifier
        .size(activeViewRoot.width.dp, activeViewRoot.height.dp)
        .offset(x = activeViewRoot.x.dp, y = activeViewRoot.y.dp)
        .background(Color.Transparent)
        .border(2.dp, Color.Red)
        .clickable { callback.onNodeClicked(activeViewRoot) }) {}

    activeViewRoot.children.forEach {
        ViewNodeRectangle(it, callback)
    }
}

private fun createPreviewModel() = ViewNode(
    id = "root",
    tag = "root",
    width = 100f,
    height = 100f,
    x = 50.0f,
    y = 50.0f,
    className = "Root",
    parent = null,
    children = listOf(
        ViewNode(
            id = "child1",
            tag = "child1",
            width = 100f,
            height = 100f,
            x = 150.0f,
            y = 150.0f,
            className = "Root",
            parent = null,
            children = emptyList()
        ), ViewNode(
            id = "child2",
            tag = "child2",
            width = 50f,
            height = 50f,
            x = 120.0f,
            y = 120.0f,
            parent = null,
            className = "Root",
            children = emptyList()
        )
    )
)

private val dummyCallback = object : Callback {
    override fun onNodeClicked(node: ViewNode) {}
}

@Composable
@Preview(showBackground = true)
private fun EmptyInspectorScreenPreview() {
    EmptyInspectScreen()
}

@Composable
@Preview(showBackground = true)
private fun InspectorScreenPreview() {
    DetoxInspectorTheme {
        InspectScreen(createPreviewModel(), dummyCallback)
    }
}