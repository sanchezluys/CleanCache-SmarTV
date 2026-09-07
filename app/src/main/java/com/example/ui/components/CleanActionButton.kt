package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CleanGreen
import com.example.ui.theme.CleanGreenBright
import com.example.ui.theme.CleanGreenDark
import com.example.ui.theme.CleanGreenGlow

@Composable
fun CleanActionButton(
    isCleaning: Boolean,
    onCleanClicked: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.04f else 1.0f,
        animationSpec = tween(200),
        label = "btn_scale"
    )

    val backgroundBrush = if (isFocused) {
        Brush.horizontalGradient(
            colors = listOf(CleanGreenBright, CleanGreen, CleanGreenDark)
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(CleanGreen, CleanGreenDark)
        )
    }

    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = if (isFocused) 20.dp else 8.dp,
                shape = shape,
                spotColor = if (isFocused) CleanGreenBright else CleanGreenDark,
                ambientColor = CleanGreenGlow
            )
            .clip(shape)
            .background(backgroundBrush)
            .then(
                if (isFocused) {
                    Modifier.border(3.5.dp, Color.White, shape)
                } else {
                    Modifier.border(1.5.dp, CleanGreenGlow.copy(alpha = 0.5f), shape)
                }
            )
            .focusRequester(focusRequester)
            .focusable(interactionSource = interactionSource, enabled = !isCleaning)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = !isCleaning,
                onClick = onCleanClicked
            )
            .testTag("clean_cache_button")
            .padding(vertical = 22.dp, horizontal = 28.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isCleaning) {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 3.5.dp,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(18.dp))
                Column {
                    Text(
                        text = "LIMPIANDO CACHÉ DEL SISTEMA…",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Solicitando purga de caché residual a Android OS",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black.copy(alpha = 0.75f),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black.copy(alpha = 0.18f))
                ) {
                    Icon(
                        imageVector = Icons.Default.CleaningServices,
                        contentDescription = "Limpiar Caché",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(18.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "LIMPIAR CACHÉ AHORA",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp,
                            fontSize = 22.sp
                        )
                        if (isFocused) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.Black)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "PULSA OK",
                                    color = CleanGreenBright,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                    Text(
                        text = "Optimiza almacenamiento y memoria residual con una sola acción",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
