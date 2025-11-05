package br.com.uri.cs2mobile.ui.highlights

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.uri.cs2mobile.data.Highlight
import br.com.uri.cs2mobile.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HighlightsViewModel : ViewModel() {

    private var fullList: List<Highlight> = emptyList()

    var uiState by mutableStateOf(HighlightsUiState(isLoading = true))
        private set

    init { fetch() }

    fun retry() = fetch()

    private fun set(block: HighlightsUiState.() -> HighlightsUiState) {
        uiState = uiState.block()
    }

    private fun fetch() {
        viewModelScope.launch {
            set { copy(isLoading = true, error = null) }

            val langs = listOf("pt-BR", "en")
            var data: List<Highlight>? = null
            var last: Throwable? = null

            for (lang in langs) {
                try {
                    val res = withContext(Dispatchers.IO) {
                        Log.d("HighlightsVM", "GET highlights lang=$lang")
                        RetrofitInstance.api.getHighlights(lang)
                    }
                    data = res
                    break
                } catch (e: Throwable) {
                    last = e
                    Log.e("HighlightsVM", "falha lang=$lang code=${(e as? HttpException)?.code()}", e)
                }
            }

            if (data != null) {
                fullList = data!!
                set { copy(isLoading = false, highlights = fullList) }
            } else {
                val msg = when (last) {
                    is HttpException -> "Erro ${(last as HttpException).code()} ao buscar highlights."
                    null -> "Falha desconhecida ao buscar highlights."
                    else -> "Falha ao buscar highlights: ${last?.message}"
                }
                set { copy(isLoading = false, error = msg) }
            }
        }
    }

    fun onSearchTextChanged(newText: String) {
        val filtered =
            if (newText.isBlank()) fullList
            else fullList.filter {
                it.name.contains(newText, true) ||
                        (it.tournament_event ?: "").contains(newText, true) ||
                        (it.map ?: "").contains(newText, true)
            }
        set { copy(searchText = newText, highlights = filtered) }
    }
}

data class HighlightsUiState(
    val highlights: List<Highlight> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
