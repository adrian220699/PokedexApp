package com.example.pokedexapp.domain.usecase

import androidx.compose.ui.geometry.Offset
import  com.example.pokedexapp.domain.model.Pokemon
import  com.example.pokedexapp.domain.repository.PokemonRepository


class GetPokemonsUseCase (
    private  val repository : PokemonRepository
) {

    suspend operator fun invoke(limit: Int = 100, offset: Int = 0) : Result<List<Pokemon>> {

        return  repository.getPokemons(limit, offset)
    }


}