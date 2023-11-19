package mobappdev.example.nback_cimpl.ui.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.ui.viewmodels.FakeVM
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

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SettingsScreen(
    vm: GameViewModel,navController: NavController
) {
    val highscore by vm.highscore.collectAsState()  // Highscore is its own StateFlow
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    //var selectedGameType = GameType.Visual

    val gameTypeIndex by vm.gameTypeIndex.collectAsState()
    val currentGameType = vm.gameTypeArray[gameTypeIndex]
    val eventTime by vm.eventInterval.collectAsState()
    val eventLen by vm.event_length.collectAsState()
    val nback by vm.nBack.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE



    if (isLandscape) {

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    Column(
                        modifier = Modifier//.background(Color.Blue)
                            .width(100.dp)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //Left Column



                        Button(onClick = {
                            vm.nback_plus()
                            //selectedGameType= gametypeArray[numb]
                        },
                            colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                            modifier = Modifier
                                .bounceClick()
                                .padding(5.dp)
                                .weight(1f)
                                .aspectRatio(1f) // This makes the Box a square


                            ) {
                            Text(text = "+" )
                        }


                        Button(onClick = {
                            vm.incrementGameType()

                        },
                            colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                            modifier = Modifier
                                .bounceClick()
                                .padding(5.dp)
                                .weight(1f)
                                 // Padding for each box, adjust as needed
                                .aspectRatio(1f) // This makes the Box a square

                            ) {
                            Text(text = "+" )
                        }





                        Button(onClick = {vm.eventInterval_plus()},
                            colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                            modifier = Modifier
                                .bounceClick()
                                .weight(1f)
                                .padding(5.dp) // Padding for each box, adjust as needed
                                .aspectRatio(1f) // This makes the Box a square



                            ) {
                            Text(text = "+" )
                            }


                        Button(onClick = {vm.event_length_plus()},
                            colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                            modifier = Modifier
                                .bounceClick()
                                .weight(1f)
                                .padding(5.dp) // Padding for each box, adjust as needed
                                .aspectRatio(1f) // This makes the Box a square



                        ) {
                            Text(text = "+")
                        }





                    }


                    Column(
                        modifier = Modifier
                            //.background(Color.Red)
                            .width(200.dp)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //Middle Column
                        Text(text = "N-Back : $nback",Modifier.padding(10.dp)) /// maybe something with the len of the string

                        Text(text = "Mode : $currentGameType",Modifier.padding(10.dp)) /// maybe something with the len of the string

                        Text(text = "Event time: "+ (eventTime/1000).toString()+"s",Modifier.padding(10.dp) )

                        Text(text = "Event Length: "+ eventLen,Modifier.padding(10.dp))


                        Button(modifier = Modifier.bounceClick(),
                            colors = ButtonDefaults.buttonColors(Color(0xFF007AFF)),
                            onClick = {

                                vm.setGameType(currentGameType)
                                scope.launch {
                                    snackBarHostState.showSnackbar(message = "Settings Saved!!!")
                                    //navController.navigate(MainDestinations.HOME_ROUTE)
                                }
                                Log.d("GameVM", "Values saved: ${vm.event_length.value}")
                                Log.d("GameVM", "Values saved: ${vm.eventInterval.value}")
                                Log.d("GameVM", "Values saved: ${vm.gameState.value}")
                                navController.popBackStack()
                            }
                        ) {
                            Text(text = "Save")
                        }




                    }
                    Column(
                        modifier = Modifier
                            //.background(Color.Blue)
                            .width(100.dp)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //Right Column
                        Button(onClick = {
                            vm.nback_minus()
                            //selectedGameType= gametypeArray[numb]
                        },
                            colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                            modifier = Modifier
                                .bounceClick()
                                .padding(5.dp)
                                .weight(1f)
                                .aspectRatio(1f) // This makes the Box a square

                                )
                        {
                            Text(text = "-", Modifier.padding(10.dp))
                        }


                        Button(onClick = {
                            vm.decrementGameType()
                        },
                            colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                            modifier = Modifier
                                .bounceClick()
                                .padding(5.dp)
                                .weight(1f)
                                .aspectRatio(1f) // This makes the Box a square

                        )
                        {
                            Text(text = "-")
                        }

                        Button(onClick = {vm.eventInterval_minus()},
                            colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                            modifier = Modifier
                                .bounceClick()
                                .weight(1f)
                                .padding(5.dp) // Padding for each box, adjust as needed
                                .aspectRatio(1f) // This makes the Box a square

                        )
                        {
                            Text(text = "-")
                        }





                        Button(onClick = {vm.event_length_minus()},
                            colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                            modifier = Modifier
                                .bounceClick()
                                .weight(1f)
                                .padding(5.dp)
                                .aspectRatio(1f)

                              )
                        {
                            Text(text = "-")
                        }





                    }
                }
            }
        }

    }else{
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
                    .padding(10.dp)
                    .weight(1f)
                ,
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {


                Button(onClick = {
                    vm.nback_plus()
                    //selectedGameType= gametypeArray[numb]
                },
                    colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                    modifier = Modifier
                        .bounceClick()
                        //.weight(1f)
                        .padding(35.dp) // Padding for each box, adjust as needed
                        .aspectRatio(1f) // This makes the Box a square
                        .shadow(
                            4.dp,
                            RoundedCornerShape(55.dp)
                        ) // This adds a shadow with rounded corners
                        .background(Color(0xFF87CEEB), RoundedCornerShape(55.dp)),

                    ) {
                    Text(text = "+", fontSize = 12.sp, )
                }


                Text(text = "N-Back : $nback   ") /// maybe something with the len of the string

                Button(onClick = {
                    vm.nback_minus()
                    //selectedGameType= gametypeArray[numb]
                },
                    colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                    modifier = Modifier
                        .bounceClick()
                        //.weight(1f)
                        .padding(35.dp) // Padding for each box, adjust as needed
                        .aspectRatio(1f) // This makes the Box a square
                        .shadow(
                            4.dp,
                            RoundedCornerShape(55.dp)
                        ) // This adds a shadow with rounded corners
                        .background(Color(0xFF87CEEB), RoundedCornerShape(55.dp)))
                {
                    Text(text = "-", fontSize = 12.sp,)
                }
            }



            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .weight(1f)

                ,
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {


                Button(onClick = {
                    vm.incrementGameType()

                },
                    colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                    modifier = Modifier
                        .bounceClick()
                        //.weight(1f)
                        .padding(35.dp) // Padding for each box, adjust as needed
                        .aspectRatio(1f) // This makes the Box a square
                        .shadow(
                            4.dp,
                            RoundedCornerShape(55.dp)
                        ) // This adds a shadow with rounded corners
                        .background(Color(0xFF87CEEB), RoundedCornerShape(55.dp)),

                    ) {
                    Text(text = "+" , fontSize = 12.sp,)
                }


                Text(text = "Mode : $currentGameType   ") /// maybe something with the len of the string

                Button(onClick = {
                    vm.decrementGameType()
                },
                    colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                    modifier = Modifier
                        .bounceClick()
                        //.weight(1f)
                        .padding(35.dp) // Padding for each box, adjust as needed
                        .aspectRatio(1f) // This makes the Box a square
                        .shadow(
                            4.dp,
                            RoundedCornerShape(55.dp)
                        ) // This adds a shadow with rounded corners
                        .background(Color(0xFF87CEEB), RoundedCornerShape(55.dp)))
                {
                    Text(text = "-", fontSize = 12.sp,)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {


                Button(onClick = {vm.eventInterval_plus()},
                    colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                    modifier = Modifier
                        .bounceClick()
                        //.weight(1f)
                        .padding(35.dp) // Padding for each box, adjust as needed
                        .aspectRatio(1f) // This makes the Box a square
                        .shadow(
                            4.dp,
                            RoundedCornerShape(55.dp)
                        ) // This adds a shadow with rounded corners
                        .background(Color(0xFF87CEEB), RoundedCornerShape(55.dp)),

                    ) {
                    Text(text = "+" , fontSize = 12.sp,)
                }


                Text(text = "Event time: "+ (eventTime/1000).toString()+"s" )

                Button(onClick = {vm.eventInterval_minus()},
                    colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                    modifier = Modifier
                        .bounceClick()
                        //.weight(1f)
                        .padding(35.dp) // Padding for each box, adjust as needed
                        .aspectRatio(1f) // This makes the Box a square
                        .shadow(
                            4.dp,
                            RoundedCornerShape(55.dp)
                        ) // This adds a shadow with rounded corners
                        .background(Color(0xFF87CEEB), RoundedCornerShape(55.dp)))
                {
                    Text(text = "-", fontSize = 12.sp,)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {


                Button(onClick = {vm.event_length_plus()},
                    colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                    modifier = Modifier
                        .bounceClick()
                        //.weight(1f)
                        .padding(35.dp) // Padding for each box, adjust as needed
                        .aspectRatio(1f) // This makes the Box a square
                        .shadow(
                            4.dp,
                            RoundedCornerShape(55.dp)
                        ) // This adds a shadow with rounded corners
                        .background(Color(0xFF87CEEB), RoundedCornerShape(55.dp)),

                    ) {
                    Text(text = "+", fontSize = 12.sp, )
                }


                Text(text = "Event Length: "+ eventLen)

                Button(onClick = {vm.event_length_minus()},
                    colors = ButtonDefaults.buttonColors(Color(0xFF87CEEB)),
                    modifier = Modifier
                        .bounceClick()
                        //.weight(1f)
                        .padding(35.dp) // Padding for each box, adjust as needed
                        .aspectRatio(1f) // This makes the Box a square
                        .shadow(
                            4.dp,
                            RoundedCornerShape(55.dp)
                        ) // This adds a shadow with rounded corners
                        .background(Color(0xFF87CEEB), RoundedCornerShape(55.dp)))
                {
                    Text(text = "-")
                }
            }






            Row(
                modifier = Modifier
                    .weight(1f)
                    //.fillMaxWidth()
                    .padding(10.dp),

                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {


                Button(modifier = Modifier.bounceClick(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF007AFF)),
                    onClick = {

                        vm.setGameType(currentGameType)
                        scope.launch {
                            snackBarHostState.showSnackbar(message = "Settings Saved!!!")
                            //navController.navigate(MainDestinations.HOME_ROUTE)
                        }
                        Log.d("GameVM", "Values saved: ${vm.event_length.value}")
                        Log.d("GameVM", "Values saved: ${vm.eventInterval.value}")
                        Log.d("GameVM", "Values saved: ${vm.gameState.value}")
                        navController.popBackStack()
                    }
                ) {
                    Text(text = "Save")
                }







            }
        }
    }}


}

@Preview
@Composable
fun SettingsScreenPreview() {
    // Since I am injecting a VM into my homescreen that depends on Application context, the preview doesn't work.
    Surface(){
        SettingsScreen(FakeVM(), navController = rememberNavController())
    }
}