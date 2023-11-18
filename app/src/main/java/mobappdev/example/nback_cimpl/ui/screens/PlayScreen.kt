package mobappdev.example.nback_cimpl.ui.screens

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mobappdev.example.nback_cimpl.R
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.MainDestinations
import mobappdev.example.nback_cimpl.ui.viewmodels.GameState

/**
 * This is the Home screen composable
 *
 * Currently this screen shows the saved highscore
 * It also contains a button which can be used to show that the C-integration works
 * Furthermore it contains two buttons that you can use to start a game
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */

@Composable
fun PlayScreen(vm: GameViewModel,navController: NavController) {
    val boxvalue = 0
    val highscore by vm.highscore.collectAsState()  // Highscore is its own StateFlow
    val gameState by vm.gameState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 1. Define the Animatable for the flashing color.
    //val flashingColor = remember { animateColorAsState() } // Default color

    // 2. Create a state to track which button should flash. This could be an index or a value.
    val buttonToFlash = remember { mutableStateOf(-1) } // -1 means no button should flash







    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

         //   Row () {


                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    text = "Score :${vm.score.value}",
                    color = Color.LightGray,
                    fontSize = 44.sp,
                    textAlign = TextAlign.Center,
                )

                Button(
                    modifier = Modifier
                        .bounceClick()
                        .padding(end = 10.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF007AFF)),
                    onClick = { navController.popBackStack()
                              },
                )
                {
                    Text(text = "Back")
                }
         //   }

                Row (){

                   if( ProgressBar(eventValue =vm.showValue.value , arraySize = vm.event_length.value)){
                       //navController.popBackStack()
                       vm.set_gamefinished(true)
                      // navController.navigate(MainDestinations.HOME_ROUTE)
                   }
                }
            // Todo: You'll probably want to change this "BOX" part of the composable
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp), // Padding for the entire screen
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Spacer(modifier = Modifier.height(32.dp)) // Adds a space between the text and the grid



                    // Create 3x3 grid of buttons
                    for (i in 0 until 3) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (j in 0 until 3) {

                                val buttonValue = i * 3 + j + 1 // Assign a value from 1 to 9
                                // 3. Set up the flashing effect based on the gameState.


                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp) // Padding for each box, adjust as needed
                                        .aspectRatio(1f) // This makes the Box a square
                                        .shadow(
                                            4.dp,
                                            RoundedCornerShape(10.dp)
                                        ) // This adds a shadow with rounded corners
                                        .background(Color(0xFF87CEEB), RoundedCornerShape(10.dp))
                                        .buttonflash(gameState, buttonValue)
                                ) {
                                    Button(
                                        onClick = {
                                                  /* TODO: Implement your onClick action here */
                                        },
                                        colors = ButtonDefaults.buttonColors(Color(buttonValue)),
                                        elevation = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            //.bounceClick()
                                            .padding(),
                                        shape = RoundedCornerShape(10.dp) // This applies the rounded corners to the button itself
                                    ) {
                                        // The button content goes here
                                        Text(text = ""+buttonValue)
                                    }
                                }
                            }
                        }
                        if (i < 2) { // Add spacing between rows, except after the last one
                            Spacer(modifier = Modifier.height(16.dp))
                        }


                    }
                }
            }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .bounceClick()
                                .weight(1f)
                                .padding(30.dp)
                                .aspectRatio(1f) // This makes the Box a square
                                .shadow(
                                    4.dp,
                                    RoundedCornerShape(30.dp)
                                ) // This adds a shadow with rounded corners
                                .background(
                                    Color(0xFF007AFF),
                                    RoundedCornerShape(30.dp)
                                ) // This sets the rounded corner background to white
                        ) {
                            Button(
                                onClick = { vm.btn_press()
                                          vm.btnAudio_press()},
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                elevation = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .bounceClick(),
                                shape = RoundedCornerShape(10.dp) // This applies the rounded corners to the button itself
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.sound_on),
                                    contentDescription = "Sound",
                                    modifier = Modifier
                                        .height(48.dp)
                                        .aspectRatio(3f / 2f)
                                )
                            }
                        }


                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .bounceClick()
                            .weight(1f)
                            .padding(30.dp)
                            .aspectRatio(1f) // This makes the Box a square
                            .shadow(
                                4.dp,
                                RoundedCornerShape(30.dp)
                            ) // This adds a shadow with rounded corners
                            .background(
                                Color(0xFF007AFF), RoundedCornerShape(30.dp)
                            ) // This sets the rounded corner background to white
                    ) {

                            Button(
                                onClick = {
                                vm.btn_press()},
                                //vm.checkMatch(1)

                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                elevation = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp)
                                    .bounceClick(),
                                shape = RoundedCornerShape(10.dp) // This applies the rounded corners to the button itself
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.visual),
                                    contentDescription = "visual",
                                    modifier = Modifier.size(48.dp)
                                )


                        }
                    }
                }
            }
        }
    }






enum class ButtonState { Pressed, Idle }
fun Modifier.bounceClick() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0.85f else 1f)

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}


@Composable
fun Modifier.buttonflash( gameState: GameState,buttonValue: Int) = composed {
    //var Buttonflash by remember { mutableStateOf(buttonflash.Idle) }
    val flashingColor = remember { Animatable(Color(0xFF87CEEB)) }

if( gameState.gameType==GameType.Visual || gameState.gameType==GameType.AudioVisual){
    LaunchedEffect(key1 = gameState.eventValue, key2 = buttonValue) {
        if (gameState.eventValue == buttonValue ) {
            // Log is not available in Compose, but this represents your logging logic
            Log.d("GameVM.", "Blink box $buttonValue")
            // Animate to the flashing color
            flashingColor.animateTo(targetValue = Color(0xFFE67E22),
                                    animationSpec = TweenSpec(durationMillis = 300))

            kotlinx.coroutines.delay(300) // The same duration to display the orange color
            flashingColor.animateTo(
                targetValue = Color(0xFF87CEEB),
                animationSpec = TweenSpec(durationMillis = 400) // Shorter duration for the animation
            )

        } else {
            // Revert to default color if not the correct button
            flashingColor.animateTo(targetValue = Color(0xFF87CEEB))
        }
    }
}
    // 2. Apply the animated color to the background of the Modifier
    this.background(flashingColor.value)

}

@Composable
fun ProgressBar(eventValue: Int, arraySize: Int):Boolean {
    val progress = eventValue.toFloat() / arraySize.toFloat()

    LinearProgressIndicator(
        progress = progress,
        color = Color.Blue,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        trackColor = Color.Gray
    )
    if(progress==(1).toFloat()){return true} else {return false}


}






@Preview
@Composable
fun PlayScreenPreview() {
    // Since I am injecting a VM into my homescreen that depends on Application context, the preview doesn't work.
    Surface(){
        PlayScreen(FakeVM(),navController = rememberNavController())
    }
}



