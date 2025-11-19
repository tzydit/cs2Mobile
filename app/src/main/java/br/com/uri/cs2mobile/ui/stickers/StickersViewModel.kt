package br.com.uri.cs2mobile.ui.stickers

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.uri.cs2mobile.data.Sticker
import br.com.uri.cs2mobile.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class StickersViewModel : ViewModel() {

    private var fullList: List<Sticker> = emptyList()

    var uiState by mutableStateOf(StickersUiState(isLoading = true))
        private set

    init { fetchStickers() }

    fun retry() = fetchStickers()

    private fun set(block: StickersUiState.() -> StickersUiState) {
        uiState = uiState.block()
    }

    private fun fetchStickers() {
        viewModelScope.launch {
            set { copy(isLoading = true, error = null) }

            val langs = listOf("en")
            var data: List<Sticker>? = null
            var last: Throwable? = null

            for (lang in langs) {
                try {
                    val result = withContext(Dispatchers.IO) {
                        Log.d("StickersVM", "GET stickers lang=$lang")
                        RetrofitInstance.api.getStickers(lang)
                    }
                    data = result
                    break
                } catch (e: Throwable) {
                    last = e
                    Log.e("StickersVM", "lang=$lang falhou (code=${(e as? HttpException)?.code()})", e)
                }
            }

            if (data != null) {
                fullList = data!!
                set { copy(isLoading = false, stickers = fullList, error = null) }
            } else {
                val msg = when (last) {
                    is HttpException -> "Erro ${(last as HttpException).code()} ao buscar adesivos."
                    null -> "Falha desconhecida ao buscar adesivos."
                    else -> "Falha ao buscar adesivos: ${last?.message}"
                }
                set { copy(isLoading = false, error = msg) }
            }
        }
    }

    fun onSearchTextChanged(newText: String) {
        val filtered =
            if (newText.isBlank()) fullList
            else fullList.filter { it.name.contains(newText, ignoreCase = true) }

        set { copy(searchText = newText, stickers = filtered) }
    }
}

data class StickersUiState(
    val stickers: List<Sticker> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
