@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.uri.cs2mobile.ui.skins

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
import br.com.uri.cs2mobile.data.Skin
import coil.compose.AsyncImage

// Converte "#RRGGBB" / "#AARRGGBB" em Color Compose
private fun hexToComposeColor(hex: String): Color = try {
    val argb = android.graphics.Color.parseColor(hex)
    val a = (argb ushr 24 and 0xFF) / 255f
    val r = (argb ushr 16 and 0xFF) / 255f
    val g = (argb ushr  8 and 0xFF) / 255f
    val b = (argb         and 0xFF) / 255f
    Color(r, g, b, a)
} catch (_: Exception) { Color(0xFFB0BEC5) }

@Composable
fun SkinsScreen(
    onBack: () -> Unit,
    onOpenDetail: (Skin) -> Unit,
    vm: SkinsViewModel = viewModel()
) {
    val ui = vm.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CS2 Mobile - Skins") },
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
            // Busca
            OutlinedTextField(
                value = ui.searchText,
                onValueChange = vm::onSearchTextChanged,
                label = { Text("Buscar skin por nome...") },
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
                            FilledTonalButton(onClick = vm::retry) { Text("Tentar novamente") }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(ui.skins) { skin ->
                            SkinListItem(
                                skin = skin,
                                onClick = { onOpenDetail(skin) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SkinListItem(skin: Skin, onClick: () -> Unit) {
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
                model = skin.image,
                contentDescription = "Imagem da skin ${skin.name}",
                modifier = Modifier.size(80.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = skin.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                val weaponName = skin.weapon?.name
                val categoryName = skin.category?.name
                val secondary = listOfNotNull(weaponName, categoryName).joinToString(" · ")
                if (secondary.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = secondary,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFB0BEC5)
                    )
                }

                skin.rarity?.let { r ->
                    Spacer(Modifier.height(4.dp))
                    val rarityColor = r.color?.let { hexToComposeColor(it) } ?: Color(0xFFB0BEC5)
                    Text(
                        text = r.name ?: "Raridade desconhecida",
                        style = MaterialTheme.typography.bodySmall,
                        color = rarityColor
                    )
                }
            }
        }
    }
}
