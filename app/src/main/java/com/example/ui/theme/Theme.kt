package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = CleanGreen,
    onPrimary = Color.Black,
    primaryContainer = CleanGreenDark,
    onPrimaryContainer = CleanGreenGlow,
    secondary = TvAccentCyan,
    onSecondary = Color.Black,
    tertiary = TvAccentAmber,
    background = TvBackgroundDark,
    onBackground = TvTextPrimary,
    surface = TvSurfaceDark,
    onSurface = TvTextPrimary,
    surfaceVariant = TvSurfaceVariant,
    onSurfaceVariant = TvTextSecondary,
    outline = TvBorder
  )

@Composable
fun CleanCacheTheme(
  content: @Composable () -> Unit,
) {
  // Google TV & Smart TV apps are optimized for dark cinema mode
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content
  )
}

