package com.example.pokedexapp.data.remote.dto

import  kotlinx.serialization.SerialName
import  kotlinx.serialization.Serializable

@Serializable
data class PokemonItemDto (

    @SerialName("name") val name : String,
    @SerialName("url") val  url : String

)