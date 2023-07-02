package com.diamount_studio.image_ninja

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class Tool {
//    Undo, Redo, Crop, Rotate, Filter, Adjustments
    Undo, Redo, Crop, Filter
}

@Composable
fun PhotoEditorScreen() {
    var selectedTool by remember { mutableStateOf(Tool.Undo) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {
            // Image preview
        }

        Toolbar(selectedTool) { tool ->
            selectedTool = tool
        }
    }
}

@Composable
fun Toolbar(selectedTool: Tool, onToolSelected: (Tool) -> Unit) {
    val tools = Tool.values()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tools.forEach { tool ->
            IconButton(
                onClick = { onToolSelected(tool) },
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp),
                content = {
                    Icon(
                        imageVector = getToolIcon(tool),
                        contentDescription = null,
                        tint = if (selectedTool == tool) Color.Blue else Color.Black
                    )
                }
            )
        }
    }
}

@Composable
fun getToolIcon(tool: Tool): ImageVector
    = when (tool) {
        Tool.Crop -> Icons.Default.Crop
//        Tool.Eraser -> Icons.Default.Eraser
        Tool.Filter -> Icons.Default.FilterBAndW
        // Add more cases for other tools
        Tool.Undo -> Icons.Default.Undo
        Tool.Redo -> Icons.Default.Redo
//        Tool.Rotate -> Icons.Default.RotateRight
//        Tool.Adjustments -> Icons.Default.Adjust
    }

@Composable
fun PhotoEditorApp() {
    MaterialTheme {
        PhotoEditorScreen()
    }
}
