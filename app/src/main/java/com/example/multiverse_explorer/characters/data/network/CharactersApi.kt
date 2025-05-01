package com.example.multiverse_explorer.characters.data.network

import com.example.multiverse_explorer.characters.data.model.ApiData
import retrofit2.Response
import retrofit2.http.GET

interface CharactersApi {

    @GET("character")
    suspend fun getCharacters(): Response<ApiData>

}