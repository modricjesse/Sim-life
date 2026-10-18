package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CampusZone
import com.example.data.StudentCareer
import com.example.data.UnnInteractiveMapData
import com.example.data.UnnMapLandmark
import com.example.ui.theme.CloutCyan
import com.example.ui.theme.LionGold
import com.example.ui.theme.LionGoldBright
import com.example.ui.theme.NsukkaClayRed
import com.example.ui.theme.SanityViolet
import com.example.ui.theme.UnnDeepGreen
import com.example.ui.theme.UnnEmerald
import com.example.ui.theme.UnnForestGreen
import com.example.ui.theme.UnnMintAccent
import kotlin.math.hypot

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InteractiveUnnMapTabContent(
    career: StudentCareer,
    currentLocationId: String,
    onNavigateTrek: (UnnMapLandmark) -> Unit,
    onNavigateShuttle: (UnnMapLandmark) -> Unit,
    onExecuteLandmarkAction: (UnnMapLandmark) -> Unit,
    onTriggerHostelNightRest: () -> Unit = {}
) {
    val currentLandmark = remember(currentLocationId) {
        UnnInteractiveMapData.findLandmark(currentLocationId)
    }
    var selectedLandmarkId by remember(currentLocationId) {
        mutableStateOf(currentLocationId)
    }
    var selectedZoneFilter by remember { mutableStateOf<CampusZone?>(null) }

    val selectedLandmark = remember(selectedLandmarkId) {
        UnnInteractiveMapData.findLandmark(selectedLandmarkId)
    }

    val visibleLandmarks = remember(selectedZoneFilter) {
        if (selectedZoneFilter == null) UnnInteractiveMapData.landmarks
        else UnnInteractiveMapData.landmarks.filter { it.zone == selectedZoneFilter }
    }

    val distanceMeters = remember(currentLandmark, selectedLandmark) {
        UnnInteractiveMapData.calculateDistanceMeters(currentLandmark, selectedLandmark)
    }
    val trekEnergy = remember(currentLandmark, selectedLandmark) {
        UnnInteractiveMapData.calculateTrekEnergyCost(currentLandmark, selectedLandmark)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("unn_map_lazy_column"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "UNN Nsukka Interactive Campus Map",
                    style = MaterialTheme.typography.headlineLarge,
                    color = LionGold
                )
                Text(
                    text = "Tap any campus landmark node to inspect, navigate via Shuttle or Trek, and perform on-site activities.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Current GPS Banner
                Surface(
                    color = UnnEmerald,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, LionGold.copy(alpha = 0.7f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "Current Location",
                                tint = LionGoldBright,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "YOU ARE CURRENTLY AT:",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = LionGoldBright
                                )
                                Text(
                                    text = currentLandmark.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Surface(
                            color = UnnDeepGreen,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.clickable { selectedLandmarkId = currentLandmark.id }
                        ) {
                            Text(
                                text = "Center Pin",
                                style = MaterialTheme.typography.labelMedium,
                                color = LionGoldBright,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                // Zone Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedZoneFilter == null,
                        onClick = { selectedZoneFilter = null },
                        label = { Text("All Zones (${UnnInteractiveMapData.landmarks.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = LionGold,
                            selectedLabelColor = UnnDeepGreen
                        ),
                        modifier = Modifier.testTag("map_zone_all")
                    )
                    CampusZone.entries.forEach { zone ->
                        FilterChip(
                            selected = selectedZoneFilter == zone,
                            onClick = { selectedZoneFilter = zone },
                            label = { Text(zone.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = LionGold,
                                selectedLabelColor = UnnDeepGreen
                            ),
                            modifier = Modifier.testTag("map_zone_${zone.name.lowercase()}")
                        )
                    }
                }

                // Interactive 2D Vector Map Canvas
                UnnCampusInteractiveCanvas(
                    landmarks = UnnInteractiveMapData.landmarks,
                    highlightedZone = selectedZoneFilter,
                    currentLandmark = currentLandmark,
                    selectedLandmark = selectedLandmark,
                    onSelectLandmark = { clicked -> selectedLandmarkId = clicked.id }
                )
            }
        }

        // Selected Landmark Navigation & Action Panel
        item {
            SelectedLandmarkNavigationCard(
                career = career,
                currentLandmark = currentLandmark,
                selectedLandmark = selectedLandmark,
                distanceMeters = distanceMeters,
                trekEnergyCost = trekEnergy,
                onNavigateTrek = { onNavigateTrek(selectedLandmark) },
                onNavigateShuttle = { onNavigateShuttle(selectedLandmark) },
                onExecuteAction = { onExecuteLandmarkAction(selectedLandmark) },
                onTriggerHostelNightRest = onTriggerHostelNightRest
            )
        }

        // Directory List of Landmarks for Quick Accessibility & Scanning
        item {
            Text(
                text = "Campus Landmark Directory (Tap to Select)",
                style = MaterialTheme.typography.titleLarge,
                color = LionGold
            )
        }

        items(visibleLandmarks, key = { it.id }) { landmark ->
            val isHere = landmark.id == currentLandmark.id
            val isSelected = landmark.id == selectedLandmark.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedLandmarkId = landmark.id }
                    .testTag("map_directory_${landmark.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) UnnEmerald else MaterialTheme.colorScheme.surfaceVariant
                ),
                border = if (isHere) BorderStroke(2.dp, UnnMintAccent)
                else if (isSelected) BorderStroke(1.5.dp, LionGold) else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = landmark.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isSelected) LionGoldBright else MaterialTheme.colorScheme.onSurface
                            )
                            if (isHere) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = UnnMintAccent,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "HERE",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = UnnDeepGreen,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "${landmark.zone.label} • ${landmark.facilities.joinToString(" • ")}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = if (isHere) UnnMintAccent else LionGold
                    )
                }
            }
        }
    }
}

