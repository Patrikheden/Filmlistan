package com.example.filmlistan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.filmlistan.ui.theme.FilmlistanTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmlistanTheme {
                FilmlistanScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FilmlistanTheme {
        Greeting("Android")
    }
}

data class Film(
    val id: Int,
    val titel: String,
    val ar: Int,
    val genre: String,
    val sedd: Boolean = false,
    val bild: Int? = null,
)

@Composable
fun BildPlats(
    titel: String,
    bild: Int? = null,
    modifier: Modifier = Modifier,
) {
    val form = modifier
        .size(width = 80.dp, height = 120.dp)
        .clip(RoundedCornerShape(8.dp))

    if (bild != null) {
        Image(
            painter = painterResource(bild),
            contentDescription = titel,
            contentScale = ContentScale.Crop,
            modifier = form,
        )
    } else {
        Box(
            modifier = form.background(
                MaterialTheme.colorScheme.primaryContainer
            ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = titel.firstOrNull()?.uppercase() ?: "?",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
fun FilmKort(
    film: Film,
    onToggle: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color(0xFF242424)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BildPlats(titel = film.titel, bild = film.bild)
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = film.titel,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = "${film.ar} · ${film.genre}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
                TextButton(
                    onClick = onRemove,
                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text("Ta bort")
                }
            }
            Checkbox(
                checked = film.sedd,
                onCheckedChange = { onToggle() },
                colors = androidx.compose.material3.CheckboxDefaults.colors(
                    checkedColor = Color(0xFFE50914),
                    uncheckedColor = Color.LightGray,
                    checkmarkColor = Color.White
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmlistanScreen(viewModel: FilmlistanViewModel = viewModel()) {
    val state = viewModel.uiState
    val attSe = state.filmer.filter { !it.sedd }
    val sedda = state.filmer.filter { it.sedd }
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "NETFLIX",
                        color = Color(0xFFE50914),
                        fontWeight = FontWeight.Black,
                        fontSize = 34.sp,
                        letterSpacing = 7.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = state.nyTitel,
                onValueChange = { viewModel.onNewTitleChange(it) },
                label = { Text("Titel") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
                        Button(
                        onClick = { viewModel.addFilm() },
                            enabled = state.nyTitel.isNotBlank(),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE50914),
                                contentColor = Color.White
                            ),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                Text("Lägg till")
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Text(
                        text = "Vill se",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(attSe, key = { it.id }) { film ->
                    FilmKort(
                        film = film,
                        onToggle = { viewModel.toggleSeen(film.id) },
                        onRemove = { viewModel.removeFilm(film.id) },
                    )
                }
                item {
                    Text(
                        "Sett",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(sedda, key = { it.id }) { film ->
                    FilmKort(
                        film = film,
                        onToggle = { viewModel.toggleSeen(film.id) },
                        onRemove = { viewModel.removeFilm(film.id) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilmlistanScreenPreview() {
    FilmlistanTheme {
        FilmlistanScreen()
    }
}