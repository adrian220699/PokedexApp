package com.example.pokedexapp.data.remote.dto


import com.example.pokedexapp.data.remote.dto.PokemonListDto

import retrofit2.http.GET
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): PokemonListDto
}