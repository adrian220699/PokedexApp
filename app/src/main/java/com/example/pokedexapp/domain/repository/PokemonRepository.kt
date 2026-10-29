package com.example.pokedexapp.domain.repository

import  com.example.pokedexapp.domain.model.Pokemon

interface PokemonRepository{

    suspend fun getPokemons(limit: Int = 100, offset: Int = 0) : Result<List<Pokemon>>



}