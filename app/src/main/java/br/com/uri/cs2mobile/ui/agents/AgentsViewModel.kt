package br.com.uri.cs2mobile.ui.agents

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.uri.cs2mobile.data.Agent
import br.com.uri.cs2mobile.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AgentsViewModel : ViewModel() {

    private val _agents = MutableStateFlow<List<Agent>>(emptyList())
    val agents: StateFlow<List<Agent>> = _agents.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchAgents()
    }

    private fun fetchAgents() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Log.d("AgentsViewModel", "Iniciando busca de agentes...")

                val response = RetrofitInstance.api.getAgents("en")

                Log.d("AgentsViewModel", "Sucesso! Encontrados: ${response.size} agentes.")
                _agents.value = response

            } catch (e: Exception) {
                Log.e("AgentsViewModel", "ERRO ao buscar agentes: ${e.message}", e)
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}