package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CleanResult
import com.example.model.StorageStatus
import com.example.system.FormatUtils
import com.example.ui.theme.CleanGreen
import com.example.ui.theme.CleanGreenBright
import com.example.ui.theme.TvAccentAmber
import com.example.ui.theme.TvAccentCyan
import com.example.ui.theme.TvBorder
import com.example.ui.theme.TvSurfaceDark
import com.example.ui.theme.TvTextMuted
import com.example.ui.theme.TvTextPrimary
import com.example.ui.theme.TvTextSecondary

@Composable
fun MetricGaugesRow(
    storageStatus: StorageStatus?,
    cpuUsagePercent: Float,
    lastCleanResult: CleanResult?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Storage Card
        StorageGaugeCard(
            storageStatus = storageStatus,
            lastCleanResult = lastCleanResult,
            modifier = Modifier.weight(1f)
        )

        // CPU Card
        CpuGaugeCard(
            cpuUsagePercent = cpuUsagePercent,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StorageGaugeCard(
    storageStatus: StorageStatus?,
    lastCleanResult: CleanResult?,
    modifier: Modifier = Modifier
) {
    val usagePercent = storageStatus?.usagePercent ?: 0f
    val animatedPercent by animateFloatAsState(
        targetValue = usagePercent,
        animationSpec = tween(600),
        label = "storage_percent"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(TvSurfaceDark)
            .border(1.dp, TvBorder, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PieChart,
                        contentDescription = "Almacenamiento",
                        tint = CleanGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ALMACENAMIENTO",
                        style = MaterialTheme.typography.labelLarge,
                        color = TvTextSecondary,
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "${FormatUtils.formatPercent(usagePercent)} en uso",
                    style = MaterialTheme.typography.bodySmall,
                    color = TvTextMuted,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Meter + Stats
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressGauge(
                    progress = (animatedPercent / 100f).coerceIn(0f, 1f),
                    color = CleanGreen,
                    trackColor = TvBorder,
                    centerText = "${FormatUtils.formatPercent(100f - usagePercent)}\nlibre",
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Espacio Disponible",
                        style = MaterialTheme.typography.bodySmall,
                        color = TvTextSecondary
                    )
                    Text(
                        text = FormatUtils.formatBytes(storageStatus?.availableBytes ?: 0L),
                        style = MaterialTheme.typography.headlineSmall,
                        color = CleanGreenBright,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = "Total: ${FormatUtils.formatBytes(storageStatus?.totalBytes ?: 0L)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TvTextMuted
                    )
                }
            }

            // Before / After Indicator if cleaned
            if (lastCleanResult != null && lastCleanResult.freedBytes > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CleanGreen.copy(alpha = 0.12f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Liberado",
                            tint = CleanGreenBright,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "+${FormatUtils.formatBytes(lastCleanResult.freedBytes)} liberados tras última limpieza",
                            style = MaterialTheme.typography.bodySmall,
                            color = CleanGreenBright,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CpuGaugeCard(
    cpuUsagePercent: Float,
    modifier: Modifier = Modifier
) {
    val animatedCpu by animateFloatAsState(
        targetValue = cpuUsagePercent,
        animationSpec = tween(500),
        label = "cpu_percent"
    )

    val cpuColor = when {
        cpuUsagePercent < 45f -> TvAccentCyan
        cpuUsagePercent < 75f -> TvAccentAmber
        else -> Color(0xFFEF4444)
    }

    val statusText = when {
        cpuUsagePercent < 45f -> "Nivel Normal"
        cpuUsagePercent < 75f -> "Carga Moderada"
        else -> "Carga Elevada"
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(TvSurfaceDark)
            .border(1.dp, TvBorder, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "CPU",
                        tint = cpuColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "USO DE CPU",
                        style = MaterialTheme.typography.labelLarge,
                        color = TvTextSecondary,
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodySmall,
                    color = cpuColor,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Meter + Stats
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressGauge(
                    progress = (animatedCpu / 100f).coerceIn(0f, 1f),
                    color = cpuColor,
                    trackColor = TvBorder,
                    centerText = FormatUtils.formatPercent(animatedCpu),
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Carga del Sistema",
                        style = MaterialTheme.typography.bodySmall,
                        color = TvTextSecondary
                    )
                    Text(
                        text = FormatUtils.formatPercent(animatedCpu),
                        style = MaterialTheme.typography.headlineSmall,
                        color = cpuColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = "Monitoreo dinámico en tiempo real",
                        style = MaterialTheme.typography.bodySmall,
                        color = TvTextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun CircularProgressGauge(
    progress: Float,
    color: Color,
    trackColor: Color,
    centerText: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokeWidth = 10.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2
            val topLeft = androidx.compose.ui.geometry.Offset(
                (size.width - radius * 2) / 2,
                (size.height - radius * 2) / 2
            )
            val arcSize = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)

            // Background Track Arc (260 degrees)
            drawArc(
                color = trackColor,
                startAngle = 140f,
                sweepAngle = 260f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress Arc
            drawArc(
                color = color,
                startAngle = 140f,
                sweepAngle = 260f * progress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Text(
            text = centerText,
            color = TvTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            lineHeight = 18.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
