package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.CampusSocialEvent
import com.example.data.NpcRelationshipState
import com.example.data.RelationshipType
import com.example.data.SocialClique
import com.example.data.StudentCareer
import com.example.data.StudentClub
import com.example.data.UnnCampusCatalog
import com.example.data.UnnPoliticalOffice
import com.example.data.UnnSocialAndClubsData
import com.example.ui.theme.AlertCrimson
import com.example.ui.theme.CloutCyan
import com.example.ui.theme.LionGold
import com.example.ui.theme.LionGoldBright
import com.example.ui.theme.SanityViolet
import com.example.ui.theme.UnnDeepGreen
import com.example.ui.theme.UnnEmerald
import com.example.ui.theme.UnnMintAccent

enum class SocialSubSection(val label: String) {
    RELATIONSHIPS("Relationships"),
    CLUBS_AND_CLIQUES("Clubs & Cliques"),
    PARTIES_CONCERTS("Parties & Events"),
    SUG_ELECTIONS("SUG Politics")
}

@Composable
fun SocialLifeAndHierarchyTabContent(
    career: StudentCareer,
    npcStates: List<NpcRelationshipState>,
    joinedClubIds: Set<String>,
    activeCliqueName: String,
    onInteractNpc: (NpcRelationshipState, String) -> Unit, // action: "FRIEND", "ROMANCE", "RIVAL"
    onJoinOrAttendClub: (StudentClub) -> Unit,
    onSelectClique: (SocialClique) -> Unit,
    onAttendSocialEvent: (CampusSocialEvent) -> Unit,
    onCampaignOffice: (UnnPoliticalOffice) -> Unit
) {
    var activeSection by remember { mutableStateOf(SocialSubSection.RELATIONSHIPS) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hierarchy & Clique Header Banner + Sub-navigation Filter Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = UnnEmerald),
                    border = BorderStroke(1.5.dp, LionGold)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "CAMPUS SOCIAL STRATA & CLIQUE",
                            style = MaterialTheme.typography.labelMedium,
                            color = LionGoldBright
                        )
                        Text(
                            text = career.socialTier,
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White
                        )
                        Text(
                            text = "Clique: $activeCliqueName • Clubs: ${joinedClubIds.size} • SUG Office: ${career.sugOffice}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = UnnMintAccent
                        )
                        Text(
                            text = "Street Cred: ${career.streetCred}/100 • Political Clout: ${career.politicalClout}/100",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFD8F3DC)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SocialSubSection.entries.forEach { section ->
                        FilterChip(
                            selected = activeSection == section,
                            onClick = { activeSection = section },
                            label = { Text(section.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = LionGold,
                                selectedLabelColor = UnnDeepGreen
                            ),
                            modifier = Modifier.testTag("social_subtab_${section.name.lowercase()}")
                        )
                    }
                }
            }
        }

        when (activeSection) {
            SocialSubSection.RELATIONSHIPS -> {
                item {
                    Text(
                        text = "Friendships, Romance & Campus Rivalries",
                        style = MaterialTheme.typography.titleLarge,
                        color = LionGold
                    )
                    Text(
                        text = "Build friendships, shoot your shot for campus romance, or settle scores with rivals.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                items(npcStates, key = { it.npc.id }) { state ->
                    NpcRelationshipCard(
                        state = state,
                        isGraduated = career.isGraduated,
                        onFriendMove = { onInteractNpc(state, "FRIEND") },
                        onRomanceMove = { onInteractNpc(state, "ROMANCE") },
                        onRivalMove = { onInteractNpc(state, "RIVAL") }
                    )
                }
            }

            SocialSubSection.CLUBS_AND_CLIQUES -> {
                item {
                    Text(
                        text = "Student Clubs, Fellowships & Societies",
                        style = MaterialTheme.typography.titleLarge,
                        color = LionGold
                    )
                    Text(
                        text = "Join religious fellowships, academic debate societies, cultural Kegite ensembles, or tech hubs.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                items(UnnSocialAndClubsData.clubs, key = { it.id }) { club ->
                    val isMember = joinedClubIds.contains(club.id)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isMember) UnnEmerald else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = if (isMember) BorderStroke(1.5.dp, LionGold) else null
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = club.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (isMember) LionGoldBright else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = club.category,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = UnnMintAccent
                                    )
                                }
                                Text(
                                    text = if (isMember) "MEMBER" else "Dues: ₦${club.duesNaira}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = LionGold
                                )
                            }
                            Text(
                                text = club.weeklyPerkDescription,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Button(
                                onClick = { onJoinOrAttendClub(club) },
                                enabled = !career.isGraduated,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("club_action_${club.id}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LionGold,
                                    contentColor = UnnDeepGreen
                                )
                            ) {
                                Icon(Icons.Default.Groups, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isMember) "Attend Weekly Fellowship / Meeting (Free)"
                                    else "Pay Dues & Join Society (₦${club.duesNaira})",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Campus Social Strata & Cliques",
                        style = MaterialTheme.typography.titleLarge,
                        color = LionGold
                    )
                    Text(
                        text = "Align with an elite UNN student clique once you meet their standing requirements.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                items(UnnSocialAndClubsData.socialCliques, key = { it.id }) { clique ->
                    val isCurrent = activeCliqueName == clique.name
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) UnnEmerald else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = if (isCurrent) BorderStroke(1.5.dp, LionGold) else null
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = clique.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isCurrent) LionGoldBright else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "\"${clique.motto}\" • 📍 ${clique.turfLandmark}",
                                style = MaterialTheme.typography.labelMedium,
                                color = UnnMintAccent
                            )
                            Text(
                                text = "Requirements: ${clique.requirementDescription}",
                                style = MaterialTheme.typography.labelMedium,
                                color = LionGold
                            )
                            Text(
                                text = clique.perkSummary,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            OutlinedButton(
                                onClick = { onSelectClique(clique) },
                                enabled = !isCurrent && !career.isGraduated,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("join_clique_${clique.id}"),
                                border = BorderStroke(1.dp, LionGold)
                            ) {
                                Text(
                                    text = if (isCurrent) "Active Clique" else "Pledge & Rep This Clique",
                                    color = LionGoldBright
                                )
                            }
                        }
                    }
                }
            }

            SocialSubSection.PARTIES_CONCERTS -> {
                item {
                    Text(
                        text = "Concerts, Hostel Bashes & Campus Parties",
                        style = MaterialTheme.typography.titleLarge,
                        color = LionGold
                    )
                    Text(
                        text = "Turn up at PAA Auditorium concerts, Hilltop penthouse parties, and Okija/Mary Slessor fiestas!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                items(UnnSocialAndClubsData.socialEvents, key = { it.id }) { ev ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = ev.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "📍 ${ev.venueName} • ${ev.category}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = UnnMintAccent
                                    )
                                }
                                Text(
                                    text = "₦${ev.ticketCostNaira}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = LionGold
                                )
                            }
                            Text(
                                text = ev.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Sanity +${ev.sanityDelta} • Street Cred +${ev.credDelta} • Clout +${ev.cloutDelta} • Energy ${ev.energyDelta}",
                                style = MaterialTheme.typography.labelMedium,
                                color = LionGoldBright
                            )
                            Button(
                                onClick = { onAttendSocialEvent(ev) },
                                enabled = !career.isGraduated,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("attend_event_${ev.id}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LionGold,
                                    contentColor = UnnDeepGreen
                                )
                            ) {
                                Icon(Icons.Default.Celebration, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Attend Event (₦${ev.ticketCostNaira})", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            SocialSubSection.SUG_ELECTIONS -> {
                item {
                    Text(
                        text = "Contest for SUG & Campus Offices",
                        style = MaterialTheme.typography.titleLarge,
                        color = LionGold
                    )
                    Text(
                        text = "Win Student Union elections at Freedom Square to earn weekly stipends and campus authority.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                items(UnnCampusCatalog.politicalOffices, key = { it.title }) { office ->
                    val holdsOffice = career.sugOffice == office.title
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (holdsOffice) UnnEmerald else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = if (holdsOffice) BorderStroke(2.dp, LionGold) else null
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = office.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (holdsOffice) LionGoldBright else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "+₦${office.weeklyStipendNaira}/wk",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = UnnMintAccent
                                )
                            }
                            Text(
                                text = office.manifestoPitch,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Req: ${office.minLevel}L+ • Clout ${office.requiredClout}+ • Cred ${office.requiredCred}+ • Campaign: ₦${office.campaignCostNaira}",
                                style = MaterialTheme.typography.labelMedium,
                                color = LionGold
                            )
                            Button(
                                onClick = { onCampaignOffice(office) },
                                enabled = !holdsOffice && !career.isGraduated,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("campaign_office_${office.title.lowercase().replace(" ", "_")}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LionGold,
                                    contentColor = UnnDeepGreen
                                )
                            ) {
                                Icon(Icons.Default.Campaign, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (holdsOffice) "Currently In Office" else "Launch Campaign (₦${office.campaignCostNaira})",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NpcRelationshipCard(
    state: NpcRelationshipState,
    isGraduated: Boolean,
    onFriendMove: () -> Unit,
    onRomanceMove: () -> Unit,
    onRivalMove: () -> Unit
) {
    val badgeColor = when (state.type) {
        RelationshipType.ROMANCE -> Color(0xFFF472B6)
        RelationshipType.RIVAL -> AlertCrimson
        RelationshipType.MENTOR -> CloutCyan
        RelationshipType.FRIEND -> UnnMintAccent
    }
    val normalizedBar = ((state.affinity + 100) / 200f).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.45f))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = state.npc.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${state.npc.title} • 📍 ${state.npc.hostelOrOffice}",
                        style = MaterialTheme.typography.labelMedium,
                        color = UnnMintAccent
                    )
                }
                Surface(
                    color = badgeColor.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, badgeColor)
                ) {
                    Text(
                        text = "${state.type.label}: ${state.affinity}",
                        style = MaterialTheme.typography.labelMedium,
                        color = badgeColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "Status: ${state.statusBadge} • Clique: ${state.npc.cliqueName}",
                style = MaterialTheme.typography.labelLarge,
                color = LionGoldBright
            )

            LinearProgressIndicator(
                progress = { normalizedBar },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = badgeColor,
                trackColor = MaterialTheme.colorScheme.surface,
                strokeCap = StrokeCap.Round
            )

            Text(
                text = state.npc.bio,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onFriendMove,
                    enabled = !isGraduated,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("npc_friend_${state.npc.id}"),
                    border = BorderStroke(1.dp, UnnMintAccent),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.People, contentDescription = null, tint = UnnMintAccent)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Chill/Study", color = UnnMintAccent, style = MaterialTheme.typography.labelMedium)
                }

                if (state.npc.id != "Dr_Obinna") {
                    OutlinedButton(
                        onClick = onRomanceMove,
                        enabled = !isGraduated,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("npc_romance_${state.npc.id}"),
                        border = BorderStroke(1.dp, Color(0xFFF472B6)),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFF472B6))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Date (₦2k)", color = Color(0xFFF472B6), style = MaterialTheme.typography.labelMedium)
                    }
                }

                OutlinedButton(
                    onClick = onRivalMove,
                    enabled = !isGraduated,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("npc_rival_${state.npc.id}"),
                    border = BorderStroke(1.dp, LionGold),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Whatshot, contentDescription = null, tint = LionGold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (state.type == RelationshipType.RIVAL) "Showdown" else "Debate",
                        color = LionGoldBright,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}