@Composable
private fun UnnCampusInteractiveCanvas(
    landmarks: List<UnnMapLandmark>,
    highlightedZone: CampusZone?,
    currentLandmark: UnnMapLandmark,
    selectedLandmark: UnnMapLandmark,
    onSelectLandmark: (UnnMapLandmark) -> Unit
) {
    val textMeasurer = rememberTextMeasurer()
    val landmarkById = remember(landmarks) { landmarks.associateBy { it.id } }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .testTag("interactive_unn_campus_map_canvas"),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.5.dp, LionGold.copy(alpha = 0.7f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF062316),
                                Color(0xFF0A3321),
                                Color(0xFF0E3E28)
                            )
                        )
                    )
                    .pointerInput(landmarks) {
                        detectTapGestures { tapOffset ->
                            val w = size.width.toFloat()
                            val h = size.height.toFloat()
                            val nearest = landmarks.minByOrNull { lm ->
                                hypot(lm.xFraction * w - tapOffset.x, lm.yFraction * h - tapOffset.y)
                            }
                            if (nearest != null) {
                                val distPx = hypot(
                                    nearest.xFraction * w - tapOffset.x,
                                    nearest.yFraction * h - tapOffset.y
                                )
                                if (distPx <= 110f) {
                                    onSelectLandmark(nearest)
                                }
                            }
                        }
                    }
            ) {
                val w = size.width
                val h = size.height

                // 1. Subtle Topographical Grid & Zone Quadrants
                val gridStep = 60f
                var gx = gridStep
                while (gx < w) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.05f),
                        start = Offset(gx, 0f),
                        end = Offset(gx, h),
                        strokeWidth = 1f
                    )
                    gx += gridStep
                }
                var gy = gridStep
                while (gy < h) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.05f),
                        start = Offset(0f, gy),
                        end = Offset(w, gy),
                        strokeWidth = 1f
                    )
                    gy += gridStep
                }

                // Campus Sector Tint Pads
                drawRoundRect(
                    color = UnnEmerald.copy(alpha = 0.25f),
                    topLeft = Offset(w * 0.12f, h * 0.14f),
                    size = Size(w * 0.50f, h * 0.30f),
                    cornerRadius = CornerRadius(28f, 28f)
                )
                drawRoundRect(
                    color = NsukkaClayRed.copy(alpha = 0.16f),
                    topLeft = Offset(w * 0.18f, h * 0.64f),
                    size = Size(w * 0.72f, h * 0.25f),
                    cornerRadius = CornerRadius(28f, 28f)
                )

                // 2. Draw Connected Campus Roads (University Road, Zik's Drive, Hostel Ring)
                landmarks.forEach { from ->
                    val startPos = Offset(from.xFraction * w, from.yFraction * h)
                    from.connectedTo.forEach { toId ->
                        val to = landmarkById[toId]
                        if (to != null && from.id < to.id) {
                            val endPos = Offset(to.xFraction * w, to.yFraction * h)
                            drawLine(
                                color = Color(0xFF2D6A4F),
                                start = startPos,
                                end = endPos,
                                strokeWidth = 7f
                            )
                            drawLine(
                                color = Color(0xFF95D5B2).copy(alpha = 0.35f),
                                start = startPos,
                                end = endPos,
                                strokeWidth = 2f
                            )
                        }
                    }
                }

                // 3. Draw Active Navigation Route Line (Current Location -> Selected Landmark)
                if (currentLandmark.id != selectedLandmark.id) {
                    val curPos = Offset(currentLandmark.xFraction * w, currentLandmark.yFraction * h)
                    val selPos = Offset(selectedLandmark.xFraction * w, selectedLandmark.yFraction * h)
                    drawLine(
                        color = LionGoldBright,
                        start = curPos,
                        end = selPos,
                        strokeWidth = 6f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 10f), 0f)
                    )
                }

                // 4. Draw Each Landmark Node & Label
                landmarks.forEach { lm ->
                    val center = Offset(lm.xFraction * w, lm.yFraction * h)
                    val isCurrent = lm.id == currentLandmark.id
                    val isSelected = lm.id == selectedLandmark.id
                    val isZoneDimmed = highlightedZone != null && lm.zone != highlightedZone

                    val nodeColor = when (lm.zone) {
                        CampusZone.ACADEMIC_CORE -> LionGold
                        CampusZone.HOSTEL_RING -> NsukkaClayRed
                        CampusZone.STUDENT_CENTER -> UnnMintAccent
                        CampusZone.LECTURE_THEATERS -> CloutCyan
                        CampusZone.GATES_AND_LODGES -> SanityViolet
                    }.let { if (isZoneDimmed) it.copy(alpha = 0.35f) else it }

                    // Outer aura if Current or Selected
                    if (isCurrent) {
                        drawCircle(
                            color = UnnMintAccent.copy(alpha = 0.30f),
                            radius = 30f,
                            center = center
                        )
                        drawCircle(
                            color = UnnMintAccent,
                            radius = 22f,
                            center = center,
                            style = Stroke(width = 3.5f)
                        )
                    }
                    if (isSelected) {
                        drawCircle(
                            color = LionGoldBright.copy(alpha = 0.35f),
                            radius = 26f,
                            center = center
                        )
                        drawCircle(
                            color = LionGoldBright,
                            radius = 18f,
                            center = center,
                            style = Stroke(width = 3f)
                        )
                    }

                    // Landmark Pin Circle
                    drawCircle(
                        color = UnnDeepGreen,
                        radius = 13f,
                        center = center
                    )
                    drawCircle(
                        color = nodeColor,
                        radius = 10f,
                        center = center
                    )

                    // Label Tag
                    val labelLayout = textMeasurer.measure(
                        text = lm.shortLabel,
                        style = TextStyle(
                            color = if (isSelected) LionGoldBright else if (isCurrent) UnnMintAccent else Color.White,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected || isCurrent) FontWeight.Bold else FontWeight.Medium
                        )
                    )
                    val labelX = (center.x - labelLayout.size.width / 2f).coerceIn(4f, w - labelLayout.size.width - 4f)
                    val labelY = (center.y + 14f).coerceIn(4f, h - labelLayout.size.height - 4f)

                    drawRoundRect(
                        color = Color(0xCC04150D),
                        topLeft = Offset(labelX - 4f, labelY - 2f),
                        size = Size(labelLayout.size.width + 8f, labelLayout.size.height + 4f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    drawText(
                        textLayoutResult = labelLayout,
                        topLeft = Offset(labelX, labelY)
                    )
                }
            }

            // Legend Pill in Top-Left Corner
            Surface(
                color = Color(0xCC062819),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LegendDot(color = LionGold, text = "Academic/Lib")
                    LegendDot(color = CloutCyan, text = "Lecture Halls")
                    LegendDot(color = NsukkaClayRed, text = "Hostels")
                    LegendDot(color = UnnMintAccent, text = "SUB/Arena")
                }
            }
        }
    }
}

