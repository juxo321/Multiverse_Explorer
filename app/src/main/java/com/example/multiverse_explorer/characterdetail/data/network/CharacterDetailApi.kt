package com.example.multiverse_explorer.characterdetail.data.network

import com.example.multiverse_explorer.characterdetail.data.model.CharacterDetailData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterDetailApi {

    @GET("character/{id}")
    suspend fun getCharacterDetail(
        @Path("id") characterId: Int
    ): Response<CharacterDetailData>

}