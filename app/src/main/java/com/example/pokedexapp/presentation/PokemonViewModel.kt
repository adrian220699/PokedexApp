package com.example.pokedexapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexapp.domain.usecase.GetPokemonsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val getPokemonsUseCase: GetPokemonsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonUiState>(PokemonUiState.Loading)
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    init {
        fetchPokemons()
    }

    fun fetchPokemons() {
        viewModelScope.launch {
            _uiState.value = PokemonUiState.Loading
            getPokemonsUseCase()
                .onSuccess { list ->
                    _uiState.value = PokemonUiState.Success(list)
                }
                .onFailure { throwable ->
                    _uiState.value = PokemonUiState.Error(
                        throwable.localizedMessage ?: "Ocurrió un error inesperado al conectar con PokeAPI"
                    )
                }
        }
    }
}