package br.com.uri.cs2mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import br.com.uri.cs2mobile.ui.home.HomeScreen
import br.com.uri.cs2mobile.ui.skins.SkinsScreen
import br.com.uri.cs2mobile.ui.stickers.StickersScreen
import br.com.uri.cs2mobile.ui.highlights.HighlightsScreen
import br.com.uri.cs2mobile.ui.highlights.HighlightDetailScreen
import br.com.uri.cs2mobile.ui.crates.CratesScreen
import br.com.uri.cs2mobile.ui.crates.CrateDetailScreen

object Routes {
    const val HOME = "home"
    const val SKINS = "skins"
    const val STICKERS = "stickers"
    const val HIGHLIGHTS = "highlights"
    const val HIGHLIGHT_DETAIL = "highlight_detail"
    const val CRATES = "crates"
    const val CRATE_DETAIL = "crate_detail"
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
                onOpenCrates     = { navController.navigate(Routes.CRATES) }
            )
        }

        composable(Routes.SKINS)    { SkinsScreen(onBack = { navController.navigateUp() }) }
        composable(Routes.STICKERS) { StickersScreen(onBack = { navController.navigateUp() }) }

        composable(Routes.HIGHLIGHTS) {
            HighlightsScreen(
                onBack = { navController.navigateUp() },
                onOpenDetail = { h -> navController.navigate("${Routes.HIGHLIGHT_DETAIL}/${h.id}") }
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
                onOpenDetail = { crate -> navController.navigate("${Routes.CRATE_DETAIL}/${crate.id}") }
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
