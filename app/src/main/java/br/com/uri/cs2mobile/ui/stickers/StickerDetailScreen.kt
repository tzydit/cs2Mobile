@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.uri.cs2mobile.ui.stickers

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
fun StickerDetailScreen(
    id: String,
    onBack: () -> Unit,
    vm: StickersViewModel = viewModel()
) {
    val ui = vm.uiState
    val sticker = ui.stickers.firstOrNull { it.id == id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(sticker?.name ?: "Detalhes do adesivo") },
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
                ui.isLoading && sticker == null -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Cyan
                    )
                }

                sticker == null -> {
                    Text(
                        text = "Não foi possível encontrar este adesivo.",
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
                            model = sticker.image,
                            contentDescription = sticker.name,
                            modifier = Modifier.size(220.dp)
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = sticker.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(8.dp))

                        sticker.description?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFCFD8DC),
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(12.dp))
                        }

                        val rarity = sticker.rarity?.name ?: "—"
                        Text(
                            text = "Raridade: $rarity",
                            color = Color(0xFFB0BEC5),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
