package mobappdev.example.nback_cimpl.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel
import java.io.IOException

/**
 * This repository provides a way to interact with the DataStore api,
 * with this API you can save key:value pairs
 *
 * Currently this file contains only one thing: getting the highscore as a flow
 * and writing to the highscore preference.
 * (a flow is like a waterpipe; if you put something different in the start,
 * the end automatically updates as long as the pipe is open)
 *
 * Date: 25-08-2023
 * Version: Skeleton code version 1.0
 * Author: Yeetivity
 *
 */

data class UserPreferences(
    val highScore: Int,
    val eventTime: Long,
    val eventLength: Int
)

class UserPreferencesRepository (
    private val dataStore: DataStore<Preferences>
){
     companion object {
        val HIGHSCORE = intPreferencesKey("highscore")
        val EVENT_TIME = longPreferencesKey("event_time")
        val EVENT_LENGTH = intPreferencesKey("event_length")
        const val TAG = "UserPreferencesRepo"
    }

    val highscore: Flow<Int> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[HIGHSCORE] ?: 0
        }

    suspend fun saveHighScore(score: Int) {
        dataStore.edit { preferences ->
            preferences[HIGHSCORE] = score
        }
    }

    suspend fun saveEventTime(score: Long) {
        dataStore.edit { preferences ->
            preferences[EVENT_TIME] = score
        }
    }

    suspend fun saveEventLength(score: Int) {
        dataStore.edit { preferences ->
            preferences[EVENT_LENGTH] = score
        }
    }

     suspend fun saveData(gameViewModel: GameViewModel){

        saveHighScore(gameViewModel.highscore.value)
        saveEventTime(gameViewModel.eventInterval.value)
        saveEventLength(gameViewModel.event_length.value)
    }
/*
   suspend fun loadData(){
       /      CoroutineScope(Dispatchers.IO).launch {
           val highScore = userPreferencesRepository.readHighScore().first()
           val eventTime = userPreferencesRepository.readEventTime().first()
           val eventLength = userPreferencesRepository.readEventLength().first()
           // Use these values to initialize your game or ViewModel


       }

   }
*/
val userPreferences: Flow<UserPreferences> = dataStore.data
    .catch { exception ->
        if (exception is IOException) {
            Log.e(TAG, "Error reading preferences.", exception)
            emit(emptyPreferences()) // Fallback to empty preferences
        } else {
            throw exception
        }
    }
    .map { preferences ->
        UserPreferences(
            highScore = preferences[HIGHSCORE] ?: 0,
            eventTime = preferences[EVENT_TIME] ?: 2000L,
            eventLength = preferences[EVENT_LENGTH] ?: 10
        )
    }

}