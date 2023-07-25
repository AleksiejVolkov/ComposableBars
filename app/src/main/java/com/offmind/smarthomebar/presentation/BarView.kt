package com.offmind.tutorial.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SeparateBarView(
    modifier: Modifier = Modifier,
    title: String = "",
    currentValue: Int = 0,
    maxValue: Int = 5,
    isEnabled: Boolean = true,
    animationDuration: Int = 700,
    cornerRadius: Dp = 15.dp,
    barIndicator: (@Composable() () -> Unit)? = null
) {
    if (currentValue > maxValue) {
        throw Exception("Invalid current value parameter!!!")
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            SeparatedBar(
                maxValue = maxValue,
                currentValue = currentValue,
                activeColors = listOf(Color(0xFF7A8A91), Color(0xFF6B7B82)),
                backgroundColor = Color(0xFF34484F),
                isEnabled = isEnabled,
                animationDuration = animationDuration,
                shape = RoundedCornerShape(
                    topEnd = cornerRadius,
                    topStart = cornerRadius,
                    bottomStart = if (barIndicator != null) 0.dp else cornerRadius,
                    bottomEnd = if (barIndicator != null) 0.dp else cornerRadius,
                )
            )
        }

        barIndicator?.invoke()

        Text(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            text = title,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 9.sp
        )
    }
}

@Composable
fun SolidBarView(
    modifier: Modifier = Modifier,
    title: String = "",
    currentValue: Float = 0f,
    minValue: Int = 0,
    maxValue: Int = 1,
    isEnabled: Boolean = true,
    animationDuration: Int = 700,
    cornerRadius: Dp = 15.dp,
    barLabelArrangement: Arrangement.Vertical,
    barIndicator: (@Composable () -> Unit)? = null,
    barLabel: @Composable (animationProgress: Float) -> Unit = {},
) {

    if (maxValue <= minValue) {
        throw Exception("Invalid min or max parameters!!!")
    }

    val normalizedValue =
        (currentValue.coerceIn(
            minValue.toFloat(),
            maxValue.toFloat()
        ) - minValue) / (maxValue - minValue)

    val animatedValue: Float by animateFloatAsState(
        if (isEnabled) normalizedValue else 0f,
        label = "ValueAnim",
        animationSpec = tween(durationMillis = animationDuration)
    )

    val animatedAlpha: Float by animateFloatAsState(
        if (isEnabled) 1f else 0f,
        label = "AlphaAnim",
        animationSpec = tween(durationMillis = animationDuration)
    )

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            SolidBarIndicator(
                currentValue = animatedValue,
                modifier = Modifier.fillMaxSize(),
                activeColors = listOf(
                    Color(0xFF7A8A91),
                    Color(0xFF6B7B82)
                ).map { it.copy(alpha = animatedAlpha) },
                backgroundColor = Color(0xFF35484F),
                labelArrangement = barLabelArrangement,
                shape = RoundedCornerShape(
                    topEnd = cornerRadius,
                    topStart = cornerRadius,
                    bottomStart = if (barIndicator != null) 0.dp else cornerRadius,
                    bottomEnd = if (barIndicator != null) 0.dp else cornerRadius,
                )
            ) {
                barLabel(animationProgress = animatedAlpha)
            }
        }

        barIndicator?.invoke()

        Text(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            text = title,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 9.sp
        )
    }
}

@Composable
fun BarIndicator(
    colorActive: Color,
    colorInactive: Color,
    isEnabled: Boolean,
    iconRes: Int,
    animationDuration: Int
) {
    val animatedAngle: Float by animateFloatAsState(
        if (isEnabled) 90f else 0f,
        label = "AngleAnim",
        animationSpec = tween(durationMillis = animationDuration)
    )

    val animatedAlpha: Float by animateFloatAsState(
        if (isEnabled) 1f else 0f,
        label = "AlphaAnim",
        animationSpec = tween(durationMillis = animationDuration)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
            .background(color = Color(0xFF34484E))
            .padding(3.dp)
            .background(
                color = lerp(colorInactive, colorActive, animatedAlpha),
                shape = RoundedCornerShape(
                    topStart = 3.dp,
                    topEnd = 3.dp,
                    bottomStart = 12.dp,
                    bottomEnd = 12.dp
                )
            )
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier
                .size(15.dp)
                .rotate(animatedAngle),
            painter = painterResource(id = iconRes),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            modifier = Modifier.width(20.dp),
            text = if (isEnabled) "ON" else "OFF",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 8.sp
        )
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Composable
private fun SolidBarIndicator(
    modifier: Modifier,
    activeColors: List<Color>,
    backgroundColor: Color,
    currentValue: Float,
    shape: Shape,
    labelArrangement: Arrangement.Vertical,
    label: @Composable() () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(shape = shape)
            .background(color = backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(currentValue)
                    .fillMaxWidth()
                    .background(brush = Brush.horizontalGradient(activeColors))
            )
        }
    }
    Column(
        modifier = Modifier
            .padding(vertical = 20.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = labelArrangement
    ) {
        label()
    }
}


@Composable
private fun SeparatedBar(
    maxValue: Int,
    currentValue: Int,
    activeColors: List<Color>,
    backgroundColor: Color,
    isEnabled: Boolean,
    animationDuration: Int,
    shape: Shape
) {
    val animatedAlpha: Float by animateFloatAsState(
        if (isEnabled) 1f else 0f,
        label = "AlphaAnim",
        animationSpec = tween(durationMillis = animationDuration)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
    ) {
        repeat(maxValue) {
            val colorList =
                activeColors.map { color ->
                    lerp(
                        backgroundColor,
                        color,
                        if (maxValue - it <= currentValue) animatedAlpha else 0f
                    )
                }
            Box(
                modifier = Modifier
                    .background(brush = Brush.horizontalGradient(colorList))
                    .fillMaxWidth()
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(1.dp))
        }
    }
}

