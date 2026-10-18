package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.example.auth.attemptAutoSignIn
import com.example.auth.onGoogleSignInClicked
import com.example.data.UnnCampusCatalog
import com.example.ui.components.LionCrestBadge
import com.example.ui.components.UnnCampusHeroBanner
import com.example.ui.theme.LionGold
import com.example.ui.theme.LionGoldBright
import com.example.ui.theme.UnnDeepGreen
import com.example.ui.theme.UnnEmerald
import com.example.ui.theme.UnnForestGreen

@Composable
fun AuthGateScreen(
    onAuthSuccess: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }
    var isSigningIn by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        attemptAutoSignIn(
            context = context,
            credentialManager = credentialManager,
            onAuthSuccess = onAuthSuccess,
            onUnauthenticated = { /* Stay on interactive sign-in screen */ },
            scope = scope
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                LionCrestBadge(modifier = Modifier.size(76.dp))

                Text(
                    text = "LION DEN SIM",
                    style = MaterialTheme.typography.displayMedium,
                    color = LionGold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "University of Nigeria (UNN & UNEC) Life Simulator\nTo Restore the Dignity of Man",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                UnnCampusHeroBanner(
                    title = "Enter the Den of Lions",
                    subtitle = "Navigate GS 101 CBT stampedes, Zik's Flats bucket queues, Carver Night Class, SUG Freedom Square rallies, and Odim hustles.",
                    badgeText = "NSUKKA & ENUGU CAMPUSES"
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
                    ),
                    border = BorderStroke(1.dp, LionGold.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "UNN Student Portal Cloud Sync",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Sign in with Google to save your UNN registration number, CGPA transcript, SUG political offices, and Hall of Fame convocation records across devices.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        if (errorMessage != null) {
                            Surface(
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.16f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = errorMessage!!,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(12.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Button(
                            onClick = {
                                isSigningIn = true
                                errorMessage = null
                                onGoogleSignInClicked(
                                    context = context,
                                    credentialManager = credentialManager,
                                    onAuthSuccess = {
                                        isSigningIn = false
                                        onAuthSuccess()
                                    },
                                    onAuthError = { msg ->
                                        isSigningIn = false
                                        errorMessage = msg
                                    },
                                    scope = scope,
                                    onAuthCancelled = {
                                        isSigningIn = false
                                    }
                                )
                            },
                            enabled = !isSigningIn,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("google_sign_in_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LionGold,
                                contentColor = UnnDeepGreen
                            )
                        ) {
                            if (isSigningIn) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = UnnDeepGreen,
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = "Sign in with Google",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Sign in with Google",
                                    style = MaterialTheme.typography.titleMedium,
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdmissionPortalScreen(
    canGoBack: Boolean,
    onBack: () -> Unit,
    onMatriculate: (
        studentName: String,
        dept: com.example.data.UnnDepartmentOption,
        hostel: com.example.data.UnnHostelOption,
        archetype: com.example.data.UnnArchetypeOption
    ) -> Unit
) {
    if (canGoBack) {
        BackHandler { onBack() }
    }

    var studentName by remember { mutableStateOf("Chukwudi Okafor") }
    var selectedCampus by remember { mutableStateOf("Nsukka") }
    val filteredDepts = remember(selectedCampus) {
        UnnCampusCatalog.departments.filter { it.campus == selectedCampus }
    }
    var selectedDept by remember(selectedCampus) { mutableStateOf(filteredDepts.first()) }
    val filteredHostels = remember(selectedCampus) {
        UnnCampusCatalog.hostels.filter { it.campus == selectedCampus }
    }
    var selectedHostel by remember(selectedCampus) { mutableStateOf(filteredHostels.first()) }
    var selectedArchetype by remember { mutableStateOf(UnnCampusCatalog.archetypes.first()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (canGoBack) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("admission_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to Career"
                            )
                        }
                    }
                    Column {
                        Text(
                            text = "UNN Matriculation Portal",
                            style = MaterialTheme.typography.headlineLarge,
                            color = LionGold
                        )
                        Text(
                            text = "Ekpo Ref Biometrics & Hostel Allocation",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                LionCrestBadge(modifier = Modifier.size(48.dp))
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = LionGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "1. Lion / Lioness Full Name",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    OutlinedTextField(
                        value = studentName,
                        onValueChange = { studentName = it },
                        label = { Text("Full Name on JAMB Admission Letter") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("student_name_input")
                    )

                    Text(
                        text = "Select Campus:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        listOf("Nsukka", "Enugu").forEach { campus ->
                            FilterChip(
                                selected = selectedCampus == campus,
                                onClick = { selectedCampus = campus },
                                label = {
                                    Text(if (campus == "Nsukka") "UNN Nsukka (The Den)" else "UNEC Enugu Campus")
                                },
                                leadingIcon = if (selectedCampus == campus) {
                                    { Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp)) }
                                } else null,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = LionGold,
                                    selectedLabelColor = UnnDeepGreen,
                                    selectedLeadingIconColor = UnnDeepGreen
                                ),
                                modifier = Modifier.testTag("campus_chip_${campus.lowercase()}")
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountBalance, contentDescription = null, tint = LionGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "2. Faculty & Department",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    filteredDepts.forEach { dept ->
                        val isSelected = dept.department == selectedDept.department
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedDept = dept }
                                .testTag("dept_card_${dept.department.lowercase().replace(" ", "_")}"),
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) UnnEmerald else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) LionGold else Color.Transparent
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${dept.department} (${dept.faculty})",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (isSelected) LionGoldBright else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = dept.signatureCourse,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = LionGold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${dept.cutoffDifficulty} • ${dept.perkDescription}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = LionGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "3. Hall of Residence / Off-Campus Lodge",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    filteredHostels.forEach { hostel ->
                        val isSelected = hostel.name == selectedHostel.name
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedHostel = hostel }
                                .testTag("hostel_card_${hostel.name.lowercase().replace(" ", "_")}"),
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) UnnEmerald else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) LionGold else Color.Transparent
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = hostel.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (isSelected) LionGoldBright else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "₦${hostel.rentNaira} / session",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = LionGold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = hostel.vibe,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Stars, contentDescription = null, tint = LionGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "4. Student Persona / Background",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        UnnCampusCatalog.archetypes.forEach { arch ->
                            val isSelected = arch.title == selectedArchetype.title
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedArchetype = arch }
                                    .testTag("archetype_card_${arch.title.lowercase().replace(" ", "_")}"),
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) UnnEmerald else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) LionGold else Color.Transparent
                                )
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = arch.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = if (isSelected) LionGoldBright else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "₦${arch.startingNaira} Allowance",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = LionGold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = arch.subtitle,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Starter Gear: ${arch.startingItem} • Prep ${arch.startingPrep} • Cred ${arch.startingCred} • Clout ${arch.startingClout}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = LionGoldBright
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    onMatriculate(studentName, selectedDept, selectedHostel, selectedArchetype)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .testTag("confirm_matriculation_button"),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LionGold,
                    contentColor = UnnDeepGreen
                )
            ) {
                Icon(Icons.Default.School, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Sign Matriculation Oath & Enter the Den",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
