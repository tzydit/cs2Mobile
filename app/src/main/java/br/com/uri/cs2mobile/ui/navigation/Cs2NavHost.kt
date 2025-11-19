package br.com.uri.cs2mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import br.com.uri.cs2mobile.ui.home.HomeScreen
import br.com.uri.cs2mobile.ui.skins.SkinsScreen
import br.com.uri.cs2mobile.ui.skins.SkinDetailScreen
import br.com.uri.cs2mobile.ui.stickers.StickersScreen
import br.com.uri.cs2mobile.ui.stickers.StickerDetailScreen
import br.com.uri.cs2mobile.ui.highlights.HighlightsScreen
import br.com.uri.cs2mobile.ui.highlights.HighlightDetailScreen
import br.com.uri.cs2mobile.ui.crates.CratesScreen
import br.com.uri.cs2mobile.ui.crates.CrateDetailScreen
import br.com.uri.cs2mobile.ui.agents.AgentsScreen

object Routes {
    const val HOME = "home"

    const val SKINS = "skins"
    const val SKIN_DETAIL = "skin_detail"

    const val STICKERS = "stickers"
    const val STICKER_DETAIL = "sticker_detail"

    const val HIGHLIGHTS = "highlights"
    const val HIGHLIGHT_DETAIL = "highlight_detail"

    const val CRATES = "crates"
    const val CRATE_DETAIL = "crate_detail"

    const val AGENTS = "agents"
}

@Composable
fun Cs2NavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onOpenSkins      = { navController.navigate(Routes.SKINS) },
                onOpenStickers   = { navController.navigate(Routes.STICKERS) },
                onOpenHighlights = { navController.navigate(Routes.HIGHLIGHTS) },
                onOpenCrates     = { navController.navigate(Routes.CRATES) },
                onOpenAgents     = { navController.navigate(Routes.AGENTS) }
            )
        }

        composable(Routes.SKINS) {
            SkinsScreen(
                onBack = { navController.navigateUp() },
                onOpenDetail = { skin ->
                    navController.navigate("${Routes.SKIN_DETAIL}/${skin.id}")
                }
            )
        }

        composable(
            route = "${Routes.SKIN_DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("id") ?: return@composable
            SkinDetailScreen(
                id = id,
                onBack = { navController.navigateUp() }
            )
        }

        composable(Routes.STICKERS) {
            StickersScreen(
                onBack = { navController.navigateUp() },
                onOpenDetail = { sticker ->
                    navController.navigate("${Routes.STICKER_DETAIL}/${sticker.id}")
                }
            )
        }

        composable(
            route = "${Routes.STICKER_DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("id") ?: return@composable
            StickerDetailScreen(
                id = id,
                onBack = { navController.navigateUp() }
            )
        }

        composable(Routes.AGENTS) {
            AgentsScreen(onBack = { navController.navigateUp() })
        }

        composable(Routes.HIGHLIGHTS) {
            HighlightsScreen(
                onBack = { navController.navigateUp() },
                onOpenDetail = { h ->
                    navController.navigate("${Routes.HIGHLIGHT_DETAIL}/${h.id}")
                }
            )
        }
        composable(
            route = "${Routes.HIGHLIGHT_DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("id") ?: return@composable
            HighlightDetailScreen(id = id, onBack = { navController.navigateUp() })
        }

        composable(Routes.CRATES) {
            CratesScreen(
                onBack = { navController.navigateUp() },
                onOpenDetail = { crate ->
                    navController.navigate("${Routes.CRATE_DETAIL}/${crate.id}")
                }
            )
        }
        composable(
            route = "${Routes.CRATE_DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("id") ?: return@composable
            CrateDetailScreen(id = id, onBack = { navController.navigateUp() })
        }
    }
}
