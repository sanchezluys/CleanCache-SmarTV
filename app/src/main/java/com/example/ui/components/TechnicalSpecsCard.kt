package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.SdCard
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeviceSpecs
import com.example.system.FormatUtils
import com.example.ui.theme.CleanGreen
import com.example.ui.theme.TvAccentCyan
import com.example.ui.theme.TvBorder
import com.example.ui.theme.TvSurfaceDark
import com.example.ui.theme.TvTextMuted
import com.example.ui.theme.TvTextPrimary
import com.example.ui.theme.TvTextSecondary

@Composable
fun TechnicalSpecsCard(
    specs: DeviceSpecs?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(TvSurfaceDark)
            .border(1.dp, TvBorder, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Header with badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(TvAccentCyan)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "DATOS TÉCNICOS",
                        style = MaterialTheme.typography.labelLarge,
                        color = TvTextSecondary,
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = specs?.cpuArchitecture?.uppercase() ?: "ARM64",
                    style = MaterialTheme.typography.bodySmall,
                    color = TvTextMuted,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Processor
            SpecRowItem(
                icon = Icons.Default.SettingsSuggest,
                iconColor = TvAccentCyan,
                title = "Procesador",
                subtitle = specs?.processorName ?: "Cargando CPU...",
                detail = "${specs?.cpuCores ?: 4} Núcleos (${specs?.cpuArchitecture ?: "arm64"})"
            )

            // RAM
            val ramTotalStr = FormatUtils.formatBytes(specs?.totalRamBytes ?: 0L)
            val ramAvailStr = FormatUtils.formatBytes(specs?.availRamBytes ?: 0L)
            val ramUsedStr = FormatUtils.formatBytes(specs?.usedRamBytes ?: 0L)
            val ramProgress = ((specs?.ramUsagePercent ?: 0f) / 100f).coerceIn(0f, 1f)

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                SpecRowItem(
                    icon = Icons.Default.Memory,
                    iconColor = CleanGreen,
                    title = "Memoria RAM",
                    subtitle = "$ramUsedStr en uso / $ramTotalStr total",
                    detail = "$ramAvailStr libre (${FormatUtils.formatPercent(100f - (specs?.ramUsagePercent ?: 0f))})"
                )
                LinearProgressIndicator(
                    progress = { ramProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = CleanGreen,
                    trackColor = TvBorder
                )
            }

            // ROM (Internal Storage)
            val romTotalStr = FormatUtils.formatBytes(specs?.totalRomBytes ?: 0L)
            val romAvailStr = FormatUtils.formatBytes(specs?.availRomBytes ?: 0L)
            val romUsedStr = FormatUtils.formatBytes(specs?.usedRomBytes ?: 0L)
            val romProgress = ((specs?.romUsagePercent ?: 0f) / 100f).coerceIn(0f, 1f)

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                SpecRowItem(
                    icon = Icons.Default.SdCard,
                    iconColor = Color(0xFFF59E0B),
                    title = "Almacenamiento ROM",
                    subtitle = "$romUsedStr en uso / $romTotalStr total",
                    detail = "$romAvailStr libre (${FormatUtils.formatPercent(100f - (specs?.romUsagePercent ?: 0f))})"
                )
                LinearProgressIndicator(
                    progress = { romProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFFF59E0B),
                    trackColor = TvBorder
                )
            }
        }
    }
}

@Composable
private fun SpecRowItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    detail: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconColor.copy(alpha = 0.15f))
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TvTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = TvTextSecondary,
                fontSize = 14.sp
            )
        }
        Text(
            text = detail,
            style = MaterialTheme.typography.labelMedium,
            color = TvTextMuted,
            fontSize = 13.sp
        )
    }
}
