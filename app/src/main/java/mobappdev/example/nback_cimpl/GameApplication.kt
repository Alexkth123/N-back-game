package mobappdev.example.nback_cimpl

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import mobappdev.example.nback_cimpl.data.UserPreferencesRepository
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel

/**
 * This is the Main Application
 *
 * Often your app doesn't even need this (custom) class, since the default
 * Application() class is enough. However, since we want to use DataStore for
 * saving the highscore, we create a custom version that inherits from the default
 * Application class.
 *
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */

private const val APP_PREFERENCES_NAME = "game_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = APP_PREFERENCES_NAME
)


/*
* Custom app entry point for manual dependency injection
 */
class GameApplication: Application() {
    lateinit var userPreferencesRespository: UserPreferencesRepository
    lateinit var vm : GameViewModel
   // lateinit var audioPlayer: AudioPlayer
    override fun onCreate() {
        super.onCreate()
        userPreferencesRespository = UserPreferencesRepository(dataStore)


       // Load settings on application start
      // CoroutineScope(Dispatchers.IO).launch {
           // Implement a function in your repository to load all necessary data
           // and then initialize your view models with this data.
          // userPreferencesRespository.loadData()
      // }

    }

}