package br.com.uri.cs2mobile.data

data class SkinCategory(
    val id: String?,
    val name: String?
)

data class SkinWeapon(
    val id: String?,
    val name: String?
)

data class Skin(
    val id: String,
    val name: String,
    val description: String?,
    val weapon: SkinWeapon?, // <-- A mudança para weapon está aqui
    val category: SkinCategory?,
    val pattern: String?,
    val rarity: String?,
    val image: String
)

// A classe Sticker permanece igual.
data class Sticker(
    val id: String,
    val name: String,
    val description: String?,
    val rarity: String?,
    val image: String
)