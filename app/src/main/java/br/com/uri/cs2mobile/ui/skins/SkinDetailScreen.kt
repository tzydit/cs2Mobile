@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.uri.cs2mobile.ui.skins

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun SkinDetailScreen(
    id: String,
    onBack: () -> Unit,
    vm: SkinsViewModel = viewModel()
) {
    val ui = vm.uiState
    val skin = ui.skins.firstOrNull { it.id == id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(skin?.name ?: "Detalhes da Skin") },
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
                ui.isLoading && skin == null -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Cyan
                    )
                }

                skin == null -> {
                    Text(
                        text = "Não foi possível encontrar esta skin.",
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
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = skin.image,
                            contentDescription = skin.name,
                            modifier = Modifier
                                .size(220.dp)
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = skin.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(8.dp))

                        skin.description?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFCFD8DC),
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(12.dp))
                        }

                        val weapon = skin.weapon?.name ?: "—"
                        val category = skin.category?.name ?: "—"
                        val pattern = skin.pattern?.name ?: "—"
                        val rarity = skin.rarity?.name ?: "—"

                        InfoLine(label = "Arma", value = weapon)
                        InfoLine(label = "Categoria", value = category)
                        InfoLine(label = "Pattern", value = pattern)
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
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color(0xFFB0BEC5))
        Text(value, color = Color.White)
    }
}
