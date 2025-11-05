package br.com.uri.cs2mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.uri.cs2mobile.ui.home.HomeScreen
import br.com.uri.cs2mobile.ui.skins.SkinsScreen
import br.com.uri.cs2mobile.ui.stickers.StickersScreen
import br.com.uri.cs2mobile.ui.highlights.HighlightsScreen
import br.com.uri.cs2mobile.ui.highlights.HighlightDetailScreen
import br.com.uri.cs2mobile.ui.crates.CratesScreen

object Routes {
    const val HOME = "home"
    const val SKINS = "skins"
    const val STICKERS = "stickers"

    const val HIGHLIGHTS = "highlights"
    const val HIGHLIGHT_DETAIL = "highlight"          // usaremos /{id}

    const val CRATES = "crates"
}

@Composable
fun Cs2NavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        // Home
        composable(Routes.HOME) {
            HomeScreen(
                onOpenSkins = { navController.navigate(Routes.SKINS) },
                onOpenStickers = { navController.navigate(Routes.STICKERS) },
                onOpenHighlights = { navController.navigate(Routes.HIGHLIGHTS) },
                onOpenCrates = { navController.navigate(Routes.CRATES) }
            )
        }

        // Skins
        composable(Routes.SKINS) {
            SkinsScreen(onBack = { navController.navigateUp() })
        }

        // Stickers
        composable(Routes.STICKERS) {
            StickersScreen(onBack = { navController.navigateUp() })
        }

        // Highlights (lista)
        composable(Routes.HIGHLIGHTS) {
            HighlightsScreen(
                onBack = { navController.navigateUp() },
                onOpenDetail = { highlightId ->
                    navController.navigate("${Routes.HIGHLIGHT_DETAIL}/$highlightId")
                }
            )
        }

        // Highlight (detalhe)
        composable(
            route = "${Routes.HIGHLIGHT_DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            HighlightDetailScreen(
                id = id,
                onBack = { navController.navigateUp() }
            )
        }

        // Crates
        composable(Routes.CRATES) {
            CratesScreen(onBack = { navController.navigateUp() })
        }
    }
}
