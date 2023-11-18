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
    val nBack: StateFlow<Int>
    val btnState: StateFlow<Boolean>
    val btnAudioState: StateFlow<Boolean>
    val showValue: StateFlow<Int>


    val eventInterval:StateFlow<Long>
    val event_length:StateFlow<Int>
    val gamestate_selection:StateFlow<Int>


    val gameTypeIndex: StateFlow<Int>
    val gameTypeArray: Array<GameType>


    fun incrementGameType()
    fun decrementGameType()
    fun getCurrentGameType(): GameType
    fun  get_gamefinished(): Boolean
    fun set_gamefinished(value: Boolean):Boolean

    fun setGameType(gameType: GameType)
    fun startGame()

    fun checkMatch(int: Int)
    fun checkAudioMatch(int: Int)
    fun btn_press()
    fun btnAudio_press()
    fun gamestate_selection_plus()
    fun gamestate_selection_minus()
    fun eventInterval_plus()
    fun eventInterval_minus()
    fun event_length_plus()
    fun event_length_minus()

    fun nback_plus()
    fun nback_minus()

    fun onGameFinishedSave()



}

class GameVM(
    application: Application,
    private val userPreferencesRepository: UserPreferencesRepository
): GameViewModel, ViewModel() {

    private lateinit var repository:UserPreferencesRepository
    private val context = application.applicationContext
    private lateinit var audioPlayer: AudioPlayer


    private val _gameState = MutableStateFlow(GameState())
    override val gameState: StateFlow<GameState>
        get() = _gameState


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

    private val _btnAudioState = MutableStateFlow(false)
    override val btnAudioState: StateFlow<Boolean>
        get() = _btnAudioState


    private val _showValue = MutableStateFlow(0)
    override val showValue: StateFlow<Int>
        get() = _showValue





    private var job: Job? = null  // coroutine job for the game event

    //private val eventInterval: Long = 2000L  // 2000 ms (2s)
    // nBack is currently hardcoded
    private val _nBack = MutableStateFlow(2)
    override val nBack: StateFlow<Int>
        get() =_nBack

    private val nBackHelper = NBackHelper()  // Helper that generate the event array
    private var events = emptyArray<Int>()  // Array with all events
    private var events_audio = emptyArray<Int>()


    private var gotPoint=false
    private var game_finished = MutableStateFlow(false)


    /// Here is the varibles for the settings menu
    private val _gameTypeIndex = MutableStateFlow(0)
    override val gameTypeIndex: StateFlow<Int> = _gameTypeIndex.asStateFlow()
    override val gameTypeArray = arrayOf(GameType.Visual, GameType.Audio, GameType.AudioVisual)

    private val _eventInterval = MutableStateFlow( 2000L)
    override val eventInterval: StateFlow<Long>
        get() = _eventInterval

    private val _event_length = MutableStateFlow(10)
    override val event_length: StateFlow<Int>
        get() = _event_length
    private val _gamestate_selection = MutableStateFlow(0)
    override val gamestate_selection: StateFlow<Int>
        get() = _gamestate_selection



    override fun incrementGameType() {
        _gameTypeIndex.value = (_gameTypeIndex.value + 1) % gameTypeArray.size
    }

    override fun decrementGameType() {
        _gameTypeIndex.value = (_gameTypeIndex.value - 1 + gameTypeArray.size) % gameTypeArray.size
    }

    override fun getCurrentGameType(): GameType {
        return gameTypeArray[_gameTypeIndex.value]
    }

     override fun  get_gamefinished(): Boolean {
        return game_finished.value
    }

     override fun set_gamefinished(value: Boolean): Boolean {
        game_finished.value = value
        return game_finished.value
    }




    override fun setGameType(gameType: GameType) {
        // update the gametype in the gamestate
        _gameState.value = _gameState.value.copy(gameType = gameType)
        //_gameState.value.gameType=gameType

    }

    override fun startGame() {
        _showValue.value=0
        set_gamefinished(false)
        job?.cancel()  // Cancel any existing game loop
        Log.d("GameVM", "Game starts")
        audioPlayer = AudioPlayer(context)
        audioPlayer.init()


        // Get the events from our C-model (returns IntArray, so we need to convert to Array<Int>)
        events = nBackHelper.generateNBackString(event_length.value, 9, 30, nBack.value).toList().toTypedArray()  // Todo Higher Grade: currently the size etc. are hardcoded, make these based on user input
        events_audio = nBackHelper.generateNBackString(event_length.value, 9, 30, nBack.value).toList().toTypedArray()
        Log.d("GameVM", "The following sequence was generated: ${events.contentToString()}")


        job = viewModelScope.launch {
            when (gameState.value.gameType) {
                GameType.Audio -> runAudioGame(events_audio)
                GameType.AudioVisual -> runAudioVisualGame(events,events_audio)
                GameType.Visual -> runVisualGame(events)

            }

            // Todo: update the highscore
            updateHighScore(_highscore.value)
            Log.d("GameVM", "highscore Updated ${highscore.value}")
        }
    }

    override fun checkMatch(int: Int) {
        //var lastEventValue = int

        //for checking the condition correctly you need to get the current event index displaying??
        // make a function that returns the index
        // Assuming _btnState is a MutableState<Boolean> and showValue is a MutableState<Int>
        if (_btnState.value && showValue.value >= nBack.value && showValue.value < events.size && events[showValue.value] == events[showValue.value - nBack.value]){
            _score.value += 1
            gotPoint = true
            Log.d("GameVM", "New score: ${_score.value}")
        }

    }

    override fun checkAudioMatch(int: Int) {
        if (_btnState.value && showValue.value >= nBack.value && showValue.value < events_audio.size && events_audio[showValue.value] == events_audio[showValue.value - nBack.value]){
            _score.value += 1
            gotPoint = true
            Log.d("GameVM", "New Audio_score: ${_score.value}")
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

    override fun btnAudio_press() {
        _btnAudioState.compareAndSet(false,true)
        Log.d("GameVM", "Button pressed "+_btnAudioState.value)
        checkAudioMatch(1)
        _btnAudioState.compareAndSet(true,false)

    }




    override fun gamestate_selection_plus() {
        _gamestate_selection.value +=1
    }

    override fun gamestate_selection_minus() {
        _gamestate_selection.value -=1
    }

    override fun eventInterval_plus() {
        _eventInterval.value += 1000L

    }
    override fun eventInterval_minus() {
        _eventInterval.value -= 1000L
    }

    override fun event_length_plus() {
        _event_length.value +=1
    }

    override fun event_length_minus() {
        _event_length.value -=1
    }

    override fun nback_plus() {
        _nBack.value +=1
    }

    override fun nback_minus() {
        if (nBack.value>0){_nBack.value -=1}

    }

    override fun onGameFinishedSave() {

        // Save data when the game is finished
        viewModelScope.launch {
            repository.saveData(this@GameVM)

        }
    }


    private suspend fun runAudioGame(_events_audio: Array<Int>) {
        delay(3000)
        _showValue.value=0
        for (value in events) {
            _showValue.value+=1
            _gameState.value = _gameState.value.copy(eventValue = value)
            gotPoint=false
            Log.d("GameVM", "_showValue.value: ${_showValue.value}")
            //checkMatch(value)

            delay(eventInterval.value)
        }
        set_gamefinished(true) // unused varible
        _showValue.value=0
        updateHighScore(score.value)///
        Log.d("GameVM", "Game finised")
    }

    private suspend fun runVisualGame(events: Array<Int>){
        // Todo: Replace this code for actual game code
        delay(3000)
        _showValue.value=0


        for (value in events) {
            _showValue.value+=1
            _gameState.value = _gameState.value.copy(eventValue = value)
            gotPoint=false
            Log.d("GameVM", "_showValue.value: ${_showValue.value}")

            delay(eventInterval.value)
        }
        set_gamefinished(true) // unused varible
        _showValue.value=0
        updateHighScore(score.value)///
        Log.d("GameVM", "Game finised")


         //Set game finished to false again

    }

    private suspend fun runAudioVisualGame(events: Array<Int>,_events_audio: Array<Int>){
        delay(3000)
        _showValue.value=0


        for (value in events) {
            _showValue.value+=1
            _gameState.value = _gameState.value.copy(eventValue = value)
            gotPoint=false
            Log.d("GameVM", "_showValue.value: ${_showValue.value}")

            delay(eventInterval.value)
        }
        set_gamefinished(true) // unused varible
        _showValue.value=0
        updateHighScore(score.value)///
        Log.d("GameVM", "Game finised")

    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GameApplication)
                GameVM(application,application.userPreferencesRespository)
            }
        }
    }

    init {
        // Code that runs during creation of the vm
        viewModelScope.launch {
            userPreferencesRepository.userPreferences.collect { preferences ->
                _highscore.value = preferences.highScore
                _eventInterval.value = preferences.eventTime
                _event_length.value = preferences.eventLength
                // Update other states as necessary
            }



            /*
                userPreferencesRepository.highscore.collect {
                    _highscore.value = userPreferencesRepository.highscore

                }

                */
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
    override val nBack: StateFlow<Int>
        get() = TODO("Not yet implemented")
    override val btnState: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    override val btnAudioState: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    override val showValue: StateFlow<Int>
        get() = TODO("Not yet implemented")
    override val eventInterval: StateFlow<Long>
        get() = TODO("Not yet implemented")

    override val event_length: StateFlow<Int>
        get() = TODO("Not yet implemented")
    override val gamestate_selection: StateFlow<Int>
        get() = TODO("Not yet implemented")
    override val gameTypeIndex: StateFlow<Int>
        get() = TODO("Not yet implemented")
    override val gameTypeArray: Array<GameType>
        get() = TODO("Not yet implemented")

    override fun incrementGameType() {
        TODO("Not yet implemented")
    }

    override fun decrementGameType() {
        TODO("Not yet implemented")
    }

    override fun getCurrentGameType(): GameType {
        TODO("Not yet implemented")
    }

    override fun get_gamefinished(): Boolean {
        TODO("Not yet implemented")
    }

    override fun set_gamefinished(value: Boolean): Boolean {
        TODO("Not yet implemented")
    }


    override fun setGameType(gameType: GameType) {
    }

    override fun startGame() {
    }

    override fun checkMatch(int: Int) {
    }

    override fun checkAudioMatch(int: Int) {
        TODO("Not yet implemented")
    }

    override fun btn_press() {
        TODO("Not yet implemented")
    }

    override fun btnAudio_press() {
        TODO("Not yet implemented")
    }

    override fun gamestate_selection_plus() {
        TODO("Not yet implemented")
    }

    override fun gamestate_selection_minus() {
        TODO("Not yet implemented")
    }

    override fun eventInterval_plus() {
        TODO("Not yet implemented")
    }

    override fun eventInterval_minus() {
        TODO("Not yet implemented")
    }

    override fun event_length_plus() {
        TODO("Not yet implemented")
    }

    override fun event_length_minus() {
        TODO("Not yet implemented")
    }

    override fun nback_plus() {
        TODO("Not yet implemented")
    }

    override fun nback_minus() {
        TODO("Not yet implemented")
    }

    override fun onGameFinishedSave() {
        TODO("Not yet implemented")
    }
}