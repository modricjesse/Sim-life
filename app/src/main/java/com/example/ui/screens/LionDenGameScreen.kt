package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AcademicStanding
import com.example.data.AfrobeatsTrack
import com.example.data.AlumniRecord
import com.example.data.CampusShenaniganEvent
import com.example.data.CampusSocialEvent
import com.example.data.HostelNightEvent
import com.example.data.HostelNightShenanigansGenerator
import com.example.data.NpcRelationshipState
import com.example.data.ShenaniganChoice
import com.example.data.SocialClique
import com.example.data.StudentCareer
import com.example.data.StudentClub
import com.example.data.UnnAcademicSystem
import com.example.data.UnnCampusCatalog
import com.example.data.UnnCourseProgress
import com.example.data.UnnHostelOption
import com.example.data.UnnInteractiveMapData
import com.example.data.UnnMapLandmark
import com.example.data.UnnPoliticalOffice
import com.example.data.UnnShenanigansEngine
import com.example.data.UnnShopItem
import com.example.data.UnnSocialAndClubsData
import com.example.data.UnnSpotAction
import com.example.ui.components.InteractiveUnnMapTabContent
import com.example.ui.components.LionFmAfrobeatsDeckCard
import com.example.ui.components.StatMeterPill
import com.example.ui.components.UnnCampusHeroBanner
import com.example.ui.theme.AlertCrimson
import com.example.ui.theme.CloutCyan
import com.example.ui.theme.LionGold
import com.example.ui.theme.LionGoldBright
import com.example.ui.theme.SanityViolet
import com.example.ui.theme.UnnDeepGreen
import com.example.ui.theme.UnnEmerald
import com.example.ui.theme.UnnMintAccent

enum class LionDenTab(val label: String) {
    DEN_HUB("Den Hub"),
    CAMPUS_MAP("UNN Map"),
    ACADEMICS("Academics"),
    SOCIAL_CLOUT("Social"),
    MARKET_RECORDS("Records")
}

