package br.com.uri.cs2mobile.data

import com.squareup.moshi.Json

data class Skin(
    @Json(name = "id") val id: String = "",
    @Json(name = "name") val name: String = "",
    @Json(name = "description") val description: String? = null,

    @Json(name = "weapon") val weapon: WeaponInfo? = null,
    @Json(name = "category") val category: CategoryInfo? = null,
    @Json(name = "pattern") val pattern: PatternInfo? = null,
    @Json(name = "rarity") val rarity: RarityInfo? = null,

    @Json(name = "image") val image: String = ""
)

data class WeaponInfo(
    @Json(name = "id") val id: String? = null,
    @Json(name = "name") val name: String? = null
)

data class CategoryInfo(
    @Json(name = "id") val id: String? = null,
    @Json(name = "name") val name: String? = null
)

data class PatternInfo(
    @Json(name = "id") val id: String? = null,
    @Json(name = "name") val name: String? = null
)

data class RarityInfo(
    @Json(name = "id") val id: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "color") val color: String? = null
)

data class Sticker(
    @Json(name = "id") val id: String = "",
    @Json(name = "name") val name: String = "",
    @Json(name = "description") val description: String? = null,
    @Json(name = "rarity") val rarity: RarityInfo? = null,
    @Json(name = "image") val image: String = ""
)
