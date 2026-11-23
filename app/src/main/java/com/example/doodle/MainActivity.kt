package com.example.doodle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.doodle.ui.theme.DoodleTheme
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke

val DarkGreen = Color(0xFF103C34)
val lightGreen = Color(0xFFA0B7B3)
var brushSize = 10f

data class StrokePath(
    val path: Path,
    val width: Float
    )
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoodleTheme {
                val paths = remember { mutableStateListOf<StrokePath>() }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        HeaderBar()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .background(lightGreen),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            DropMenu()
                            ResetButton(onClick = { paths.clear() })
                        }
                        DrawCanvas(paths)
                    }
                }
            }
        }
    }
}
//Header
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderBar() {
    CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkGreen,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Welcome to Doodle Me This!",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )
                }
    )
}
//Dropdown button to select brush size
@Composable
fun DropdownButton(onClick: () -> Unit) {
    Column (
        modifier = Modifier.padding(3.dp, 5.dp)

    ) {
        FilledTonalButton(
            onClick = { onClick() },
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = DarkGreen
            )
        )
        {
            Text("Choose Brush Size",
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
//Dropdown feature for brush size button
@Composable
fun DropMenu() {
    Column (horizontalAlignment =
        Alignment.CenterHorizontally)
    {
    var expanded by remember {mutableStateOf(false)}
    DropdownButton(onClick = {expanded =  !expanded})
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("Small") },
            onClick = { expanded = false; brushSize = 10f }
        )
        DropdownMenuItem(
            text = { Text("Medium") },
            onClick = { expanded = false; brushSize = 40f }
        )
        DropdownMenuItem(
            text = { Text("Large") },
            onClick = { expanded = false; brushSize = 70f }
        )
}
        var size: String
        if (brushSize == 10f) {
            size = "Small"
        }
        else if (brushSize == 40f) {
            size = "Medium"
        }
        else {
            size = "Large"
        }
    Text("Brush Size: $size",
        fontSize = 20.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.ExtraBold
        )
}
}
//Button to reset the canvas
@Composable
fun ResetButton(onClick: () -> Unit) {
    FilledTonalButton(onClick = {onClick() },
        modifier = Modifier.padding(3.dp, 5.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = DarkGreen
        )
    ) {
        Text("Reset",
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = Color.White)
    }
}

//Setup canvas to listen for drag events
@Composable
fun DrawCanvas(pathsRef: MutableList<StrokePath>) {
    var currentPath by remember {mutableStateOf(StrokePath(Path(), 10f))}
    Column {
    Canvas(modifier =
        Modifier
            .fillMaxWidth()
            .height(80.dp)
            .weight(1f)
            .background(Color.White)
            .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset: Offset ->
                    val newPath = Path()
                    newPath.moveTo(offset.x, offset.y)
                    val newStrokePath = StrokePath(newPath, brushSize)
                    currentPath = newStrokePath
                },
                onDrag = {change, dragAmount ->
                    val updatedPath = Path()
                    updatedPath.addPath(currentPath.path)

                    updatedPath.lineTo(
                        change.position.x,
                        change.position.y
                    )
                    currentPath = StrokePath(updatedPath,currentPath.width)
                },
                onDragEnd = {
                    pathsRef.add(currentPath)
                    currentPath = StrokePath(Path(), 10f)
                }
            )
            }
    ) {
        pathsRef.forEach { ref: StrokePath ->
            drawPath(
                ref.path,
                Color.Black,
                style = Stroke(width = ref.width)
            )
        }
        drawPath(currentPath.path, Color.Black, style = Stroke(width = currentPath.width))
    }
    }
}