package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
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
fun SettingsScreen(
    vm: GameViewModel,navController: NavController
) {
    val highscore by vm.highscore.collectAsState()  // Highscore is its own StateFlow
    val gameState by vm.gameState.collectAsState()
    //val event_time by vm..collectAsState()  // Highscore is its own StateFlow
    //val event_length by vm.gameState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val gametypeArray = arrayOf(GameType.Visual, GameType.Audio, GameType.AudioVisual)
    var selectedGameType = GameType.Visual

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
                modifier = Modifier.padding(32.dp),
                text = "Game Settings",
                style = MaterialTheme.typography.headlineLarge
            )
            // Todo: You'll probably want to change this "BOX" part of the composable



            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                var numb = 0
                Button(onClick = {
                    if (numb<2){numb+=1}
                    selectedGameType= gametypeArray[numb]
                },
                    colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                    modifier = Modifier
                        .bounceClick()
                        .weight(1f)
                        .padding(35.dp) // Padding for each box, adjust as needed
                        .aspectRatio(1f) // This makes the Box a square
                        .shadow(4.dp, RoundedCornerShape(55.dp)) // This adds a shadow with rounded corners
                        .background(Color(0xFF87CEEB), RoundedCornerShape(55.dp)),

                    ) {
                    Text(text = "+" )
                }


                Text(text = "${selectedGameType.toString()}") /// why is it not updating value

                Button(onClick = {
                    if (numb>0){numb-=1}
                    selectedGameType= gametypeArray[numb]
                },
                    colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                    modifier = Modifier
                        .bounceClick()
                        .weight(1f)
                        .padding(35.dp) // Padding for each box, adjust as needed
                        .aspectRatio(1f) // This makes the Box a square
                        .shadow(4.dp, RoundedCornerShape(55.dp)) // This adds a shadow with rounded corners
                        .background(Color(0xFF87CEEB), RoundedCornerShape(55.dp)))
                {
                    Text(text = "-")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {


                   Button(onClick = {},
                       colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                          modifier = Modifier
                              .bounceClick()
                              .weight(1f)
                              .padding(35.dp) // Padding for each box, adjust as needed
                              .aspectRatio(1f) // This makes the Box a square
                              .shadow(4.dp, RoundedCornerShape(55.dp)) // This adds a shadow with rounded corners
                              .background(Color(0xFF87CEEB), RoundedCornerShape(55.dp)),

                         ) {
                       Text(text = "+" )
                   }


                    Text(text = "Event time: xS")

                    Button(onClick = {},
                        colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                        modifier = Modifier
                            .bounceClick()
                            .weight(1f)
                            .padding(35.dp) // Padding for each box, adjust as needed
                            .aspectRatio(1f) // This makes the Box a square
                            .shadow(4.dp, RoundedCornerShape(55.dp)) // This adds a shadow with rounded corners
                            .background(Color(0xFF87CEEB), RoundedCornerShape(55.dp)))
                    {
                        Text(text = "-")
                    }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {


                Button(onClick = {},
                    colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                    modifier = Modifier
                        .bounceClick()
                        .weight(1f)
                        .padding(35.dp) // Padding for each box, adjust as needed
                        .aspectRatio(1f) // This makes the Box a square
                        .shadow(4.dp, RoundedCornerShape(55.dp)) // This adds a shadow with rounded corners
                        .background(Color(0xFF87CEEB), RoundedCornerShape(55.dp)),

                    ) {
                    Text(text = "+" )
                }


                Text(text = "Event Length: x")

                Button(onClick = {},
                    colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                    modifier = Modifier
                        .bounceClick()
                        .weight(1f)
                        .padding(35.dp) // Padding for each box, adjust as needed
                        .aspectRatio(1f) // This makes the Box a square
                        .shadow(4.dp, RoundedCornerShape(55.dp)) // This adds a shadow with rounded corners
                        .background(Color(0xFF87CEEB), RoundedCornerShape(55.dp)))
                {
                    Text(text = "-")
                }
            }






            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {


                Button(modifier = Modifier.bounceClick(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF007AFF)),
                    onClick = {

                        vm.setGameType(selectedGameType)
                    scope.launch {
                        snackBarHostState.showSnackbar(message = "Settings Saved!!!")
                        //navController.navigate(MainDestinations.HOME_ROUTE)

                    }
                        navController.popBackStack()
                    }
                ) {
                    Text(text = "Save")
                }







            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    // Since I am injecting a VM into my homescreen that depends on Application context, the preview doesn't work.
    Surface(){
        SettingsScreen(FakeVM(), navController = rememberNavController())
    }
}