@Composable
private fun LegendDot(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 10.sp,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectedLandmarkNavigationCard(
    career: StudentCareer,
    currentLandmark: UnnMapLandmark,
    selectedLandmark: UnnMapLandmark,
    distanceMeters: Int,
    trekEnergyCost: Int,
    onNavigateTrek: () -> Unit,
    onNavigateShuttle: () -> Unit,
    onExecuteAction: () -> Unit,
    onTriggerHostelNightRest: () -> Unit
) {
    val isAlreadyHere = currentLandmark.id == selectedLandmark.id
    val isHostelOrLodge = selectedLandmark.primaryActionCategory == "HOSTEL" ||
        selectedLandmark.zone == CampusZone.HOSTEL_RING ||
        selectedLandmark.zone == CampusZone.GATES_AND_LODGES

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.5.dp, if (isAlreadyHere) UnnMintAccent else LionGold)
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = selectedLandmark.zone.label.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = LionGoldBright
                    )
                    Text(
                        text = selectedLandmark.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Surface(
                    color = if (isAlreadyHere) UnnMintAccent else UnnEmerald,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (isAlreadyHere) "YOU ARE HERE" else "${distanceMeters}m away",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isAlreadyHere) UnnDeepGreen else LionGoldBright,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = selectedLandmark.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                selectedLandmark.facilities.forEach { fac ->
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "• $fac",
                            style = MaterialTheme.typography.labelMedium,
                            color = UnnMintAccent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (!isAlreadyHere) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateTrek,
                        enabled = !career.isGraduated,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("map_trek_button_${selectedLandmark.id}"),
                        border = BorderStroke(1.dp, LionGold)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.DirectionsWalk, contentDescription = null, tint = LionGold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Trek (-${trekEnergyCost} Energy)", color = LionGoldBright)
                    }

                    Button(
                        onClick = onNavigateShuttle,
                        enabled = !career.isGraduated,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("map_shuttle_button_${selectedLandmark.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UnnEmerald,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = LionGoldBright)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Shuttle (₦${UnnInteractiveMapData.SHUTTLE_FARE_NAIRA})")
                    }
                }
            }

            Button(
                onClick = onExecuteAction,
                enabled = !career.isGraduated,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("map_primary_action_${selectedLandmark.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LionGold,
                    contentColor = UnnDeepGreen
                )
            ) {
                Icon(
                    imageVector = if (isAlreadyHere) Icons.Default.PlayArrow else Icons.Default.Navigation,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isAlreadyHere) {
                        "${selectedLandmark.primaryActionLabel} (Wk +1)"
                    } else {
                        "Go & ${selectedLandmark.primaryActionLabel} (Wk +1)"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (isHostelOrLodge) {
                OutlinedButton(
                    onClick = onTriggerHostelNightRest,
                    enabled = !career.isGraduated,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("map_hostel_night_rest_button"),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.5.dp, SanityViolet)
                ) {
                    Icon(Icons.Default.Bedtime, contentDescription = null, tint = SanityViolet)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Turn In for the Night (+22 Energy & Midnight Roll)",
                        color = SanityViolet,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
