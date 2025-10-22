@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package br.com.uri.cs2mobile.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onOpenSkins: () -> Unit,
    onOpenStickers: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CS2 Mobile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1F1F23),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF121212)
    ) { pv ->
        Column(
            modifier = Modifier
                .padding(pv)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Escolha o que deseja ver:",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )

            FilledTonalButton(
                onClick = onOpenSkins,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ver todas as skins") }

            FilledTonalButton(
                onClick = onOpenStickers,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ver todos os adesivos") }
        }
    }
}
