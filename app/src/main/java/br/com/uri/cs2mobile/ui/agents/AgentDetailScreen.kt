@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.uri.cs2mobile.ui.agents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun AgentDetailScreen(
    id: String,
    onBack: () -> Unit,
    vm: AgentsViewModel = viewModel()
) {
    val agents by vm.agents.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    val agent = remember(agents, id) {
        agents.firstOrNull { it.id == id }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(agent?.name ?: "Detalhes do Agente") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1F1F23),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF121212)
    ) { pv ->
        Box(
            modifier = Modifier
                .padding(pv)
                .fillMaxSize()
        ) {
            when {
                isLoading && agent == null -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Cyan
                    )
                }

                agent == null -> {
                    Text(
                        text = "Não foi possível encontrar este agente ou a lista está vazia.",
                        color = Color.Red,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        textAlign = TextAlign.Center
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = agent.image,
                            contentDescription = agent.name,
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(Modifier.height(24.dp))

                        Text(
                            text = agent.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(16.dp))

                        agent.description?.let { desc ->
                            Text(
                                text = desc,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFCFD8DC),
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(24.dp))
                        }

                        val team = agent.team?.name ?: "—"
                        val rarity = agent.rarity?.name ?: "—"

                        HorizontalDivider(color = Color(0xFF2A2A2E))
                        Spacer(Modifier.height(12.dp))

                        InfoLine(label = "Equipe", value = team)
                        InfoLine(label = "Raridade", value = rarity)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color(0xFFB0BEC5), style = MaterialTheme.typography.bodyLarge)
        Text(value, color = Color.White, style = MaterialTheme.typography.bodyLarge)
    }
}