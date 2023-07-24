package com.offmind.smarthomebar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.offmind.smarthomebar.ui.theme.SmartHomeBarTheme
import com.offmind.tutorial.presentation.BarIndicator
import com.offmind.tutorial.presentation.SeparateBarView
import com.offmind.tutorial.presentation.SolidBarView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHomeBarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ThreeBarsExample()
                }
            }
        }
    }
}

@Composable
fun ThreeBarsExample() {
    var isEnabled by remember { mutableStateOf(false) }
    val animationDuration by remember { mutableStateOf(700) }

    val coolerValue by remember { mutableStateOf(20f) }
    val brightnessValue by remember { mutableStateOf(0.6f) }
    val fanValue by remember { mutableStateOf(2) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SolidBarView(
                modifier = Modifier
                    .width(60.dp)
                    .height(180.dp),
                currentValue = brightnessValue,
                minValue = 0,
                maxValue = 1,
                isEnabled = isEnabled,
                title = "MAIN",
                barLabel = {
                    Image(
                        modifier = Modifier.size(15.dp),
                        painter = painterResource(id = R.drawable.brightness_icon),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = if (isEnabled) "ON" else "OFF",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 9.sp
                    )
                },
                barLabelArrangement = Arrangement.Bottom,
                barIndicator = null
            )

            SeparateBarView(
                modifier = Modifier
                    .width(60.dp)
                    .height(180.dp),
                currentValue = fanValue,
                isEnabled = isEnabled,
                maxValue = 5,
                title = "FAN",
                barIndicator = {
                    BarIndicator(
                        colorActive = Color(0xFF88C17D),
                        colorInactive = Color(0xFF2E4044),
                        isEnabled = isEnabled,
                        iconRes = R.drawable.fan_icon,
                        animationDuration = animationDuration
                    )
                }
            )

            SolidBarView(
                modifier = Modifier
                    .width(60.dp)
                    .height(180.dp),
                currentValue = coolerValue,
                isEnabled = isEnabled,
                minValue = 5,
                maxValue = 30,
                title = "AC",
                barLabel = {
                    Text(
                        text = "+${coolerValue}°",
                        color = Color.White.copy(alpha = it),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                },
                barLabelArrangement = Arrangement.Center,
                barIndicator = {
                    BarIndicator(
                        colorActive = Color(0xFF80C8DB),
                        colorInactive = Color(0xFF2E4044),
                        isEnabled = isEnabled,
                        iconRes = R.drawable.cool_icon,
                        animationDuration = animationDuration
                    )
                }
            )
        }

        Button(onClick = {
            isEnabled = !isEnabled
        }) {
            Text(text = if (isEnabled) "Turn off" else "Turn on")
        }
    }
}

@Preview
@Composable
fun SolidBarPreview() {
    SolidBarView(
        modifier = Modifier
            .width(60.dp)
            .height(180.dp),
        currentValue = 0.6f,
        minValue = 0,
        maxValue = 1,
        isEnabled = true,
        title = "MAIN",
        barLabel = {
            Image(
                modifier = Modifier.size(15.dp),
                painter = painterResource(id = R.drawable.brightness_icon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "ON",
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 9.sp
            )
        },
        barLabelArrangement = Arrangement.Bottom,
        barIndicator = null
    )
}

@Preview
@Composable
fun SeparateBarPreview() {
    SeparateBarView(
        modifier = Modifier
            .width(60.dp)
            .height(180.dp),
        currentValue = 2,
        isEnabled = true,
        maxValue = 4,
        title = "FAN",
        barIndicator = {
            BarIndicator(
                colorActive = Color(0xFF88C17D),
                colorInactive = Color(0xFF2E4044),
                isEnabled = true,
                iconRes = R.drawable.fan_icon,
                animationDuration = 700
            )
        }
    )
}
