package br.com.uri.cs2mobile.ui.crates

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.uri.cs2mobile.data.Crate
import br.com.uri.cs2mobile.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CratesViewModel : ViewModel() {

    var uiState by mutableStateOf(
        CratesUiState(isLoading = true, searchText = "")
    )
        private set

    private var allCrates: List<Crate> = emptyList()

    init { fetch() }

    fun retry() = fetch()

    fun onSearchTextChanged(newText: String) {
        uiState = uiState.copy(searchText = newText)
        applyFilter()
    }

    private fun fetch() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)

            val langs = listOf("pt-BR", "en")
            var loaded: List<Crate>? = null
            var last: Throwable? = null

            for (lang in langs) {
                try {
                    loaded = withContext(Dispatchers.IO) {
                        RetrofitInstance.api.getCrates(lang)
                    }
                    break
                } catch (t: Throwable) {
                    last = t
                }
            }

            if (loaded != null) {
                allCrates = loaded
                uiState = uiState.copy(isLoading = false)
                applyFilter()
            } else {
                val msg = when (last) {
                    is HttpException -> "Erro ${(last as HttpException).code()} ao buscar crates."
                    null -> "Falha desconhecida ao buscar crates."
                    else -> "Falha ao buscar crates: ${last?.message}"
                }
                uiState = uiState.copy(isLoading = false, error = msg)
            }
        }
    }

    private fun applyFilter() {
        val q = uiState.searchText.trim().lowercase()
        val filtered = if (q.isEmpty()) {
            allCrates
        } else {
            allCrates.filter { c ->
                val name = c.name.orEmpty().lowercase()
                val type = c.type.orEmpty().lowercase()
                val date = c.first_sale_date.orEmpty().lowercase()
                name.contains(q) || type.contains(q) || date.contains(q)
            }
        }
        uiState = uiState.copy(crates = filtered)
    }
}

data class CratesUiState(
    val crates: List<Crate> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
