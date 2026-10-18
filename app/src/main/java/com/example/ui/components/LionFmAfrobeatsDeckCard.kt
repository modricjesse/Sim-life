package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AfrobeatsAudioEngine
import com.example.data.AfrobeatsTrack
import com.example.data.UnnAfrobeatsCatalog
import com.example.ui.theme.CloutCyan
import com.example.ui.theme.LionGold
import com.example.ui.theme.LionGoldBright
import com.example.ui.theme.SanityViolet
import com.example.ui.theme.UnnDeepGreen
import com.example.ui.theme.UnnEmerald
import com.example.ui.theme.UnnMintAccent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LionFmAfrobeatsDeckCard(
    inventory: List<String>,
    enabled: Boolean,
    onVibeToTrack: (AfrobeatsTrack) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val audioEngine = remember(scope) { AfrobeatsAudioEngine(scope) }

    var currentTrackIndex by rememberSaveable { mutableIntStateOf(0) }
    var isPlayingAudio by rememberSaveable { mutableStateOf(false) }
    var showFullPlaylist by rememberSaveable { mutableStateOf(false) }

    val tracks = UnnAfrobeatsCatalog.tracks
    val activeTrack = tracks[currentTrackIndex % tracks.size]

    val hasSpeakerOrPods = remember(inventory) {
        inventory.any {
            it.contains("Speaker", ignoreCase = true) ||
                it.contains("JBL", ignoreCase = true) ||
                it.contains("AirPods", ignoreCase = true) ||
                it.contains("Headphones", ignoreCase = true)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            audioEngine.stop()
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("lion_fm_afrobeats_deck_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = UnnEmerald),
        border = BorderStroke(1.5.dp, LionGold)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Radio Station Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Radio,
                        contentDescription = null,
                        tint = LionGoldBright
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LION FM 91.1 NSUKKA • AFROBEATS DECK",
                        style = MaterialTheme.typography.labelMedium,
                        color = LionGoldBright,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = if (isPlayingAudio) UnnMintAccent else UnnDeepGreen,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, LionGold.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Equalizer,
                            contentDescription = null,
                            tint = if (isPlayingAudio) UnnDeepGreen else LionGoldBright,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isPlayingAudio) "ON AIR • ${activeTrack.bpm} BPM" else "${activeTrack.subgenre} • ${activeTrack.bpm} BPM",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isPlayingAudio) UnnDeepGreen else LionGoldBright,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Now Spinning Track Display & Audio Controls
            Surface(
                color = UnnDeepGreen.copy(alpha = 0.85f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, LionGold.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = activeTrack.title,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${activeTrack.artist} • 📍 ${activeTrack.campusVibeLocation}",
                                style = MaterialTheme.typography.labelLarge,
                                color = UnnMintAccent
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    if (isPlayingAudio) {
                                        audioEngine.stop()
                                        isPlayingAudio = false
                                    } else {
                                        audioEngine.startTrack(activeTrack)
                                        isPlayingAudio = true
                                    }
                                },
                                modifier = Modifier.testTag("afrobeats_play_pause_button")
                            ) {
                                Icon(
                                    imageVector = if (isPlayingAudio) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                    contentDescription = if (isPlayingAudio) "Pause Afrobeats Audio" else "Play Afrobeats Audio",
                                    tint = LionGoldBright,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    val nextIdx = (currentTrackIndex + 1) % tracks.size
                                    currentTrackIndex = nextIdx
                                    if (isPlayingAudio) {
                                        audioEngine.startTrack(tracks[nextIdx])
                                    }
                                },
                                modifier = Modifier.testTag("afrobeats_next_track_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "Next Afrobeats Hit",
                                    tint = Color.White,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = activeTrack.vibeDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.88f)
                    )

                    // Stat Buff Preview Chips
                    val statPills = buildList {
                        if (activeTrack.energyDelta != 0) add("Energy +${activeTrack.energyDelta}")
                        if (activeTrack.sanityDelta != 0) add("Sanity +${activeTrack.sanityDelta}")
                        if (activeTrack.prepDelta != 0) add("Prep +${activeTrack.prepDelta}")
                        if (activeTrack.credDelta != 0) add("Cred +${activeTrack.credDelta}")
                        if (activeTrack.cloutDelta != 0) add("Clout +${activeTrack.cloutDelta}")
                        if (hasSpeakerOrPods) add("🔊 Speaker Gear +4 Cred")
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        statPills.forEach { pill ->
                            Surface(
                                color = UnnEmerald,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = pill,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = LionGoldBright,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Action Buttons: Blast & Boost Stats + Toggle Full 8-Track Afrobeats Crate
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onVibeToTrack(activeTrack) },
                    enabled = enabled,
                    modifier = Modifier
                        .weight(1.3f)
                        .height(48.dp)
                        .testTag("afrobeats_vibe_boost_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LionGold,
                        contentColor = UnnDeepGreen
                    )
                ) {
                    Icon(Icons.Default.Speaker, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Vibe & Boost Stats",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { showFullPlaylist = !showFullPlaylist },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("afrobeats_toggle_playlist_button"),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, LionGoldBright)
                ) {
                    Icon(Icons.Default.LibraryMusic, contentDescription = null, tint = LionGoldBright, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (showFullPlaylist) "Hide Hits (${tracks.size})" else "All Hits (${tracks.size})",
                        style = MaterialTheme.typography.labelLarge,
                        color = LionGoldBright
                    )
                }
            }

            // Expandable 8-Track Afrobeats Crate
            AnimatedVisibility(visible = showFullPlaylist) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Select an Afrobeats Hit to Spin on Lion FM 91.1:",
                        style = MaterialTheme.typography.labelLarge,
                        color = LionGoldBright
                    )
                    tracks.forEachIndexed { idx, track ->
                        val isSelected = idx == (currentTrackIndex % tracks.size)
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    currentTrackIndex = idx
                                    if (isPlayingAudio) {
                                        audioEngine.startTrack(track)
                                    }
                                }
                                .testTag("afrobeats_track_item_${track.id}"),
                            color = if (isSelected) UnnDeepGreen else UnnDeepGreen.copy(alpha = 0.55f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) LionGold else Color.White.copy(alpha = 0.2f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MusicNote,
                                        contentDescription = null,
                                        tint = if (isSelected) LionGoldBright else CloutCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "${track.title} — ${track.artist}",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                        Text(
                                            text = "${track.subgenre} • ${track.bpm} BPM • ${track.campusVibeLocation}",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = UnnMintAccent
                                        )
                                    }
                                }

                                Surface(
                                    color = if (isSelected) LionGold else SanityViolet.copy(alpha = 0.35f),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = if (isSelected) "SPINNING" else "SELECT",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isSelected) UnnDeepGreen else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