@Composable
fun LionDenGameScreen(
    career: StudentCareer,
    allCareers: List<StudentCareer>,
    hallOfFame: List<AlumniRecord>,
    statusBanner: String?,
    activeEventModal: CampusShenaniganEvent?,
    activeNightEventModal: HostelNightEvent? = null,
    onDismissBanner: () -> Unit,
    onTriggerShenanigan: () -> Unit,
    onDismissShenanigan: () -> Unit,
    onResolveShenanigan: (CampusShenaniganEvent, ShenaniganChoice) -> Unit,
    onTriggerHostelNightRest: () -> Unit = {},
    onDismissNightShenanigan: () -> Unit = {},
    onResolveNightShenanigan: (HostelNightEvent, ShenaniganChoice) -> Unit = { _, _ -> },
    onVibeToAfrobeatsTrack: (AfrobeatsTrack) -> Unit = {},
    onPerformSpotAction: (UnnSpotAction) -> Unit,
    onNavigateMapTrek: (UnnMapLandmark) -> Unit,
    onNavigateMapShuttle: (UnnMapLandmark) -> Unit,
    onExecuteLandmarkAction: (UnnMapLandmark) -> Unit,
    onToggleCourseRegistration: (String) -> Unit,
    onAttendCourseLecture: (UnnCourseProgress) -> Unit,
    onStudyAtJpClarkLibrary: () -> Unit,
    onSitMidTermExams: () -> Unit,
    onSitFinalSemesterExams: () -> Unit,
    onInteractNpc: (NpcRelationshipState, String) -> Unit,
    onJoinOrAttendClub: (StudentClub) -> Unit,
    onSelectClique: (SocialClique) -> Unit,
    onAttendSocialEvent: (CampusSocialEvent) -> Unit,
    onBuyShopItem: (UnnShopItem) -> Unit,
    onSwitchHostel: (UnnHostelOption) -> Unit,
    onCampaignOffice: (UnnPoliticalOffice) -> Unit,
    onSelectCareer: (String) -> Unit,
    onStartNewCareer: () -> Unit,
    onDeleteCareer: (String) -> Unit,
    onSignOut: () -> Unit
) {
    var currentTab by rememberSaveable { mutableStateOf(LionDenTab.DEN_HUB) }

    val decodedCourses = remember(career.relationships, career.department, career.level, career.semester) {
        UnnAcademicSystem.decodeCourses(career.relationships, career.department, career.level, career.semester)
    }
    val npcStates = remember(career.relationships) {
        UnnSocialAndClubsData.parseNpcStates(career.relationships)
    }
    val joinedClubs = remember(career.relationships) {
        UnnSocialAndClubsData.parseJoinedClubs(career.relationships)
    }
    val activeClique = remember(career.relationships) {
        UnnSocialAndClubsData.parseActiveClique(career.relationships)
    }
    val currentLocationId = remember(career.relationships) {
        UnnSocialAndClubsData.parseCurrentLocation(career.relationships)
    }

    if (currentTab != LionDenTab.DEN_HUB) {
        BackHandler { currentTab = LionDenTab.DEN_HUB }
    }

    if (activeEventModal != null) {
        ShenaniganDilemmaDialog(
            event = activeEventModal,
            onDismiss = onDismissShenanigan,
            onSelectChoice = { choice -> onResolveShenanigan(activeEventModal, choice) }
        )
    } else if (activeNightEventModal != null) {
        HostelNightShenaniganDialog(
            event = activeNightEventModal,
            hostelName = career.hostel,
            inventory = career.inventory,
            onDismiss = onDismissNightShenanigan,
            onSelectChoice = { choice -> onResolveNightShenanigan(activeNightEventModal, choice) }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == LionDenTab.DEN_HUB,
                    onClick = { currentTab = LionDenTab.DEN_HUB },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Den Hub") },
                    label = { Text("Den Hub") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = UnnDeepGreen,
                        selectedTextColor = LionGold,
                        indicatorColor = LionGold
                    ),
                    modifier = Modifier.testTag("nav_tab_den_hub")
                )
                NavigationBarItem(
                    selected = currentTab == LionDenTab.CAMPUS_MAP,
                    onClick = { currentTab = LionDenTab.CAMPUS_MAP },
                    icon = { Icon(Icons.Default.Explore, contentDescription = "UNN Map") },
                    label = { Text("UNN Map") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = UnnDeepGreen,
                        selectedTextColor = LionGold,
                        indicatorColor = LionGold
                    ),
                    modifier = Modifier.testTag("nav_tab_campus")
                )
                NavigationBarItem(
                    selected = currentTab == LionDenTab.ACADEMICS,
                    onClick = { currentTab = LionDenTab.ACADEMICS },
                    icon = { Icon(Icons.Default.School, contentDescription = "Academics") },
                    label = { Text("Academics") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = UnnDeepGreen,
                        selectedTextColor = LionGold,
                        indicatorColor = LionGold
                    ),
                    modifier = Modifier.testTag("nav_tab_academics")
                )
                NavigationBarItem(
                    selected = currentTab == LionDenTab.SOCIAL_CLOUT,
                    onClick = { currentTab = LionDenTab.SOCIAL_CLOUT },
                    icon = { Icon(Icons.Default.Groups, contentDescription = "Social & Clubs") },
                    label = { Text("Social") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = UnnDeepGreen,
                        selectedTextColor = LionGold,
                        indicatorColor = LionGold
                    ),
                    modifier = Modifier.testTag("nav_tab_politics")
                )
                NavigationBarItem(
                    selected = currentTab == LionDenTab.MARKET_RECORDS,
                    onClick = { currentTab = LionDenTab.MARKET_RECORDS },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = "Market & Records") },
                    label = { Text("Records") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = UnnDeepGreen,
                        selectedTextColor = LionGold,
                        indicatorColor = LionGold
                    ),
                    modifier = Modifier.testTag("nav_tab_records")
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            StudentIdHeaderBar(
                career = career,
                currentLocationId = currentLocationId,
                statusBanner = statusBanner,
                onDismissBanner = onDismissBanner
            )

            when (currentTab) {
                LionDenTab.DEN_HUB -> DenHubTabContent(
                    career = career,
                    activeClique = activeClique,
                    onTriggerShenanigan = onTriggerShenanigan,
                    onTriggerHostelNightRest = onTriggerHostelNightRest,
                    onVibeToAfrobeatsTrack = onVibeToAfrobeatsTrack,
                    onQuickAction = onPerformSpotAction,
                    onJumpToTab = { currentTab = it }
                )

                LionDenTab.CAMPUS_MAP -> InteractiveUnnMapTabContent(
                    career = career,
                    currentLocationId = currentLocationId,
                    onNavigateTrek = onNavigateMapTrek,
                    onNavigateShuttle = onNavigateMapShuttle,
                    onExecuteLandmarkAction = onExecuteLandmarkAction,
                    onTriggerHostelNightRest = onTriggerHostelNightRest
                )

                LionDenTab.ACADEMICS -> AcademicProgressionTabContent(
                    career = career,
                    courses = decodedCourses,
                    onToggleCourseRegistration = onToggleCourseRegistration,
                    onAttendCourseLecture = onAttendCourseLecture,
                    onStudyAtJpClarkLibrary = onStudyAtJpClarkLibrary,
                    onSitMidTermExams = onSitMidTermExams,
                    onSitFinalSemesterExams = onSitFinalSemesterExams
                )

                LionDenTab.SOCIAL_CLOUT -> SocialLifeAndHierarchyTabContent(
                    career = career,
                    npcStates = npcStates,
                    joinedClubIds = joinedClubs,
                    activeCliqueName = activeClique,
                    onInteractNpc = onInteractNpc,
                    onJoinOrAttendClub = onJoinOrAttendClub,
                    onSelectClique = onSelectClique,
                    onAttendSocialEvent = onAttendSocialEvent,
                    onCampaignOffice = onCampaignOffice
                )

                LionDenTab.MARKET_RECORDS -> MarketAndRecordsCombinedTabContent(
                    career = career,
                    allCareers = allCareers,
                    hallOfFame = hallOfFame,
                    onBuyShopItem = onBuyShopItem,
                    onSwitchHostel = onSwitchHostel,
                    onSelectCareer = onSelectCareer,
                    onStartNewCareer = onStartNewCareer,
                    onDeleteCareer = onDeleteCareer,
                    onSignOut = onSignOut
                )
            }
        }
    }
}

