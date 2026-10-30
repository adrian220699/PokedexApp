package com.example.pokedexapp.data.repository

import com.example.pokedexapp.data.mapper.toDomain
import com.example.pokedexapp.data.remote.dto.PokeApiService
import com.example.pokedexapp.domain.model.Pokemon
import com.example.pokedexapp.domain.repository.PokemonRepository

class PokemonRepositoryImpl(
    private val apiService: PokeApiService
) : PokemonRepository {

    override suspend fun getPokemons(limit: Int, offset: Int): Result<List<Pokemon>> {
        return try {
            val response = apiService.getPokemonList(limit, offset)
            val pokemonList = response.results.map { it.toDomain() }
            Result.success(pokemonList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}