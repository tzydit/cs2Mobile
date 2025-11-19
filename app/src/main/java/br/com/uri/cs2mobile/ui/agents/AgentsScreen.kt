@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.uri.cs2mobile.ui.agents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.uri.cs2mobile.data.Agent
import coil.compose.AsyncImage

private fun hexToComposeColor(hex: String): Color = try {
    val argb = android.graphics.Color.parseColor(hex)
    val a = (argb ushr 24 and 0xFF) / 255f
    val r = (argb ushr 16 and 0xFF) / 255f
    val g = (argb ushr  8 and 0xFF) / 255f
    val b = (argb         and 0xFF) / 255f
    Color(r, g, b, a)
} catch (_: Exception) { Color(0xFFB0BEC5) }

@Composable
fun AgentsScreen(
    onBack: () -> Unit,
    onOpenDetail: (Agent) -> Unit,
    vm: AgentsViewModel = viewModel()
) {
    val agents by vm.agents.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    var searchText by remember { mutableStateOf("") }

    val filteredAgents = remember(agents, searchText) {
        if (searchText.isBlank()) agents
        else agents.filter { it.name.contains(searchText, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CS2 Mobile - Agentes") },
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
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar agente por nome...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Cyan,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Cyan,
                    focusedLabelColor = Color.Cyan,
                    unfocusedLabelColor = Color.Gray
                ),
                enabled = !isLoading
            )

            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Cyan)
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredAgents) { agent ->
                            AgentListItem(
                                agent = agent,
                                onClick = { onOpenDetail(agent) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AgentListItem(agent: Agent, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2E))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = agent.image,
                contentDescription = "Imagem do agente ${agent.name}",
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    text = agent.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                val teamName = agent.team?.name ?: ""

                if (teamName.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = teamName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFB0BEC5)
                    )
                }

                agent.rarity?.let { r ->
                    Spacer(Modifier.height(4.dp))
                    val rarityColor = r.color?.let { hexToComposeColor(it) } ?: Color(0xFFB0BEC5)
                    Text(
                        text = r.name ?: "Raridade",
                        style = MaterialTheme.typography.bodySmall,
                        color = rarityColor
                    )
                } ?: run {
                    Spacer(Modifier.height(4.dp))
                    val teamColor = when {
                        teamName.contains("Counter", true) -> Color(0xFF5D79AE)
                        teamName.contains("Terrorist", true) -> Color(0xFFDE9B35)
                        else -> Color.Gray
                    }
                    Text(
                        text = if (teamName.isNotBlank()) teamName else "Agente",
                        style = MaterialTheme.typography.bodySmall,
                        color = teamColor
                    )
                }
            }
        }
    }
}