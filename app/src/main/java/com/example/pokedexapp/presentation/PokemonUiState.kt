package com.example.pokedexapp.presentation

import android.os.Message
import  com.example.pokedexapp.domain.model.Pokemon

sealed interface PokemonUiState {
    data object Loading : PokemonUiState
    data class Success(val pokemons: List<Pokemon>) : PokemonUiState
    data class Error(val message: String) : PokemonUiState
}