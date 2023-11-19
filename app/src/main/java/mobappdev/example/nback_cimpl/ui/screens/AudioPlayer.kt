package mobappdev.example.nback_cimpl.ui.screens

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale


//
class AudioPlayer(context: Context) {  // Passing context needed for TextToSpeech

    private var tts: TextToSpeech = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            init()
        }
    }
    private lateinit var test: String

    fun init(){
        test="\"Stephen hawkings initialized\""
        tts.language= Locale.ENGLISH
        tts.setSpeechRate(1f)
       // tts.speak(test, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun int_to_asci(value:Int):String{
      return ('A'+ value).toString()
    }

    fun play_audio(str:String){
        tts.speak(str, TextToSpeech.QUEUE_FLUSH, null, null)
        Log.d("GameVM.", "audio letter $str")

    }


}



