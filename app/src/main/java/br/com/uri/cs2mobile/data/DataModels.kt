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

data class Highlight(
    val id: String,
    val name: String,
    val description: String?,
    val tournament_event: String?,
    val team0: String?,
    val team1: String?,
    val stage: String?,
    val map: String?,
    val market_hash_name: String?,
    val image: String?,
    val video: String?
)

data class CrateItem(
    val id: String?,
    val name: String?,
    val rarity: RarityLite?,
    val paint_index: String?,
    val image: String?
)

data class RarityLite(
    val id: String?,
    val name: String?,
    val color: String?
)

data class Crate(
    val id: String,
    val name: String,
    val description: String?,
    val type: String?,
    val first_sale_date: String?,
    val contains: List<CrateItem>?,
    val contains_rare: List<CrateItem>?,
    val market_hash_name: String?,
    val rental: Boolean?,
    val image: String?
)

data class Agent(
    @Json(name = "id") val id: String = "",
    @Json(name = "name") val name: String = "",
    @Json(name = "description") val description: String? = null,
    @Json(name = "image") val image: String? = null,
    @Json(name = "team") val team: TeamInfo? = null,
    @Json(name = "rarity") val rarity: RarityInfo? = null
)

data class TeamInfo(
    @Json(name = "id") val id: String? = null,
    @Json(name = "name") val name: String? = null
)
