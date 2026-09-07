package com.example.ui.components

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CleanResult
import com.example.system.FormatUtils
import com.example.ui.theme.CleanGreen
import com.example.ui.theme.CleanGreenBright
import com.example.ui.theme.TvAccentCyan
import com.example.ui.theme.TvBorder
import com.example.ui.theme.TvSurfaceDark
import com.example.ui.theme.TvSurfaceVariant
import com.example.ui.theme.TvTextMuted
import com.example.ui.theme.TvTextPrimary
import com.example.ui.theme.TvTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleanResultModal(
    result: CleanResult,
    onDismiss: () -> Unit
) {
    val acceptFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        acceptFocusRequester.requestFocus()
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .widthIn(max = 560.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(TvSurfaceDark)
            .border(1.5.dp, CleanGreen.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
            .padding(28.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(CleanGreen.copy(alpha = 0.18f))
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Limpieza Exitosa",
                    tint = CleanGreenBright,
                    modifier = Modifier.size(44.dp)
                )
            }

            Text(
                text = "¡Caché del Sistema Liberada!",
                style = MaterialTheme.typography.headlineSmall,
                color = TvTextPrimary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            // Freed Space Highlight
            val freedText = if (result.freedBytes > 0) {
                FormatUtils.formatBytes(result.freedBytes)
            } else {
                "Memoria Optimizada"
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(TvSurfaceVariant)
                    .border(1.dp, TvBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "ESPACIO RECLAMADO",
                        style = MaterialTheme.typography.labelMedium,
                        color = TvTextSecondary,
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = freedText,
                        style = MaterialTheme.typography.headlineLarge,
                        color = CleanGreenBright,
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Before vs After row
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Antes",
                                style = MaterialTheme.typography.labelSmall,
                                color = TvTextMuted
                            )
                            Text(
                                text = FormatUtils.formatBytes(result.previousFreeBytes),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TvTextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Incremento",
                            tint = CleanGreen,
                            modifier = Modifier.size(20.dp)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Ahora",
                                style = MaterialTheme.typography.labelSmall,
                                color = TvTextMuted
                            )
                            Text(
                                text = FormatUtils.formatBytes(result.newFreeBytes),
                                style = MaterialTheme.typography.bodyMedium,
                                color = CleanGreenBright,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Text(
                text = "Android purga la caché residual y archivos temporales para garantizar fluidez en tu Smart TV.",
                style = MaterialTheme.typography.bodySmall,
                color = TvTextMuted,
                textAlign = TextAlign.Center,
                fontSize = 13.sp
            )

            // D-Pad focusable Dismiss Button
            AcceptButton(
                onClick = onDismiss,
                focusRequester = acceptFocusRequester,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AcceptButton(
    onClick: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.05f else 1.0f,
        animationSpec = tween(150),
        label = "accept_btn_scale"
    )

    val shape = RoundedCornerShape(16.dp)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .background(if (isFocused) CleanGreenBright else CleanGreen)
            .then(
                if (isFocused) Modifier.border(3.dp, Color.White, shape)
                else Modifier
            )
            .focusRequester(focusRequester)
            .focusable(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .testTag("result_accept_button")
            .padding(vertical = 14.dp)
    ) {
        Text(
            text = "ENTENDIDO",
            color = Color.Black,
            fontWeight = FontWeight.Black,
            fontSize = 17.sp,
            letterSpacing = 1.sp
        )
    }
}
