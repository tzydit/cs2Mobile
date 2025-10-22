package br.com.uri.cs2mobile.network

import br.com.uri.cs2mobile.data.Skin
import br.com.uri.cs2mobile.data.Sticker
import retrofit2.http.GET

interface CsGoApiService {


    @GET("skins.json")
    suspend fun getSkins(): List<Skin>

    @GET("stickers.json")
    suspend fun getStickers(): List<Sticker>
}