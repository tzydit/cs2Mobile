@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.uri.cs2mobile.ui.stickers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.uri.cs2mobile.data.Sticker
import coil.compose.AsyncImage

@Composable
fun StickersScreen(
    onBack: () -> Unit,
    vm: StickersViewModel = viewModel()
) {
    val ui = vm.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CS2 Mobile - Adesivos") },
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
            // 🔎 Barra de busca (igual à de Skins)
            OutlinedTextField(
                value = ui.searchText,
                onValueChange = vm::onSearchTextChanged,
                label = { Text("Buscar adesivo por nome...") },
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
                enabled = !ui.isLoading
            )

            when {
                ui.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Cyan)
                    }
                }
                ui.error != null -> {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(ui.error!!, color = Color.Red, textAlign = TextAlign.Center)
                            Spacer(Modifier.height(12.dp))
                            FilledTonalButton(onClick = vm::retry) {
                                Text("Tentar novamente")
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(ui.stickers) { sticker ->
                            StickerItem(sticker = sticker) { /* clique futuro */ }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StickerItem(sticker: Sticker, onClick: () -> Unit) {
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
                model = sticker.image,
                contentDescription = "Imagem do adesivo ${sticker.name}",
                modifier = Modifier.size(80.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    sticker.name,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                sticker.rarity?.name?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(it, color = Color(0xFFB0BEC5), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
