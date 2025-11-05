@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.uri.cs2mobile.ui.crates

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.uri.cs2mobile.data.Crate
import coil.compose.AsyncImage

@Composable
fun CratesScreen(
    onBack: () -> Unit,
    vm: CratesViewModel = viewModel()
) {
    val ui = vm.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crates") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
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
        Column(Modifier.padding(pv).fillMaxSize()) {

            OutlinedTextField(
                value = ui.searchText,
                onValueChange = vm::onSearchTextChanged,
                label = { Text("Buscar crate (nome ou tipo)...") },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                singleLine = true,
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Cyan, unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Cyan, focusedLabelColor = Color.Cyan, unfocusedLabelColor = Color.Gray
                ),
                enabled = !ui.isLoading
            )

            when {
                ui.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Cyan)
                }
                ui.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(ui.error!!, color = Color.Red)
                        Spacer(Modifier.height(12.dp))
                        FilledTonalButton(onClick = vm::retry) { Text("Tentar novamente") }
                    }
                }
                else -> LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(ui.crates) { c -> CrateItem(c) { /* clique futuro */ } }
                }
            }
        }
    }
}

@Composable
private fun CrateItem(c: Crate, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2E))
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = c.image, contentDescription = c.name, modifier = Modifier.size(80.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(c.name, color = Color.White, style = MaterialTheme.typography.titleMedium)
                val sub = listOfNotNull(c.type, c.first_sale_date).joinToString(" · ")
                if (sub.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(sub, color = Color(0xFFB0BEC5), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
