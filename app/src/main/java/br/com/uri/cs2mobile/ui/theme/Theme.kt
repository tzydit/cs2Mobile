package br.com.uri.cs2mobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 1. Definimos nossa paleta de cores "gamer" personalizada.
//    - O prefixo 0xFF no início dos códigos de cor significa que a cor é totalmente opaca.
private val AppDarkColorScheme = darkColorScheme(
    // Cor primária/destaque (botões, links, bordas focadas)
    primary = Color(0xFF00BCD4), // Ciano Vibrante

    // Cor de fundo principal do app
    background = Color(0xFF121212), // Cinza quase preto

    // Cor para superfícies que ficam sobre o fundo (cards, menus)
    surface = Color(0xFF1F1F23), // Um cinza um pouco mais claro

    // --- Cores para textos e ícones ---
    // Cor do texto em cima de um fundo primário (ex: texto de um botão ciano)
    onPrimary = Color.Black,

    // Cor do texto em cima do fundo principal
    onBackground = Color.White,

    // Cor do texto em cima de superfícies (ex: texto dentro de um card)
    onSurface = Color.White
)

@Composable
fun Cs2mobileTheme( // O nome da sua função pode ser 'CS2MobileTheme' com 'M' maiúsculo
    content: @Composable () -> Unit
) {
    // 2. Removemos a lógica de tema claro e dinâmico.
    //    Agora o app usa nossa paleta escura personalizada em 100% do tempo.
    MaterialTheme(
        colorScheme = AppDarkColorScheme,
        typography = Typography,
        content = content
    )
}