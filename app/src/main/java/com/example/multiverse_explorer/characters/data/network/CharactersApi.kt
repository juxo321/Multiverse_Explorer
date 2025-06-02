package com.example.multiverse_explorer.characters.data.network

import com.example.multiverse_explorer.characters.data.network.model.ApiData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersApi {

    @GET("character")
    suspend fun getCharacters(@Query("status") selectedStatus: String): Response<ApiData>

}