package com.wix.detox.inspector.features.inspect.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wix.detox.inspector.features.inspect.viewmodel.InspectViewModel
import com.wix.detox.inspector.repository.ViewNode
import com.wix.detox.inspector.ui.theme.DetoxInspectorTheme
import timber.log.Timber

private interface Callback {
    fun onNodeClicked(node: ViewNode)
}

@Composable
fun InspectScreen(viewModel: InspectViewModel) {

    val activeViewRootState = viewModel.activeViewRoot.collectAsState()
    val selectedNodeState by viewModel.selectedNode.collectAsState()
    val activeViewRoot = activeViewRootState.value

    if (activeViewRoot != null) {
        InspectScreen(activeViewRoot, selectedNodeState, object : Callback {
            override fun onNodeClicked(node: ViewNode) {
                Timber.i("Node clicked: ${node.className} ${node.id} ${node.tag}")
                viewModel.onNodeClicked(node)
            }
        })

        selectedNodeState?.let {
            Dialog(
                selectedNode = it,
                onDismiss = {
                    viewModel.onModalDismissed()
                },
                onParentClick = {
                    it.parent?.let { parent -> viewModel.onNodeClicked(parent) }
                },
            )
        }
    } else {
        EmptyInspectScreen()
    }
}

@Composable
private fun Dialog(selectedNode: ViewNode, onDismiss: () -> Unit, onParentClick: () -> Unit = {}) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                    RoundedCornerShape(8.dp)
                ), shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    modifier = Modifier.align(CenterHorizontally),
                    text = selectedNode.className,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Column(modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)) {
                    Row {
                        Text(
                            modifier = Modifier.alignByBaseline(),
                            text = "TestID: ",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            modifier = Modifier.alignByBaseline(),
                            text = selectedNode.tag,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Row {
                        Text(
                            modifier = Modifier.alignByBaseline(),
                            text = "Label: ",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            modifier = Modifier.alignByBaseline(),
                            text = selectedNode.label,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Row {
                        Text(
                            modifier = Modifier.alignByBaseline(),
                            text = "Android ID: ",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            modifier = Modifier.alignByBaseline(),
                            text = selectedNode.id,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Row(Modifier.fillMaxWidth(), Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Close", style = MaterialTheme.typography.labelLarge)
                    }

                    TextButton(onClick = onParentClick, enabled = selectedNode.parent != null) {
                        Text(text = "View Parent", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyInspectScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent)

    ) {
    }
}

@Composable
private fun InspectScreen(activeViewRoot: ViewNode, selectedNode: ViewNode?, callback: Callback) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ViewNodeRectangle(activeViewRoot, selectedNode, callback)
        }
    }
}

@Composable
private fun ViewNodeRectangle(
    activeViewRoot: ViewNode, selectedNode: ViewNode?, callback: Callback
) {
    val bgColor = if (activeViewRoot == selectedNode) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    } else {
        Color.Transparent
    }

    val modifier = Modifier
        .size(activeViewRoot.width.dp, activeViewRoot.height.dp)
        .offset(x = activeViewRoot.x.dp, y = activeViewRoot.y.dp)
        .background(bgColor)
        .border(2.dp, MaterialTheme.colorScheme.primary)
        .clickable {
            callback.onNodeClicked(activeViewRoot)
        }
    Box(modifier = modifier) {}

    activeViewRoot.children.forEach {
        ViewNodeRectangle(it, selectedNode, callback)
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
    label = "Root",
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
            label = "Root",
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
            label = "Root",
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
        InspectScreen(createPreviewModel(), createPreviewModel(), dummyCallback)
    }
}

@Composable
@Preview(showBackground = true)
private fun DialogPreview() {
    DetoxInspectorTheme {
        Dialog(selectedNode = createPreviewModel(), onDismiss = {})
    }
}