package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LionGold
import com.example.ui.theme.LionGoldBright
import com.example.ui.theme.NsukkaClayRed
import com.example.ui.theme.UnnDeepGreen
import com.example.ui.theme.UnnEmerald
import com.example.ui.theme.UnnForestGreen

@Composable
fun UnnCampusHeroBanner(
    title: String,
    subtitle: String,
    badgeText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(176.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(UnnDeepGreen, UnnForestGreen, Color(0xFF1B4332))
                )
            )
    ) {
        // Custom Canvas Illustration of UNN Nnamdi Azikiwe Library, Nsukka Hills & Golden Lion Sun
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Golden Nsukka Sun / Halo
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(LionGold.copy(alpha = 0.45f), Color.Transparent),
                    center = Offset(w * 0.82f, h * 0.28f),
                    radius = h * 0.65f
                ),
                center = Offset(w * 0.82f, h * 0.28f),
                radius = h * 0.65f
            )
            drawCircle(
                color = LionGoldBright.copy(alpha = 0.85f),
                radius = h * 0.16f,
                center = Offset(w * 0.82f, h * 0.28f)
            )

            // Rolling Nsukka Vet Mountain & Odim Hills silhouette
            val hillsPath = Path().apply {
                moveTo(0f, h * 0.78f)
                quadraticTo(w * 0.25f, h * 0.52f, w * 0.55f, h * 0.72f)
                quadraticTo(w * 0.80f, h * 0.55f, w, h * 0.68f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(hillsPath, color = UnnEmerald.copy(alpha = 0.55f))

            // Iconic Nnamdi Azikiwe Library architectural facade silhouette on right
            val libX = w * 0.62f
            val libY = h * 0.44f
            val libW = w * 0.32f
            val libH = h * 0.48f
            drawRoundRect(
                color = Color(0xFF082216),
                topLeft = Offset(libX, libY),
                size = Size(libW, libH),
                cornerRadius = CornerRadius(12f, 12f)
            )
            // Library vertical sun-breaker fins & glowing night-class windows
            for (i in 1..5) {
                val colX = libX + (libW / 6f) * i
                drawLine(
                    color = LionGold.copy(alpha = 0.55f),
                    start = Offset(colX, libY + 10f),
                    end = Offset(colX, libY + libH - 8f),
                    strokeWidth = 4f
                )
            }

            // Terracotta base stripe representing Nsukka red earth
            drawRect(
                color = NsukkaClayRed.copy(alpha = 0.45f),
                topLeft = Offset(0f, h * 0.91f),
                size = Size(w, h * 0.09f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = LionGold.copy(alpha = 0.2f),
                shape = RoundedCornerShape(50),
                border = androidx.compose.foundation.BorderStroke(1.dp, LionGold.copy(alpha = 0.6f))
            ) {
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelMedium,
                    color = LionGoldBright,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFD8F3DC)
                )
            }
        }
    }
}

@Composable
fun StatMeterPill(
    label: String,
    value: Long,
    maxValue: Long = 100L,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val progress = (value.toFloat() / maxValue.toFloat()).coerceIn(0f, 1f)
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$value",
                    style = MaterialTheme.typography.labelLarge,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = accentColor,
                trackColor = MaterialTheme.colorScheme.surface,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun LionCrestBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    listOf(LionGold, Color(0xFFD97706))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(36.dp)) {
            val w = size.width
            val h = size.height
            val shield = Path().apply {
                moveTo(w * 0.5f, h * 0.08f)
                lineTo(w * 0.88f, h * 0.24f)
                lineTo(w * 0.88f, h * 0.58f)
                quadraticTo(w * 0.5f, h * 0.95f, w * 0.5f, h * 0.95f)
                quadraticTo(w * 0.12f, h * 0.58f, w * 0.12f, h * 0.58f)
                lineTo(w * 0.12f, h * 0.24f)
                close()
            }
            drawPath(shield, color = UnnDeepGreen)
            drawCircle(
                color = LionGoldBright,
                radius = w * 0.20f,
                center = Offset(w * 0.5f, h * 0.48f)
            )
        }
    }
}