@Composable
private fun StudentIdHeaderBar(
    career: StudentCareer,
    currentLocationId: String,
    statusBanner: String?,
    onDismissBanner: () -> Unit
) {
    val currentLm = remember(currentLocationId) {
        UnnInteractiveMapData.findLandmark(currentLocationId)
    }
    val standing = remember(career.cgpa) {
        UnnAcademicSystem.evaluateAcademicStanding(career.cgpa)
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = career.studentName,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = UnnEmerald,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = career.regNumber,
                                style = MaterialTheme.typography.labelMedium,
                                color = LionGoldBright,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "${career.department} • 📍 ${currentLm.shortLabel} • ${career.hostel}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₦${String.format("%,d", career.naira)}",
                        style = MaterialTheme.typography.titleLarge,
                        color = LionGold,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (career.isGraduated) "GRADUATED LION"
                        else if (standing == AcademicStanding.ACADEMIC_PROBATION) "PROBATION WARNING!"
                        else "${career.level}L • Sem ${career.semester} • Wk ${career.week}/8",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (standing == AcademicStanding.ACADEMIC_PROBATION) AlertCrimson else UnnMintAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatMeterPill(
                    label = "Energy",
                    value = career.energy,
                    accentColor = UnnMintAccent,
                    modifier = Modifier.weight(1f)
                )
                StatMeterPill(
                    label = "Sanity",
                    value = career.sanity,
                    accentColor = SanityViolet,
                    modifier = Modifier.weight(1f)
                )
                StatMeterPill(
                    label = "Exam Prep",
                    value = career.academicPrep,
                    accentColor = LionGold,
                    modifier = Modifier.weight(1f)
                )
                StatMeterPill(
                    label = "Clout",
                    value = career.politicalClout,
                    accentColor = CloutCyan,
                    modifier = Modifier.weight(1f)
                )
            }

            AnimatedVisibility(visible = statusBanner != null) {
                if (statusBanner != null) {
                    Surface(
                        color = UnnEmerald,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, LionGold.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .clickable { onDismissBanner() }
                            .testTag("status_feedback_banner")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = statusBanner,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Dismiss",
                                style = MaterialTheme.typography.labelMedium,
                                color = LionGoldBright,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DenHubTabContent(
    career: StudentCareer,
    activeClique: String,
    onTriggerShenanigan: () -> Unit,
    onTriggerHostelNightRest: () -> Unit,
    onVibeToAfrobeatsTrack: (AfrobeatsTrack) -> Unit,
    onQuickAction: (UnnSpotAction) -> Unit,
    onJumpToTab: (LionDenTab) -> Unit
) {
    val currentWeekEvent = remember(career.level, career.semester, career.week) {
        UnnShenanigansEngine.pickEventForWeek(career.level, career.semester, career.week)
    }
    val hostelRiskSummary = remember(career.hostel) {
        HostelNightShenanigansGenerator.hostelRiskProfileSummary(career.hostel)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            UnnCampusHeroBanner(
                title = "CGPA: ${String.format("%.2f", career.cgpa)} / 5.00",
                subtitle = "${career.socialTier} • Clique: $activeClique • Cred: ${career.streetCred}/100",
                badgeText = if (career.isGraduated) "CONVOCATION HALL OF FAME"
                else "${career.campus.uppercase()} CAMPUS • WEEK ${career.week} OF 8"
            )
        }

        // Quick Feature Shortcuts Row (Map, Academics, Social/Clubs)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onJumpToTab(LionDenTab.CAMPUS_MAP) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hub_shortcut_map"),
                    border = BorderStroke(1.dp, LionGold)
                ) {
                    Icon(Icons.Default.Explore, contentDescription = null, tint = LionGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("UNN Map", color = LionGoldBright, style = MaterialTheme.typography.labelMedium)
                }
                OutlinedButton(
                    onClick = { onJumpToTab(LionDenTab.ACADEMICS) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hub_shortcut_academics"),
                    border = BorderStroke(1.dp, UnnMintAccent)
                ) {
                    Icon(Icons.Default.School, contentDescription = null, tint = UnnMintAccent, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Courses", color = UnnMintAccent, style = MaterialTheme.typography.labelMedium)
                }
                OutlinedButton(
                    onClick = { onJumpToTab(LionDenTab.SOCIAL_CLOUT) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hub_shortcut_social"),
                    border = BorderStroke(1.dp, CloutCyan)
                ) {
                    Icon(Icons.Default.Groups, contentDescription = null, tint = CloutCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Clubs & Life", color = CloutCyan, style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        // Lion FM 91.1 & Campus Afrobeats Jukebox Deck
        item {
            LionFmAfrobeatsDeckCard(
                inventory = career.inventory,
                enabled = !career.isGraduated,
                onVibeToTrack = onVibeToAfrobeatsTrack
            )
        }

        // Weekly Campus Shenanigan / Dilemma Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.5.dp, LionGold)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = LionGold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "THIS WEEK'S CAMPUS SHENANIGAN",
                                style = MaterialTheme.typography.labelMedium,
                                color = LionGoldBright
                            )
                        }
                        Surface(
                            color = UnnEmerald,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = currentWeekEvent.category,
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = currentWeekEvent.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "📍 ${currentWeekEvent.locationTag}",
                        style = MaterialTheme.typography.labelLarge,
                        color = UnnMintAccent
                    )
                    Text(
                        text = currentWeekEvent.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = onTriggerShenanigan,
                        enabled = !career.isGraduated,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("open_weekly_shenanigan_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LionGold,
                            contentColor = UnnDeepGreen
                        )
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (career.isGraduated) "Journey Completed"
                            else if (career.week == 8L) "Handle Dilemma & Sit Semester Exams!"
                            else "Handle Campus Dilemma (Advances Week)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Midnight Hostel Life & Night Shenanigans Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.5.dp, SanityViolet)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bedtime,
                                contentDescription = null,
                                tint = SanityViolet
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "MIDNIGHT HOSTEL LIFE • ${career.hostel.uppercase()}",
                                style = MaterialTheme.typography.labelMedium,
                                color = SanityViolet
                            )
                        }
                        Surface(
                            color = SanityViolet.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, SanityViolet)
                        ) {
                            Text(
                                text = "NIGHT ROLL",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "Turn In for the Night at ${career.hostel}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = hostelRiskSummary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = onTriggerHostelNightRest,
                        enabled = !career.isGraduated,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("trigger_hostel_night_rest_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SanityViolet,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.Bedtime, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Rest at Hostel (+22 Energy, +8 Sanity & Night Event)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Quick Campus Actions Row
        item {
            Text(
                text = "Quick Moves Around the Den",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                UnnCampusCatalog.spotActions.take(4).forEach { spot ->
                    SpotActionCard(
                        spot = spot,
                        enabled = !career.isGraduated,
                        onPerform = { onQuickAction(spot) }
                    )
                }
            }
        }

        // Campus Chronicle Log Feed
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HistoryEdu, contentDescription = null, tint = LionGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "The Roar Chronicle (Campus Diary)",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    career.recentLogs.take(10).forEach { logLine ->
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = logLine,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpotActionCard(
    spot: UnnSpotAction,
    enabled: Boolean,
    onPerform: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = spot.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "📍 ${spot.landmark} • ${spot.category}",
                        style = MaterialTheme.typography.labelMedium,
                        color = UnnMintAccent
                    )
                }
                val costText = when {
                    spot.costNaira < 0 -> "+₦${-spot.costNaira}"
                    spot.costNaira == 0L -> "FREE"
                    else -> "-₦${spot.costNaira}"
                }
                Surface(
                    color = if (spot.costNaira < 0) UnnEmerald else MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = costText,
                        style = MaterialTheme.typography.labelMedium,
                        color = LionGoldBright,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = spot.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val statPreview = buildList {
                    if (spot.prepDelta != 0) add("Prep ${if (spot.prepDelta > 0) "+" else ""}${spot.prepDelta}")
                    if (spot.energyDelta != 0) add("Energy ${if (spot.energyDelta > 0) "+" else ""}${spot.energyDelta}")
                    if (spot.sanityDelta != 0) add("Sanity ${if (spot.sanityDelta > 0) "+" else ""}${spot.sanityDelta}")
                    if (spot.credDelta != 0) add("Cred +${spot.credDelta}")
                    if (spot.cloutDelta != 0) add("Clout +${spot.cloutDelta}")
                }.joinToString(" • ")

                Text(
                    text = statPreview,
                    style = MaterialTheme.typography.labelMedium,
                    color = LionGold,
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = onPerform,
                    enabled = enabled,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LionGold,
                        contentColor = UnnDeepGreen
                    ),
                    modifier = Modifier.testTag("spot_action_${spot.id}")
                ) {
                    Text("Go Now", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MarketAndRecordsCombinedTabContent(
    career: StudentCareer,
    allCareers: List<StudentCareer>,
    hallOfFame: List<AlumniRecord>,
    onBuyShopItem: (UnnShopItem) -> Unit,
    onSwitchHostel: (UnnHostelOption) -> Unit,
    onSelectCareer: (String) -> Unit,
    onStartNewCareer: () -> Unit,
    onDeleteCareer: (String) -> Unit,
    onSignOut: () -> Unit
) {
    var subView by remember { mutableStateOf("TRANSCRIPT") }
    val honoursClass = UnnShenanigansEngine.degreeClassification(career.cgpa, career.carryOvers)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = subView == "TRANSCRIPT",
                    onClick = { subView = "TRANSCRIPT" },
                    label = { Text("Transcript & Careers") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = LionGold,
                        selectedLabelColor = UnnDeepGreen
                    ),
                    modifier = Modifier.testTag("subview_transcript")
                )
                FilterChip(
                    selected = subView == "MARKET",
                    onClick = { subView = "MARKET" },
                    label = { Text("Odim Market & Hostels") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = LionGold,
                        selectedLabelColor = UnnDeepGreen
                    ),
                    modifier = Modifier.testTag("subview_market")
                )
            }
        }

        if (subView == "MARKET") {
            item {
                Text(
                    text = "Odim Gate & SUB Survival Market",
                    style = MaterialTheme.typography.headlineLarge,
                    color = LionGold
                )
                Text(
                    text = "Upgrade your Night Class kit, hostel cooking gear, and campus drip.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(UnnCampusCatalog.shopItems, key = { it.id }) { item ->
                val owned = career.inventory.contains(item.name)
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
                            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = "₦${item.priceNaira}", style = MaterialTheme.typography.titleMedium, color = LionGold)
                        }
                        Text(
                            text = item.benefit,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = { onBuyShopItem(item) },
                            enabled = !owned && !career.isGraduated,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("buy_item_${item.id}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LionGold,
                                contentColor = UnnDeepGreen
                            )
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (owned) "Owned in Hostel Locker" else "Buy for ₦${item.priceNaira}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Hostel & Off-Campus Lodge Relocation",
                    style = MaterialTheme.typography.titleLarge,
                    color = LionGold
                )
                Text(
                    text = "Current Residence: ${career.hostel}. Move to Mary Slessor, Okija, Zik's Flats, or Hilltop!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(UnnCampusCatalog.hostels, key = { it.name }) { hostel ->
                val isCurrent = career.hostel == hostel.name
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCurrent) UnnEmerald else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "${hostel.name} (${hostel.campus})", style = MaterialTheme.typography.titleMedium)
                            Text(text = "₦${hostel.rentNaira}", style = MaterialTheme.typography.labelLarge, color = LionGold)
                        }
                        Text(
                            text = hostel.vibe,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(
                            onClick = { onSwitchHostel(hostel) },
                            enabled = !isCurrent && !career.isGraduated,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("relocate_hostel_${hostel.name.lowercase().replace(" ", "_")}"),
                            border = BorderStroke(1.dp, LionGold)
                        ) {
                            Text(
                                text = if (isCurrent) "Current Residence" else "Pay Rent & Move In (₦${hostel.rentNaira})",
                                color = LionGoldBright
                            )
                        }
                    }
                }
            }
        } else {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, LionGold)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = LionGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "OFFICIAL UNN ACADEMIC TRANSCRIPT",
                                style = MaterialTheme.typography.labelMedium,
                                color = LionGoldBright
                            )
                        }
                        Text(
                            text = "${career.studentName} (${career.regNumber})",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "CGPA: ${String.format("%.2f", career.cgpa)} • Standing: $honoursClass",
                            style = MaterialTheme.typography.titleMedium,
                            color = UnnMintAccent
                        )
                        Text(
                            text = "Archetype: ${career.archetype} • Carry-Overs: ${career.carryOvers} • Hostel: ${career.hostel}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Earned UNN Badges (${career.badges.size}):",
                            style = MaterialTheme.typography.labelLarge,
                            color = LionGold
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            career.badges.forEach { badge ->
                                Surface(
                                    color = UnnEmerald,
                                    shape = RoundedCornerShape(50),
                                    border = BorderStroke(1.dp, LionGold.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Stars,
                                            contentDescription = null,
                                            tint = LionGoldBright,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = badge, style = MaterialTheme.typography.labelMedium, color = Color.White)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Hostel Locker Inventory (${career.inventory.size}):",
                            style = MaterialTheme.typography.labelLarge,
                            color = LionGold
                        )
                        Text(
                            text = if (career.inventory.isEmpty()) "No items yet" else career.inventory.joinToString(" • "),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Saved UNN Careers (${allCareers.size})",
                        style = MaterialTheme.typography.titleLarge,
                        color = LionGold
                    )
                    Button(
                        onClick = onStartNewCareer,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LionGold,
                            contentColor = UnnDeepGreen
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("matriculate_new_student_button")
                    ) {
                        Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("New Student", fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(allCareers, key = { it.id }) { itemCareer ->
                val isActive = itemCareer.id == career.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectCareer(itemCareer.id) }
                        .testTag("career_item_${itemCareer.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isActive) UnnEmerald else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    border = if (isActive) BorderStroke(1.5.dp, LionGold) else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${itemCareer.studentName} (${itemCareer.level}L)",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "${itemCareer.department} • CGPA ${String.format("%.2f", itemCareer.cgpa)} • ₦${itemCareer.naira}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (allCareers.size > 1) {
                            IconButton(
                                onClick = { onDeleteCareer(itemCareer.id) },
                                modifier = Modifier.testTag("delete_career_${itemCareer.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Career",
                                    tint = AlertCrimson
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Margaret Ekpo Convocation Hall of Fame",
                    style = MaterialTheme.typography.titleLarge,
                    color = LionGold
                )
                if (hallOfFame.isEmpty()) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Complete 400 Level Semester 2 Exams to induct your first graduating Lion/Lioness into the UNN Hall of Fame!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            items(hallOfFame, key = { it.id }) { alumni ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = BorderStroke(1.dp, LionGold.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = LionGold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = alumni.studentName, style = MaterialTheme.typography.titleMedium)
                            }
                            Text(
                                text = "CGPA ${String.format("%.2f", alumni.finalCgpa)}",
                                style = MaterialTheme.typography.titleMedium,
                                color = LionGold
                            )
                        }
                        Text(
                            text = "${alumni.degreeClass} • ${alumni.department}",
                            style = MaterialTheme.typography.labelLarge,
                            color = UnnMintAccent
                        )
                        Text(
                            text = alumni.summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = onSignOut,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("sign_out_button"),
                    border = BorderStroke(1.dp, AlertCrimson)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = AlertCrimson)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign Out of UNN Portal", color = AlertCrimson)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ShenaniganDilemmaDialog(
    event: CampusShenaniganEvent,
    onDismiss: () -> Unit,
    onSelectChoice: (ShenaniganChoice) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Column {
                Text(
                    text = "📍 ${event.locationTag}",
                    style = MaterialTheme.typography.labelMedium,
                    color = LionGold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                event.choices.forEachIndexed { idx, choice ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectChoice(choice) }
                            .testTag("shenanigan_choice_$idx"),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, LionGold.copy(alpha = 0.55f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = choice.label,
                                style = MaterialTheme.typography.titleMedium,
                                color = LionGoldBright
                            )
                            val deltaSummary = buildList {
                                if (choice.nairaDelta != 0L) {
                                    add(if (choice.nairaDelta > 0) "+₦${choice.nairaDelta}" else "-₦${-choice.nairaDelta}")
                                }
                                if (choice.prepDelta != 0) add("Prep ${if (choice.prepDelta > 0) "+" else ""}${choice.prepDelta}")
                                if (choice.energyDelta != 0) add("Energy ${if (choice.energyDelta > 0) "+" else ""}${choice.energyDelta}")
                                if (choice.credDelta != 0) add("Cred ${if (choice.credDelta > 0) "+" else ""}${choice.credDelta}")
                                if (choice.cloutDelta != 0) add("Clout ${if (choice.cloutDelta > 0) "+" else ""}${choice.cloutDelta}")
                            }.joinToString(" • ")
                            if (deltaSummary.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = deltaSummary,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = UnnMintAccent
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("close_shenanigan_dialog_button")
            ) {
                Text("Later", color = LionGold)
            }
        }
    )
}

@Composable
private fun HostelNightShenaniganDialog(
    event: HostelNightEvent,
    hostelName: String,
    inventory: List<String>,
    onDismiss: () -> Unit,
    onSelectChoice: (ShenaniganChoice) -> Unit
) {
    val gearSynergy = remember(event, inventory) {
        HostelNightShenanigansGenerator.evaluateGearBonus(event, inventory)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = SanityViolet.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, SanityViolet)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bedtime,
                                contentDescription = null,
                                tint = SanityViolet,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${event.timestampLabel} • ${event.scenarioType.label}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White
                            )
                        }
                    }
                }
                Text(
                    text = "🏨 $hostelName",
                    style = MaterialTheme.typography.labelLarge,
                    color = LionGoldBright
                )
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (event.gearBonusDescription != null) {
                    Surface(
                        color = if (gearSynergy.isActive) UnnEmerald else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            if (gearSynergy.isActive) UnnMintAccent else LionGold.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (gearSynergy.isActive) {
                                "✅ Locker Gear Active (${gearSynergy.matchedItemName}): ${event.gearBonusDescription}"
                            } else {
                                "💡 Tip: Own '${event.helpfulGearKeyword}' from Odim Market for bonus stats (${event.gearBonusDescription})"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = if (gearSynergy.isActive) UnnMintAccent else LionGoldBright,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))
                event.choices.forEachIndexed { idx, choice ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectChoice(choice) }
                            .testTag("night_shenanigan_choice_$idx"),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, SanityViolet.copy(alpha = 0.7f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = choice.label,
                                style = MaterialTheme.typography.titleMedium,
                                color = LionGoldBright
                            )
                            val deltaSummary = buildList {
                                if (choice.nairaDelta != 0L) {
                                    add(if (choice.nairaDelta > 0) "+₦${choice.nairaDelta}" else "-₦${-choice.nairaDelta}")
                                }
                                if (choice.energyDelta != 0) add("Energy ${if (choice.energyDelta > 0) "+" else ""}${choice.energyDelta}")
                                if (choice.sanityDelta != 0) add("Sanity ${if (choice.sanityDelta > 0) "+" else ""}${choice.sanityDelta}")
                                if (choice.prepDelta != 0) add("Prep ${if (choice.prepDelta > 0) "+" else ""}${choice.prepDelta}")
                                if (choice.credDelta != 0) add("Cred ${if (choice.credDelta > 0) "+" else ""}${choice.credDelta}")
                                if (choice.cloutDelta != 0) add("Clout ${if (choice.cloutDelta > 0) "+" else ""}${choice.cloutDelta}")
                                if (choice.badgeAwarded != null) add("🏅 ${choice.badgeAwarded}")
                            }.joinToString(" • ")
                            if (deltaSummary.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = deltaSummary,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = UnnMintAccent
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("close_night_shenanigan_dialog_button")
            ) {
                Text("Sleep Through It", color = LionGold)
            }
        }
    )
}

