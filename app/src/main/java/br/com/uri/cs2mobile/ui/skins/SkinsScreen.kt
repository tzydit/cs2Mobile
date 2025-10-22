@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.uri.cs2mobile.ui.skins

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun SkinsScreen(viewModel: SkinsViewModel = viewModel()) {
    val uiState = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CS2 Mobile - Skins") },
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
                value = uiState.searchText,
                onValueChange = viewModel::onSearchTextChanged,
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
                )
            )

            when {
                uiState.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Cyan)
                    }
                }
                uiState.error != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(uiState.error, color = Color.Red, textAlign = TextAlign.Center)
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.skins) { skin ->
                            SkinListItem(
                                skin = skin,
                                onClick = { println("Clicou em: ${skin.name}") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SkinListItem(skin: Skin, onClick: () -> Unit) {
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
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = skin.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                skin.rarity?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF00BCD4)
                    )
                }
            }
        }
    }
}
