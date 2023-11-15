package mobappdev.example.nback_cimpl


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mobappdev.example.nback_cimpl.ui.screens.HomeScreen
import mobappdev.example.nback_cimpl.ui.screens.PlayScreen
import mobappdev.example.nback_cimpl.ui.screens.SettingsScreen
import mobappdev.example.nback_cimpl.ui.theme.NBack_CImplTheme
import mobappdev.example.nback_cimpl.ui.viewmodels.GameVM


/**
 * This is the MainActivity of the application
 *
 * Your navigation between the two (or more) screens should be handled here
 * For this application you need at least a homescreen (a start is already made for you)
 * and a gamescreen (you will have to make yourself, but you can use the same viewmodel)
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NBack_CImplTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val PLAY_ROUTE = "play"
    const val SETTINGS_ROUTE = "settings"
}
@Composable
fun AppNavigation() {
    // Instantiate the viewmodel
    val gameViewModel: GameVM = viewModel(
        factory = GameVM.Factory
    )

    // Create a NavController
    val navController = rememberNavController()

    // Set up the NavHost with the navigation graph
    NavHost(navController = navController, startDestination = MainDestinations.HOME_ROUTE) {
        composable(MainDestinations.HOME_ROUTE) {
            HomeScreen(vm = gameViewModel, navController = navController)
        }
        composable(MainDestinations.PLAY_ROUTE) {
            PlayScreen(vm = gameViewModel, navController = navController)
        }
        composable(MainDestinations.SETTINGS_ROUTE) {
            SettingsScreen(vm = gameViewModel,navController = navController)
        }
    }
}


