package br.com.uri.cs2mobile.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onOpenSkins: () -> Unit,
    onOpenStickers: () -> Unit,
    onOpenHighlights: () -> Unit,
    onOpenCrates: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onOpenSkins) { Text("Ver todas as Skins") }
            Button(onClick = onOpenStickers) { Text("Ver todos os Adesivos") }
            Button(onClick = onOpenHighlights) { Text("Ver Highlights") }
            Button(onClick = onOpenCrates) { Text("Ver Crates") }
        }
    }
}
