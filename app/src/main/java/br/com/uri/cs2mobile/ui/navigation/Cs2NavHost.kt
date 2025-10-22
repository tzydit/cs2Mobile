package br.com.uri.cs2mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.uri.cs2mobile.ui.home.HomeScreen
import br.com.uri.cs2mobile.ui.skins.SkinsScreen
import br.com.uri.cs2mobile.ui.stickers.StickersScreen

object Routes {
    const val HOME = "home"
    const val SKINS = "skins"
    const val STICKERS = "stickers"
}

@Composable
fun Cs2NavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onOpenSkins = { navController.navigate(Routes.SKINS) },
                onOpenStickers = { navController.navigate(Routes.STICKERS) }
            )
        }
        composable(Routes.SKINS) {
            SkinsScreen(onBack = { navController.navigateUp() })
        }
        composable(Routes.STICKERS) {
            StickersScreen(onBack = { navController.navigateUp() })
        }
    }
}
