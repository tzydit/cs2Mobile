package br.com.uri.cs2mobile.ui.highlights

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

data class HighlightsUiState(
    val highlights: List<Highlight> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class HighlightsViewModel : ViewModel() {

    var uiState by mutableStateOf(HighlightsUiState(isLoading = true))
        private set

    init { fetch() }

    fun onSearchTextChanged(t: String) {
        uiState = uiState.copy(searchText = t)
    }

    fun retry() = fetch()

    private fun set(block: HighlightsUiState.() -> HighlightsUiState) {
        uiState = uiState.block()
    }

    private fun fetch() {
        viewModelScope.launch {
            set { copy(isLoading = true, error = null) }
            val langs = listOf("en")
            var data: List<Highlight>? = null
            var last: Throwable? = null
            for (lang in langs) {
                try {
                    val list = withContext(Dispatchers.IO) {
                        RetrofitInstance.api.getHighlights(lang)
                    }
                    data = list
                    break
                } catch (e: Throwable) {
                    last = e
                }
            }
            if (data != null) {
                set { copy(isLoading = false, highlights = data!!, error = null) }
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
}
