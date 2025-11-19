package br.com.uri.cs2mobile.ui.crates

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
import androidx.compose.ui.unit.dp
import br.com.uri.cs2mobile.data.Crate
import br.com.uri.cs2mobile.data.CrateItem
import br.com.uri.cs2mobile.network.RetrofitInstance
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrateDetailScreen(
    id: String,
    onBack: () -> Unit
) {
    var crate by remember { mutableStateOf<Crate?>(null) }
    var itemsList by remember { mutableStateOf<List<CrateItem>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(id) {
        loading = true; error = null
        try {
            val langs = listOf("en")
            var found: Crate? = null
            for (lang in langs) {
                val list = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getCrates(lang)
                }
                found = list.firstOrNull { it.id == id }
                if (found != null) break
            }
            crate = found
            if (found == null) {
                error = "Crate não encontrada."
            } else {
                itemsList = (found.contains.orEmpty() + found.contains_rare.orEmpty())
            }
        } catch (e: Throwable) {
            error = "Falha ao carregar crate: ${e.message}"
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(crate?.name ?: "Crate") },
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
    ) { padding ->
        when {
            loading -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Cyan)
            }
            error != null -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(error!!, color = Color.Red)
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(itemsList) { it ->
                        CrateContainedItemRow(item = it)
                    }
                }
            }
        }
    }
}

@Composable
private fun CrateContainedItemRow(item: CrateItem) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2E))) {
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = item.image, contentDescription = item.name, modifier = Modifier.size(72.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(item.name ?: "Sem nome", color = Color.White, style = MaterialTheme.typography.titleMedium)
                item.rarity?.name?.let {
                    Spacer(Modifier.height(2.dp))
                    Text(it, color = Color(0xFFB0BEC5), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
