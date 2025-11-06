package br.com.uri.cs2mobile.ui.highlights

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import br.com.uri.cs2mobile.data.Highlight
import br.com.uri.cs2mobile.network.RetrofitInstance
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighlightDetailScreen(
    id: String,
    onBack: () -> Unit
) {
    var highlight by remember { mutableStateOf<Highlight?>(null) }
    var loading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(id) {
        loading = true; errorMsg = null
        try {
            val langs = listOf("pt-BR", "en")
            var found: Highlight? = null
            for (lang in langs) {
                val list = withContext(Dispatchers.IO) { RetrofitInstance.api.getHighlights(lang) }
                found = list.firstOrNull { it.id == id }
                if (found != null) break
            }
            if (found == null) errorMsg = "Highlight não encontrado."
            highlight = found
        } catch (e: Throwable) {
            errorMsg = "Falha ao carregar highlight: ${e.message}"
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(highlight?.name ?: "Highlight") },
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
            loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = Color.Cyan) }

            errorMsg != null -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) { Text(errorMsg!!, color = Color.Red) }

            else -> {
                val h = highlight!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Text(h.name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    h.description?.let { Text(it, color = Color(0xFFB0BEC5)) }
                    Spacer(Modifier.height(16.dp))

                    val url = h.video?.trim().orEmpty()
                    if (url.isBlank()) {
                        Text("Vídeo não disponível para este highlight.", color = Color(0xFFB0BEC5))
                    } else {
                        val context = LocalContext.current
                        var playerError by remember { mutableStateOf<String?>(null) }

                        val player = remember(url) {
                            try {
                                ExoPlayer.Builder(context).build().apply {
                                    setMediaItem(MediaItem.fromUri(url))
                                    prepare()
                                    playWhenReady = true
                                    addListener(object : Player.Listener {
                                        override fun onPlayerError(error: PlaybackException) {
                                            playerError = "Erro no player: ${error.message}"
                                        }
                                    })
                                }
                            } catch (e: Throwable) {
                                playerError = "Falha ao iniciar o player: ${e.message}"
                                null
                            }
                        }

                        DisposableEffect(player) { onDispose { player?.release() } }

                        if (player != null && playerError == null) {
                            AndroidView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp),
                                factory = { ctx ->
                                    PlayerView(ctx).apply {
                                        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                                        useController = true
                                        this.player = player
                                    }
                                }
                            )
                        } else {
                            Text(playerError ?: "Vídeo indisponível.", color = Color.Red)
                        }
                    }
                }
            }
        }
    }
}
