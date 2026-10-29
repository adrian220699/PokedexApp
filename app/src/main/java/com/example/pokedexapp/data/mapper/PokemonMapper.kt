package com.example.pokedexapp.data.mapper

import com.example.pokedexapp.data.remote.dto.PokemonItemDto
import com.example.pokedexapp.domain.model.Pokemon

//Mapea el DTO puro del API -> a la Entity / Model de Dominio

fun PokemonItemDto.toDomain(): Pokemon {

    val id = url.trimEnd('/').split("/").last().toIntOrNull() ?: 0

    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

    return Pokemon (

        id = id,
        name = name.replaceFirstChar { it.uppercase() },
        imageUrl = imageUrl


    )

}


