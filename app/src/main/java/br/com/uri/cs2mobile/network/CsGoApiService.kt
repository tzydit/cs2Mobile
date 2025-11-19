package br.com.uri.cs2mobile.network

import br.com.uri.cs2mobile.data.*
import retrofit2.http.GET
import retrofit2.http.Path

interface CsGoApiService {
    @GET("public/api/{lang}/skins.json")
    suspend fun getSkins(@Path("lang") lang: String): List<Skin>

    @GET("public/api/{lang}/stickers.json")
    suspend fun getStickers(@Path("lang") lang: String): List<Sticker>

    @GET("public/api/{lang}/crates.json")
    suspend fun getCrates(@Path("lang") lang: String): List<Crate>

    @GET("public/api/{lang}/highlights.json")
    suspend fun getHighlights(@Path("lang") lang: String): List<Highlight>

    @GET("public/api/{lang}/agents.json")
    suspend fun getAgents(@Path("lang") lang: String): List<Agent>
}
