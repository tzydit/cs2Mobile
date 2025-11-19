package br.com.uri.cs2mobile.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.uri.cs2mobile.data.Skin
import br.com.uri.cs2mobile.network.RetrofitInstance
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val csOrange = Color(0xFFFE9600)
private val cardGray = Color(0xFF2A2A2E)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(
    onOpenSkins: () -> Unit,
    onOpenStickers: () -> Unit,
    onOpenHighlights: () -> Unit,
    onOpenCrates: () -> Unit,
    onOpenAgents: () -> Unit // ✅ Novo parâmetro
) {

    var featuredSkins by remember { mutableStateOf<List<Skin>>(emptyList()) }
    var isLoadingSkins by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoadingSkins = true
        var skinsList: List<Skin>? = null
        val langs = listOf("en")

        for (lang in langs) {
            try {
                skinsList = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getSkins(lang)
                }
                break
            } catch (e: Exception) {
                Log.e("HomeScreen", "Falha ao buscar skins com lang=$lang", e)
            }
        }

        if (skinsList != null) {
            featuredSkins = skinsList.shuffled().take(10)
        }
        isLoadingSkins = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CS2 Mobile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            item { WelcomeSection() }

            item {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Destaques da Comunidade",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(12.dp))

                if (isLoadingSkins) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = csOrange)
                    }
                } else {
                    FeaturedSkinsRow(skins = featuredSkins)
                }
                Spacer(Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Explorar Categorias",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(12.dp))
            }

            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeCategoryCard("Skins", onOpenSkins, Modifier.weight(1f))
                    HomeCategoryCard("Adesivos", onOpenStickers, Modifier.weight(1f))
                }
            }

            item { Spacer(Modifier.height(16.dp)) }

            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeCategoryCard("Highlights", onOpenHighlights, Modifier.weight(1f))
                    HomeCategoryCard("Crates", onOpenCrates, Modifier.weight(1f))
                }
            }

            item { Spacer(Modifier.height(16.dp)) }

            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeCategoryCard("Agentes", onOpenAgents, Modifier.weight(1f))
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun WelcomeSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = "Bem-vindo ao CS2 Mobile",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Explore skins, adesivos, caixas, agentes e os melhores momentos do jogo diretamente no celular.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFFB0BEC5)
        )
    }
}

@Composable
private fun FeaturedSkinsRow(skins: List<Skin>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(skins) { skin ->
            FeaturedSkinItem(skin = skin)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FeaturedSkinItem(skin: Skin) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardGray),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        AsyncImage(
            model = skin.image,
            contentDescription = skin.name,
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center
        )
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeCategoryCard(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardGray
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = csOrange
            )
        }
    }
}