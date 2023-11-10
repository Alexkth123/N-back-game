package mobappdev.example.nback_cimpl.ui.screens

import android.graphics.fonts.Font
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.R
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
import mobappdev.example.nback_cimpl.ui.viewmodels.GameType
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel

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
fun PlayScreen(
    vm: GameViewModel

) {
    val boxvalue = 0
    val highscore by vm.highscore.collectAsState()  // Highscore is its own StateFlow
    val gameState by vm.gameState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    vm.setGameType(GameType.Visual)
    vm.startGame()

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
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                text = "N = 2 :${gameState.eventValue}" ,
                color = Color.LightGray,
                fontSize = 44.sp,
                textAlign = TextAlign.Center,



            )
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
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp) // Padding for each box, adjust as needed
                                        .aspectRatio(1f) // This makes the Box a square
                                        .shadow(4.dp, RoundedCornerShape(10.dp)) // This adds a shadow with rounded corners
                                        .background(Color(0xFF87CEEB), RoundedCornerShape(10.dp))
                                ) {
                                    Button(
                                        onClick = { /* TODO: Implement your onClick action here */
                                                  },
                                        colors = ButtonDefaults.buttonColors( Color.Transparent),
                                        elevation = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(),
                                        shape = RoundedCornerShape(10.dp) // This applies the rounded corners to the button itself
                                    ) {
                                        // The button content goes here
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
                        .weight(1f)
                        .padding(30.dp)
                        .aspectRatio(1f) // This makes the Box a square
                        .shadow(4.dp, RoundedCornerShape(10.dp)) // This adds a shadow with rounded corners
                        .background(Color(0xFF007AFF), RoundedCornerShape(30.dp)) // This sets the rounded corner background to white
                ) {
                    Button(
                        onClick = { /* TODO: Implement your onClick action here */ },
                        colors = ButtonDefaults.buttonColors( Color.Transparent),
                        elevation = null,
                        modifier = Modifier
                            .fillMaxSize(),
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
                        .weight(1f)
                        .padding(30.dp)
                        .aspectRatio(1f) // This makes the Box a square
                        .shadow(4.dp, RoundedCornerShape(10.dp)) // This adds a shadow with rounded corners
                        .background(Color(0xFF007AFF), RoundedCornerShape(30.dp)) // This sets the rounded corner background to white
                ) {
                    Button(
                        onClick = { /* TODO: Implement your onClick action here */ },
                        colors = ButtonDefaults.buttonColors( Color.Transparent),
                        elevation = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        shape = RoundedCornerShape(10.dp) // This applies the rounded corners to the button itself
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.visual),
                            contentDescription = "Sound",
                            modifier = Modifier.size(48.dp)
                        )


                    }
                }
            }
        }
    }


}

@Preview
@Composable
fun PlayScreenPreview() {
    // Since I am injecting a VM into my homescreen that depends on Application context, the preview doesn't work.
    Surface(){
        PlayScreen(FakeVM())
    }
}



