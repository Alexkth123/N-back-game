package mobappdev.example.nback_cimpl.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.GameApplication
import mobappdev.example.nback_cimpl.NBackHelper
import mobappdev.example.nback_cimpl.data.UserPreferencesRepository
import mobappdev.example.nback_cimpl.ui.screens.AudioPlayer

/**
 * This is the GameViewModel.
 *
 * It is good practice to first make an interface, which acts as the blueprint
 * for your implementation. With this interface we can create fake versions
 * of the viewmodel, which we can use to test other parts of our app that depend on the VM.
 *
 * Our viewmodel itself has functions to start a game, to specify a gametype,
 * and to check if we are having a match
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */


interface GameViewModel {
    val gameState: StateFlow<GameState>
    val score: StateFlow<Int>
    val highscore: StateFlow<Int>
    val nBack: Int
    val btnState: StateFlow<Boolean>
    val showValue: StateFlow<Int>




    fun setGameType(gameType: GameType)
    fun startGame()

    fun checkMatch(int: Int)
    fun btn_press()
}

class GameVM(
    application: Application,
    private val userPreferencesRepository: UserPreferencesRepository
): GameViewModel, ViewModel() {


    private val context = application.applicationContext
    private lateinit var audioPlayer: AudioPlayer

    private val _gameState = MutableStateFlow(GameState())
    override val gameState: StateFlow<GameState>
        get() = _gameState.asStateFlow()


    private val _score = MutableStateFlow(0)
    override val score: StateFlow<Int>
        get() = _score

    private val _highscore = MutableStateFlow(0)
    //override val highscore: StateFlow<Int>
    override val highscore: StateFlow<Int>
        get() = _highscore

    private val _btnState = MutableStateFlow(false)
    override val btnState: StateFlow<Boolean>
        get() = _btnState

    private val _showValue = MutableStateFlow(0)
    override val showValue: StateFlow<Int>
        get() = _showValue

    // nBack is currently hardcoded
    override val nBack: Int = 2

    private var job: Job? = null  // coroutine job for the game event
    private val eventInterval: Long = 2000L  // 2000 ms (2s)

    private val nBackHelper = NBackHelper()  // Helper that generate the event array
    private var events = emptyArray<Int>()  // Array with all events

    private var gotPoint=false







    override fun setGameType(gameType: GameType) {
        // update the gametype in the gamestate
        _gameState.value = _gameState.value.copy(gameType = gameType)
    }

    override fun startGame() {
        job?.cancel()  // Cancel any existing game loop
        Log.d("GameVM", "Game starts")
        audioPlayer = AudioPlayer(context)
        audioPlayer.init()


        // Get the events from our C-model (returns IntArray, so we need to convert to Array<Int>)
        events = nBackHelper.generateNBackString(10, 9, 30, nBack).toList().toTypedArray()  // Todo Higher Grade: currently the size etc. are hardcoded, make these based on user input
        Log.d("GameVM", "The following sequence was generated: ${events.contentToString()}")


        job = viewModelScope.launch {
            when (gameState.value.gameType) {
                GameType.Audio -> runAudioGame()
                GameType.AudioVisual -> runAudioVisualGame()
                GameType.Visual -> runVisualGame(events)

            }

            // Todo: update the highscore
            updateHighScore(_highscore.value)
        }
    }

    override fun checkMatch(int: Int) {
        //var lastEventValue = int

        //for checking the condition correctly you need to get the current event index displaying??
        // make a function that returns the index
        if (_btnState.value and (events.get(showValue.value).equals(events.get(showValue.value-2)) )){ // and (events.get(int).equals(events.get(int)) )
            _score.value +=1
            gotPoint=true
            Log.d("GameVM", "New score"+_score.value)
        }
    }


    suspend fun updateHighScore(newScore: Int) {
        if (newScore > _highscore.value) {
            _highscore.value = newScore
            Log.d("GameVM", "Highscore updated "+_highscore.value)
            userPreferencesRepository.saveHighScore(_highscore.value)
        }
    }

    override fun btn_press() {
        _btnState.compareAndSet(false,true)
        Log.d("GameVM", "Button pressed "+_btnState.value)
        checkMatch(1)
        _btnState.compareAndSet(true,false)

    }





    private fun runAudioGame() {
        // Todo: Make work for Basic grade
    }

    private suspend fun runVisualGame(events: Array<Int>){
        // Todo: Replace this code for actual game code
        for (value in events) {
            _showValue.value=value
            _gameState.value = _gameState.value.copy(eventValue = value)
            gotPoint=false
            //Log.d("GameVM.runVisualGame loop", "Check match")
            //checkMatch(value)

            delay(eventInterval)
        }

        //upd

    }

    private fun runAudioVisualGame(){
        // Todo: Make work for Higher grade
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GameApplication)
               //val gameap=GameApplication()
                GameVM(application,application.userPreferencesRespository)
            }
        }
    }

    init {
        // Code that runs during creation of the vm
        viewModelScope.launch {
            userPreferencesRepository.highscore.collect {
                _highscore.value = it
            }
        }
    }
}

// Class with the different game types
enum class GameType{
    Audio,
    Visual,
    AudioVisual
}

data class GameState(
    // You can use this state to push values from the VM to your UI.
    val gameType: GameType = GameType.Visual,  // Type of the game
    val eventValue: Int = -1  // The value of the array string
)





class FakeVM: GameViewModel{
    override val gameState: StateFlow<GameState>
        get() = MutableStateFlow(GameState()).asStateFlow()
    override val score: StateFlow<Int>
        get() = MutableStateFlow(2).asStateFlow()
    override val highscore: StateFlow<Int>
        get() = MutableStateFlow(42).asStateFlow()
    override val nBack: Int
        get() = 2
    override val btnState: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    override val showValue: StateFlow<Int>
        get() = TODO("Not yet implemented")


    override fun setGameType(gameType: GameType) {
    }

    override fun startGame() {
    }

    override fun checkMatch(int: Int) {
    }

    override fun btn_press() {
        TODO("Not yet implemented")
    }
}