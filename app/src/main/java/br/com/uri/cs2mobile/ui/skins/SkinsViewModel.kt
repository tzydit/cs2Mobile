package br.com.uri.cs2mobile.ui.skins

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.uri.cs2mobile.data.Skin
import br.com.uri.cs2mobile.network.RetrofitInstance
import kotlinx.coroutines.launch

class SkinsViewModel : ViewModel() {

    private var fullSkinsList: List<Skin> = listOf()

    var uiState by mutableStateOf(SkinsUiState())
        private set

    init {
        fetchSkins()
    }

    private fun fetchSkins() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true) // Inicia o estado de carregamento
            try {
                val skins = RetrofitInstance.api.getSkins()
                fullSkinsList = skins
                uiState = uiState.copy(skins = skins, isLoading = false)
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = "Falha ao carregar skins.")
                // ✅ MUDE A LINHA ABAIXO
                e.printStackTrace() // Isso vai imprimir o erro completo no Logcat
            }
        }
    }

    fun onSearchTextChanged(newText: String) {
        val filteredList = if (newText.isBlank()) {
            fullSkinsList
        } else {
            fullSkinsList.filter { skin ->
                skin.name.contains(newText, ignoreCase = true)
            }
        }
        uiState = uiState.copy(searchText = newText, skins = filteredList)
    }

    data class SkinsUiState(
        val skins: List<Skin> = emptyList(),
        val searchText: String = "",
        val isLoading: Boolean = false,
        val error: String? = null
    )
}