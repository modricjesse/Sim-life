package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.auth.authStateFlow
import com.example.auth.signOut
import com.example.data.LionDenRepository
import com.example.data.UiState
import com.example.ui.screens.AdmissionPortalScreen
import com.example.ui.screens.AuthGateScreen
import com.example.ui.screens.LionDenGameScreen
import com.example.ui.theme.LionGold
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.LionDenViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LionDenAppNavigation()
                }
            }
        }
    }
}

@Composable
fun LionDenAppNavigation() {
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUser by auth.authStateFlow().collectAsStateWithLifecycle(initialValue = auth.currentUser)
    val user = currentUser

    if (user == null) {
        AuthGateScreen(
            onAuthSuccess = { /* authStateFlow automatically updates currentUser */ }
        )
    } else {
        AuthenticatedLionDenSession(currentUserId = user.uid)
    }
}

@Composable
private fun AuthenticatedLionDenSession(currentUserId: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }

    val viewModel: LionDenViewModel = viewModel(
        key = currentUserId,
        factory = viewModelFactory {
            initializer {
                val app = checkNotNull(this[APPLICATION_KEY]) {
                    "APPLICATION_KEY missing from CreationExtras"
                }
                val databaseId = app.getString(R.string.firestore_database_id)
                val db = FirebaseFirestore.getInstance(databaseId)
                LionDenViewModel(LionDenRepository(db), currentUserId)
            }
        }
    )

    val careersState by viewModel.careersState.collectAsStateWithLifecycle()
    val hallOfFameState by viewModel.hallOfFameState.collectAsStateWithLifecycle()
    val selectedCareerId by viewModel.selectedCareerId.collectAsStateWithLifecycle()
    val statusBanner by viewModel.statusBannerMessage.collectAsStateWithLifecycle()
    val activeEventModal by viewModel.activeEventModal.collectAsStateWithLifecycle()
    val activeNightEventModal by viewModel.activeNightEventModal.collectAsStateWithLifecycle()

    var showMatriculationPortal by rememberSaveable { mutableStateOf(false) }

    val hallOfFameList = (hallOfFameState as? UiState.Success)?.data ?: emptyList()

    when (val state = careersState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = LionGold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Loading UNN Student Portal...",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        is UiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Portal Connection Issue",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Button(
                        onClick = {
                            signOut(
                                credentialManager = credentialManager,
                                onSignOutComplete = {},
                                scope = scope
                            )
                        }
                    ) {
                        Text("Sign Out & Retry")
                    }
                }
            }
        }

        is UiState.Success -> {
            val careers = state.data
            val activeCareer = careers.firstOrNull { it.id == selectedCareerId } ?: careers.firstOrNull()

            if (activeCareer == null || showMatriculationPortal) {
                AdmissionPortalScreen(
                    canGoBack = activeCareer != null,
                    onBack = { showMatriculationPortal = false },
                    onMatriculate = { name, dept, hostel, archetype ->
                        viewModel.matriculateNewStudent(
                            studentName = name,
                            departmentOpt = dept,
                            hostelOpt = hostel,
                            archetypeOpt = archetype,
                            onCreated = {
                                showMatriculationPortal = false
                            }
                        )
                    }
                )
            } else {
                LionDenGameScreen(
                    career = activeCareer,
                    allCareers = careers,
                    hallOfFame = hallOfFameList,
                    statusBanner = statusBanner,
                    activeEventModal = activeEventModal,
                    activeNightEventModal = activeNightEventModal,
                    onDismissBanner = viewModel::dismissBanner,
                    onTriggerShenanigan = { viewModel.triggerWeeklyShenanigan(activeCareer) },
                    onDismissShenanigan = viewModel::dismissEventModal,
                    onResolveShenanigan = { event, choice ->
                        viewModel.resolveShenaniganChoice(activeCareer, event, choice)
                    },
                    onTriggerHostelNightRest = {
                        viewModel.triggerHostelNightRest(activeCareer)
                    },
                    onDismissNightShenanigan = viewModel::dismissNightEventModal,
                    onResolveNightShenanigan = { nightEvent, choice ->
                        viewModel.resolveHostelNightShenanigan(activeCareer, nightEvent, choice)
                    },
                    onVibeToAfrobeatsTrack = { track ->
                        viewModel.vibeToAfrobeatsTrack(activeCareer, track)
                    },
                    onPerformSpotAction = { action ->
                        viewModel.performSpotAction(activeCareer, action)
                    },
                    onNavigateMapTrek = { landmark ->
                        viewModel.navigateMapByTrek(activeCareer, landmark)
                    },
                    onNavigateMapShuttle = { landmark ->
                        viewModel.navigateMapByShuttle(activeCareer, landmark)
                    },
                    onExecuteLandmarkAction = { landmark ->
                        viewModel.executeLandmarkActivity(activeCareer, landmark)
                    },
                    onToggleCourseRegistration = { code ->
                        viewModel.toggleCourseRegistration(activeCareer, code)
                    },
                    onAttendCourseLecture = { course ->
                        viewModel.attendCourseLecture(activeCareer, course)
                    },
                    onStudyAtJpClarkLibrary = {
                        viewModel.studyAtJohnPepperClarkLibrary(activeCareer)
                    },
                    onSitMidTermExams = {
                        viewModel.sitMidTermContinuousAssessment(activeCareer)
                    },
                    onSitFinalSemesterExams = {
                        viewModel.sitFinalSemesterExaminations(activeCareer)
                    },
                    onInteractNpc = { npcState, mode ->
                        viewModel.interactWithNpc(activeCareer, npcState, mode)
                    },
                    onJoinOrAttendClub = { club ->
                        viewModel.joinOrAttendClub(activeCareer, club)
                    },
                    onSelectClique = { clique ->
                        viewModel.selectSocialClique(activeCareer, clique)
                    },
                    onAttendSocialEvent = { event ->
                        viewModel.attendCampusSocialEvent(activeCareer, event)
                    },
                    onBuyShopItem = { item ->
                        viewModel.buyShopItem(activeCareer, item)
                    },
                    onSwitchHostel = { hostel ->
                        viewModel.switchHostel(activeCareer, hostel)
                    },
                    onCampaignOffice = { office ->
                        viewModel.campaignForOffice(activeCareer, office)
                    },
                    onSelectCareer = { id ->
                        viewModel.selectCareer(id)
                    },
                    onStartNewCareer = {
                        showMatriculationPortal = true
                    },
                    onDeleteCareer = { id ->
                        viewModel.deleteCareer(id)
                    },
                    onSignOut = {
                        signOut(
                            credentialManager = credentialManager,
                            onSignOutComplete = {},
                            scope = scope
                        )
                    }
                )
            }
        }
    }
}
