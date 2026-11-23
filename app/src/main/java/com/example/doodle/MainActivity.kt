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
import androidx.compose.ui.tooling.preview.Preview
import com.example.doodle.ui.theme.DoodleTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
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

val DarkGreen = Color(0xFF103C34)
val lightGreen = Color(0xFFA0B7B3)
var brushSize = "Small"

var isClear = true

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoodleTheme {
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
                            ResetButton(onClick = { isClear = true })
                        }
                        Canvas()
                    }
                }
            }
        }
    }
}

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
//Dropdown button
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
            Text("Choose BrushSize",
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
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
            onClick = { expanded = false; brushSize = "Small"}
        )
        DropdownMenuItem(
            text = { Text("Medium") },
            onClick = { expanded = false; brushSize = "Medium" }
        )
        DropdownMenuItem(
            text = { Text("Large") },
            onClick = { expanded = false; brushSize = "Large" }
        )
}
    Text("Brush Size: $brushSize",
        fontSize = 20.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.ExtraBold
        )
}
}

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
fun Canvas() {
    Column {
    Canvas(modifier =
        Modifier
            .fillMaxWidth()
            .height(80.dp)
            .weight(1f)
            .background(Color.White)
    ) { }
    }
}