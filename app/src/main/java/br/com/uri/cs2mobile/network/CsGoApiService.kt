package br.com.uri.cs2mobile.network

import br.com.uri.cs2mobile.data.Skin
import br.com.uri.cs2mobile.data.Sticker
import br.com.uri.cs2mobile.data.Crate
import br.com.uri.cs2mobile.data.Highlight
import retrofit2.http.GET
import retrofit2.http.Path

interface CsGoApiService {

    @GET("{lang}/skins.json")
    suspend fun getSkins(@Path("lang") language: String): List<Skin>

    @GET("{lang}/stickers.json")
    suspend fun getStickers(@Path("lang") language: String): List<Sticker>

    @GET("public/api/{lang}/highlights.json")
    suspend fun getHighlights(@Path("lang") lang: String): List<Highlight>

    @GET("public/api/{lang}/crates.json")
    suspend fun getCrates(@Path("lang") lang: String): List<Crate>
}
