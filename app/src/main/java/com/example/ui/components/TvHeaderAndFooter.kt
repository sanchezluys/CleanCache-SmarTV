package com.example.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CleanGreen
import com.example.ui.theme.CleanGreenBright
import com.example.ui.theme.TvAccentCyan
import com.example.ui.theme.TvBorder
import com.example.ui.theme.TvSurfaceVariant
import com.example.ui.theme.TvTextMuted
import com.example.ui.theme.TvTextPrimary
import com.example.ui.theme.TvTextSecondary
import java.util.Calendar

@Composable
fun TvHeader(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(TvSurfaceVariant)
                    .border(1.dp, TvBorder, RoundedCornerShape(14.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cleancache_logo),
                    contentDescription = "CleanCache Logo",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "CleanCache",
                        style = MaterialTheme.typography.titleLarge,
                        color = TvTextPrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "SmarTV",
                        style = MaterialTheme.typography.titleLarge,
                        color = CleanGreenBright,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp
                    )
                }
                Text(
                    text = "Optimizador de Almacenamiento & Caché",
                    style = MaterialTheme.typography.bodySmall,
                    color = TvTextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        // Live System Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(TvSurfaceVariant)
                .border(1.dp, TvBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(CleanGreen)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Google TV Edition",
                    style = MaterialTheme.typography.labelSmall,
                    color = TvTextSecondary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun TvFooter(
    versionName: String = "1.0",
    developerEmail: String = "sanchezluys@gmail.com",
    modifier: Modifier = Modifier
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.25f))
            .border(1.dp, TvBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        // App Version
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Versión: ",
                style = MaterialTheme.typography.bodySmall,
                color = TvTextMuted,
                fontSize = 13.sp
            )
            Text(
                text = "v$versionName",
                style = MaterialTheme.typography.bodySmall,
                color = TvTextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }

        // Developer
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Desarrollador: ",
                style = MaterialTheme.typography.bodySmall,
                color = TvTextMuted,
                fontSize = 13.sp
            )
            Text(
                text = developerEmail,
                style = MaterialTheme.typography.bodySmall,
                color = CleanGreenBright,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
        }

        // Dynamic Year
        Text(
            text = "© $currentYear Todos los derechos reservados",
            style = MaterialTheme.typography.bodySmall,
            color = TvTextMuted,
            fontSize = 13.sp
        )
    }
}
