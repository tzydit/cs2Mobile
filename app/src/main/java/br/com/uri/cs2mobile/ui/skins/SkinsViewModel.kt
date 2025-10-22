package br.com.uri.cs2mobile.ui.skins

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.uri.cs2mobile.data.Skin
import br.com.uri.cs2mobile.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class SkinsViewModel : ViewModel() {

    private var fullSkinsList: List<Skin> = emptyList()

    var uiState by mutableStateOf(SkinsUiState(isLoading = true))
        private set

    init { fetchSkins() }

    fun retry() = fetchSkins()

    private fun set(block: SkinsUiState.() -> SkinsUiState) {
        uiState = uiState.block()
    }

    private fun fetchSkins() {
        viewModelScope.launch {
            set { copy(isLoading = true, error = null) }

            val langs = listOf("pt-BR", "en")
            var data: List<Skin>? = null
            var lastErr: Throwable? = null

            for (lang in langs) {
                try {
                    val result = withContext(Dispatchers.IO) {
                        Log.d("SkinsVM", "GET skins lang=$lang")
                        RetrofitInstance.api.getSkins(lang)
                    }
                    data = result
                    break
                } catch (e: Throwable) {
                    lastErr = e
                    Log.e("SkinsVM", "lang=$lang falhou (code=${(e as? HttpException)?.code()})", e)
                }
            }

            if (data != null) {
                fullSkinsList = data!!
                set { copy(isLoading = false, skins = fullSkinsList, error = null) }
            } else {
                val msg = when (lastErr) {
                    is HttpException -> "Erro ${(lastErr as HttpException).code()} ao buscar skins."
                    null -> "Falha desconhecida ao buscar skins."
                    else -> "Falha ao buscar skins: ${lastErr?.message}"
                }
                set { copy(isLoading = false, error = msg) }
            }
        }
    }

    fun onSearchTextChanged(newText: String) {
        val filtered =
            if (newText.isBlank()) fullSkinsList
            else fullSkinsList.filter { it.name.contains(newText, ignoreCase = true) }

        set { copy(searchText = newText, skins = filtered) }
    }
}

data class SkinsUiState(
    val skins: List<Skin> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
