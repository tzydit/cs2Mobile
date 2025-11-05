package br.com.uri.cs2mobile.ui.crates

import android.util.Log
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

    private var fullList: List<Crate> = emptyList()

    var uiState by mutableStateOf(CratesUiState(isLoading = true))
        private set

    init { fetch() }

    fun retry() = fetch()

    private fun set(block: CratesUiState.() -> CratesUiState) {
        uiState = uiState.block()
    }

    private fun fetch() {
        viewModelScope.launch {
            set { copy(isLoading = true, error = null) }

            val langs = listOf("pt-BR", "en")
            var data: List<Crate>? = null
            var last: Throwable? = null

            for (lang in langs) {
                try {
                    val res = withContext(Dispatchers.IO) {
                        Log.d("CratesVM", "GET crates lang=$lang")
                        RetrofitInstance.api.getCrates(lang)
                    }
                    data = res
                    break
                } catch (e: Throwable) {
                    last = e
                    Log.e("CratesVM", "falha lang=$lang code=${(e as? HttpException)?.code()}", e)
                }
            }

            if (data != null) {
                fullList = data!!
                set { copy(isLoading = false, crates = fullList) }
            } else {
                val msg = when (last) {
                    is HttpException -> "Erro ${(last as HttpException).code()} ao buscar crates."
                    null -> "Falha desconhecida ao buscar crates."
                    else -> "Falha ao buscar crates: ${last?.message}"
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
                        (it.type ?: "").contains(newText, true)
            }
        set { copy(searchText = newText, crates = filtered) }
    }
}

data class CratesUiState(
    val crates: List<Crate> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
