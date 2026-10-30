package com.example.pokedexapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pokedexapp.data.remote.dto.RetrofitClient
import com.example.pokedexapp.data.repository.PokemonRepositoryImpl
import com.example.pokedexapp.domain.model.Pokemon
import com.example.pokedexapp.domain.usecase.GetPokemonsUseCase
import com.example.pokedexapp.presentation.PokemonUiState
import com.example.pokedexapp.presentation.PokemonViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instanciación manual respetando Clean Architecture
        val repository = PokemonRepositoryImpl(RetrofitClient.pokeApiService)
        val useCase = GetPokemonsUseCase(repository)
        val viewModel = PokemonViewModel(useCase)


        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state by viewModel.uiState.collectAsState()
                    PokedexScreen(state = state, onRetry = { viewModel.fetchPokemons() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScreen(
    state: PokemonUiState,
    onRetry: () -> Unit
) {
    var pokemonSeleccionado by remember {
        mutableStateOf<Pokemon?>(null)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pokédex", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is PokemonUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is PokemonUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRetry) {
                            Text("Reintentar")
                        }
                    }
                }
                is PokemonUiState.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.pokemons, key = { it.idPokemon }) { pokemon ->
                            PokemonCard(
                                pokemon = pokemon,
                                onClick = {
                                    pokemonSeleccionado = pokemon
                                }
                            )

                        }
                    }
                }
            }
        }
    }
    pokemonSeleccionado?.let { pokemon ->
        Dialog(
            onDismissRequest = {
                pokemonSeleccionado = null
            }
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "#%03d".format(pokemon.idPokemon),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    AsyncImage(
                        model = pokemon.imageUrlPokemon,
                        contentDescription = pokemon.namePokemon,
                        modifier = Modifier.size(240.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = pokemon.namePokemon.uppercase(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            pokemonSeleccionado = null
                        }
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "#%03d".format(pokemon.idPokemon),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )

            AsyncImage(
                model = pokemon.imageUrlPokemon,
                contentDescription = pokemon.namePokemon,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = pokemon.namePokemon,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}