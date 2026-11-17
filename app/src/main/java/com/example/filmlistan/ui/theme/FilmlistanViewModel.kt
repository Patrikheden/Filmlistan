package com.example.filmlistan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class FilmlistanUiState(
    val nyTitel: String = "",
    val filmer: List<Film> = listOf(
        Film(
            id = 1,
            titel = "Avengers: Endgame",
            ar = 2019,
            genre = "Action",
            bild = R.drawable.endgame
        ),
        Film(
            id = 2,
            titel = "Iron Man",
            ar = 2008,
            genre = "Action",
            bild = R.drawable.ironman
        ),
        Film(
            id = 3,
            titel = "Guardians of the Galaxy",
            ar = 2014,
            genre = "Sci-fi",
            bild = R.drawable.guardian
        ),
        Film(
            id = 4,
            titel = "Spider-Man: No Way Home",
            ar = 2021,
            genre = "Action",
            bild = R.drawable.spidermannowayhome
        ),
        Film(
            id = 5,
            titel = "Thor: Ragnarok",
            ar = 2017,
            genre = "Action",
            bild = R.drawable.thorragnarrok
        ),
        Film(
            id = 6,
            titel = "Deadpool & Wolverine",
            ar = 2024,
            genre = "Action",
            bild = R.drawable.deadpool_wolverine
        ),
        Film(
            id = 7,
            titel = "Black Panther",
            ar = 2018,
            genre = "Action",
            bild = R.drawable.blackpanther
        ),
        Film(
            id = 8,
            titel = "Captain America: The Winter Soldier",
            ar = 2014,
            genre = "Action",
            bild = R.drawable.captainamericathewintersoldier


        )
    )
)

class FilmlistanViewModel : ViewModel() {
    var uiState by mutableStateOf(FilmlistanUiState())
        private set

    private var nextId = 10
    fun onNewTitleChange(text: String) {
        uiState = uiState.copy(nyTitel = text)
    }
    fun addFilm() {
        val titel = uiState.nyTitel.trim()
        if (titel.isEmpty()) return
        val film = Film(
            id = nextId,
            titel = titel,
            ar = 0,
            genre = "Okänd",
        )

        nextId = nextId + 1
        uiState = uiState.copy(
            nyTitel = "",
            filmer = uiState.filmer + film,
        )
    }
    fun toggleSeen(id: Int) {
        uiState = uiState.copy(
            filmer = uiState.filmer.map {
                if (it.id == id) it.copy(sedd = !it.sedd) else it
            }
        )
    }
    fun removeFilm(id: Int) {
        uiState = uiState.copy(
            filmer = uiState.filmer.filter { it.id != id }
        )
    }
}
