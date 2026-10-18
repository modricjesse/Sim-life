package com.example.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AcademicStanding
import com.example.data.AfrobeatsTrack
import com.example.data.AlumniRecord
import com.example.data.CampusRelationshipNpc
import com.example.data.CampusShenaniganEvent
import com.example.data.CampusSocialEvent
import com.example.data.HostelNightEvent
import com.example.data.HostelNightShenanigansGenerator
import com.example.data.LionDenRepository
import com.example.data.NpcRelationshipState
import com.example.data.RelationshipType
import com.example.data.ShenaniganChoice
import com.example.data.SocialClique
import com.example.data.StudentCareer
import com.example.data.StudentClub
import com.example.data.UiState
import com.example.data.UnnAcademicSystem
import com.example.data.UnnArchetypeOption
import com.example.data.UnnCampusCatalog
import com.example.data.UnnCourseProgress
import com.example.data.UnnDepartmentOption
import com.example.data.UnnHostelOption
import com.example.data.UnnInteractiveMapData
import com.example.data.UnnMapLandmark
import com.example.data.UnnPoliticalOffice
import com.example.data.UnnShenanigansEngine
import com.example.data.UnnShopItem
import com.example.data.UnnSocialAndClubsData
import com.example.data.UnnSpotAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

class LionDenViewModel(
    private val repository: LionDenRepository,
    private val currentUserId: String
) : ViewModel() {

    val careersState: StateFlow<UiState<List<StudentCareer>>> = repository.observeCareers(currentUserId)
        .map<List<StudentCareer>, UiState<List<StudentCareer>>> { list ->
            val currentSelectedId = _selectedCareerId.value
            if (currentSelectedId == null && list.isNotEmpty()) {
                _selectedCareerId.value = list.first().id
            } else if (currentSelectedId != null && list.none { it.id == currentSelectedId }) {
                _selectedCareerId.value = list.firstOrNull()?.id
            }
            UiState.Success(list)
        }
        .catch { error ->
            Log.w(TAG, "Error observing student careers", error)
            emit(UiState.Error(error.message ?: "Failed to load student careers"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = UiState.Loading
        )

    val hallOfFameState: StateFlow<UiState<List<AlumniRecord>>> = repository.observeHallOfFame(currentUserId)
        .map<List<AlumniRecord>, UiState<List<AlumniRecord>>> { UiState.Success(it) }
        .catch { error ->
            Log.w(TAG, "Error observing hall of fame", error)
            emit(UiState.Error(error.message ?: "Failed to load Hall of Fame"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = UiState.Loading
        )

    private val _selectedCareerId = MutableStateFlow<String?>(null)
    val selectedCareerId: StateFlow<String?> = _selectedCareerId.asStateFlow()

    private val _statusBannerMessage = MutableStateFlow<String?>(null)
    val statusBannerMessage: StateFlow<String?> = _statusBannerMessage.asStateFlow()

    private val _activeEventModal = MutableStateFlow<CampusShenaniganEvent?>(null)
    val activeEventModal: StateFlow<CampusShenaniganEvent?> = _activeEventModal.asStateFlow()

    private val _activeNightEventModal = MutableStateFlow<HostelNightEvent?>(null)
    val activeNightEventModal: StateFlow<HostelNightEvent?> = _activeNightEventModal.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    fun selectCareer(careerId: String?) {
        _selectedCareerId.value = careerId
    }

    fun dismissBanner() {
        _statusBannerMessage.value = null
    }

    fun triggerWeeklyShenanigan(career: StudentCareer) {
        _activeEventModal.value = UnnShenanigansEngine.pickEventForWeek(
            level = career.level,
            semester = career.semester,
            week = career.week
        )
    }

    fun dismissEventModal() {
        _activeEventModal.value = null
    }

    fun dismissNightEventModal() {
        _activeNightEventModal.value = null
    }

    fun triggerHostelNightRest(career: StudentCareer) {
        if (career.isGraduated) {
            _statusBannerMessage.value = "You have already graduated! Start a new career or view Hall of Fame."
            return
        }
        val restoredEnergy = (career.energy + 22L).coerceIn(0L, 100L)
        val restoredSanity = (career.sanity + 8L).coerceIn(0L, 100L)
        val restLog = "L${career.level} S${career.semester} W${career.week}: Turned in for the night at ${career.hostel} (+22 Energy, +8 Sanity)."
        val updatedCareer = career.copy(
            energy = restoredEnergy,
            sanity = restoredSanity,
            recentLogs = (listOf(restLog) + career.recentLogs).take(30)
        )
        val nightEvent = HostelNightShenanigansGenerator.rollNightEvent(
            hostelName = career.hostel,
            recentLogs = career.recentLogs
        )
        _activeNightEventModal.value = nightEvent
        saveCareerUpdate(updatedCareer, "Resting at ${career.hostel}... Midnight Hostel Shenanigan triggered!")
    }

    fun resolveHostelNightShenanigan(
        career: StudentCareer,
        event: HostelNightEvent,
        choice: ShenaniganChoice
    ) {
        _activeNightEventModal.value = null
        if (choice.nairaDelta < 0 && career.naira + choice.nairaDelta < 0) {
            _statusBannerMessage.value = "Not enough Naira (Need ₦${-choice.nairaDelta}) for that midnight move! Pick another option."
            return
        }

        val gearSynergy = HostelNightShenanigansGenerator.evaluateGearBonus(event, career.inventory)
        val totalEnergyDelta = choice.energyDelta + gearSynergy.extraEnergy
        val totalSanityDelta = choice.sanityDelta + gearSynergy.extraSanity
        val totalPrepDelta = choice.prepDelta + gearSynergy.extraPrep
        val totalCredDelta = choice.credDelta + gearSynergy.extraCred

        val updatedNaira = (career.naira + choice.nairaDelta).coerceAtLeast(0L)
        val updatedEnergy = (career.energy + totalEnergyDelta).coerceIn(0L, 100L)
        val updatedSanity = (career.sanity + totalSanityDelta).coerceIn(0L, 100L)
        val updatedPrep = (career.academicPrep + totalPrepDelta).coerceIn(0L, 100L)
        val updatedCred = (career.streetCred + totalCredDelta).coerceIn(0L, 100L)
        val updatedClout = (career.politicalClout + choice.cloutDelta).coerceIn(0L, 100L)

        val updatedBadges = if (choice.badgeAwarded != null && !career.badges.contains(choice.badgeAwarded)) {
            (career.badges + choice.badgeAwarded).take(25)
        } else {
            career.badges
        }

        val updatedTier = UnnCampusCatalog.computeSocialTier(
            cred = updatedCred,
            clout = updatedClout,
            cgpa = career.cgpa,
            naira = updatedNaira,
            sugOffice = career.sugOffice
        )

        val gearNote = if (gearSynergy.isActive && gearSynergy.matchedItemName != null) {
            " [Gear Perk: ${gearSynergy.matchedItemName}]"
        } else ""

        val logEntry = "L${career.level} S${career.semester} W${career.week} [NIGHT @ ${career.hostel} ${event.timestampLabel} - ${event.title}]: ${choice.outcomeNarrative}$gearNote"

        val updatedCareer = career.copy(
            naira = updatedNaira,
            energy = updatedEnergy,
            sanity = updatedSanity,
            academicPrep = updatedPrep,
            streetCred = updatedCred,
            politicalClout = updatedClout,
            socialTier = updatedTier,
            badges = updatedBadges,
            recentLogs = (listOf(logEntry) + career.recentLogs).take(30)
        )
        saveCareerUpdate(updatedCareer, "${choice.outcomeNarrative}$gearNote")
    }

    fun vibeToAfrobeatsTrack(career: StudentCareer, track: AfrobeatsTrack) {
        if (career.isGraduated) {
            _statusBannerMessage.value = "You have already graduated! Start a new career or view Hall of Fame."
            return
        }
        val hasSpeakerGear = career.inventory.any {
            it.contains("Speaker", ignoreCase = true) ||
                it.contains("JBL", ignoreCase = true) ||
                it.contains("AirPods", ignoreCase = true) ||
                it.contains("Headphones", ignoreCase = true)
        }
        val extraCred = if (hasSpeakerGear) 4 else 0
        val updatedEnergy = (career.energy + track.energyDelta).coerceIn(0L, 100L)
        val updatedSanity = (career.sanity + track.sanityDelta).coerceIn(0L, 100L)
        val updatedPrep = (career.academicPrep + track.prepDelta).coerceIn(0L, 100L)
        val updatedCred = (career.streetCred + track.credDelta + extraCred).coerceIn(0L, 100L)
        val updatedClout = (career.politicalClout + track.cloutDelta).coerceIn(0L, 100L)

        val afrobeatsBadge = "Lion FM Afrobeats DJ"
        val updatedBadges = if (!career.badges.contains(afrobeatsBadge)) {
            (career.badges + afrobeatsBadge).take(25)
        } else {
            career.badges
        }

        val updatedTier = UnnCampusCatalog.computeSocialTier(
            cred = updatedCred,
            clout = updatedClout,
            cgpa = career.cgpa,
            naira = career.naira,
            sugOffice = career.sugOffice
        )

        val gearTag = if (hasSpeakerGear) " [JBL Speaker Boost +4 Cred]" else ""
        val logEntry = "L${career.level} S${career.semester} W${career.week} [AFROBEATS VIBE]: Blasted '${track.title}' by ${track.artist} (${track.subgenre} • ${track.bpm} BPM) at ${track.campusVibeLocation}$gearTag."

        val updatedCareer = career.copy(
            energy = updatedEnergy,
            sanity = updatedSanity,
            academicPrep = updatedPrep,
            streetCred = updatedCred,
            politicalClout = updatedClout,
            socialTier = updatedTier,
            badges = updatedBadges,
            recentLogs = (listOf(logEntry) + career.recentLogs).take(30)
        )
        saveCareerUpdate(
            updatedCareer,
            "Now Vibing: '${track.title}' by ${track.artist}!$gearTag"
        )
    }

    private fun buildRichMetadataList(
        courses: List<UnnCourseProgress>,
        npcs: List<NpcRelationshipState>,
        clubs: Set<String>,
        cliqueName: String,
        locationId: String
    ): List<String> {
        return buildList {
            add("LOC|$locationId")
            add("CLIQUE|$cliqueName")
            clubs.take(5).forEach { add("CLUB|$it") }
            npcs.take(7).forEach { state ->
                add("REL|${state.npc.id}|${state.type.name}|${state.affinity}")
            }
            courses.take(6).forEach { c ->
                add(UnnAcademicSystem.encodeCourse(c))
            }
        }.take(25)
    }

    fun matriculateNewStudent(
        studentName: String,
        departmentOpt: UnnDepartmentOption,
        hostelOpt: UnnHostelOption,
        archetypeOpt: UnnArchetypeOption,
        onCreated: (String) -> Unit = {}
    ) {
        val trimmedName = studentName.trim().ifEmpty { "Chukwudi Lion" }.take(55)
        val regNum = "2026/${Random.nextInt(240000, 299999)}"
        val deptCloutBonus = if (departmentOpt.department.contains("Political") || departmentOpt.department.contains("Law")) 12L else 0L
        val deptCredBonus = if (departmentOpt.department.contains("Mass Comm") || departmentOpt.department.contains("Law")) 10L else 0L

        val initialCred = (archetypeOpt.startingCred + deptCredBonus).coerceIn(0L, 100L)
        val initialClout = (archetypeOpt.startingClout + deptCloutBonus + hostelOpt.cloutModifier).coerceIn(0L, 100L)
        val initialEnergy = (85 + hostelOpt.energyModifier).coerceIn(20, 100).toLong()
        val initialSanity = (85 + hostelOpt.sanityModifier).coerceIn(20, 100).toLong()
        val initialNaira = (archetypeOpt.startingNaira - (hostelOpt.rentNaira / 3L)).coerceAtLeast(12000L)

        val tier = UnnCampusCatalog.computeSocialTier(
            cred = initialCred,
            clout = initialClout,
            cgpa = 4.50,
            naira = initialNaira,
            sugOffice = "None"
        )

        val startLandmarkId = when {
            hostelOpt.name.contains("Slessor") -> "mary_slessor_hall"
            hostelOpt.name.contains("Okija") || hostelOpt.name.contains("Bello") -> "okija_hall"
            hostelOpt.name.contains("Zik") || hostelOpt.name.contains("Odenigwe") -> "ziks_flats_odenigwe"
            hostelOpt.name.contains("Hilltop") -> "hilltop_second_gate"
            else -> "sub_building"
        }

        val initialCourses = UnnAcademicSystem.defaultCoursesForStudent(
            department = departmentOpt.department,
            level = 100L,
            semester = 1L
        )
        val initialNpcs = UnnSocialAndClubsData.npcs.map {
            NpcRelationshipState(npc = it, type = it.defaultType, affinity = it.defaultAffinity)
        }
        val metadata = buildRichMetadataList(
            courses = initialCourses,
            npcs = initialNpcs,
            clubs = emptySet(),
            cliqueName = "Unaligned Fresher",
            locationId = startLandmarkId
        )

        val newCareer = StudentCareer(
            userId = currentUserId,
            studentName = trimmedName,
            regNumber = regNum,
            campus = departmentOpt.campus,
            faculty = departmentOpt.faculty,
            department = departmentOpt.department,
            hostel = hostelOpt.name,
            archetype = archetypeOpt.title,
            level = 100L,
            semester = 1L,
            week = 1L,
            cgpa = 4.50,
            naira = initialNaira,
            energy = initialEnergy,
            sanity = initialSanity,
            academicPrep = archetypeOpt.startingPrep.coerceIn(10L, 100L),
            streetCred = initialCred,
            politicalClout = initialClout,
            socialTier = tier,
            sugOffice = "None",
            carryOvers = 0L,
            isGraduated = false,
            inventory = listOf(archetypeOpt.startingItem),
            relationships = metadata,
            recentLogs = listOf(
                "L100 S1 W1: Matriculated into ${departmentOpt.department} (${departmentOpt.campus} Campus) with Reg No $regNum!",
                "L100 S1 W1: Checked into ${hostelOpt.name} and registered 4 core semester courses on the UNN Portal."
            ),
            badges = listOf("Matriculated Lion")
        )

        viewModelScope.launch {
            _isSaving.value = true
            val result = repository.createCareer(newCareer)
            _isSaving.value = false
            result.onSuccess { newId ->
                _selectedCareerId.value = newId
                _statusBannerMessage.value = "Welcome to UNN, $trimmedName! Reg No: $regNum"
                onCreated(newId)
            }.onFailure { err ->
                _statusBannerMessage.value = err.localizedMessage ?: "Failed to matriculate student"
            }
        }
    }

    // --- INTERACTIVE CAMPUS MAP NAVIGATION & LANDMARK ACTIONS ---

    fun navigateMapByTrek(career: StudentCareer, destination: UnnMapLandmark) {
        val currentLocId = UnnSocialAndClubsData.parseCurrentLocation(career.relationships)
        val currentLm = UnnInteractiveMapData.findLandmark(currentLocId)
        if (currentLm.id == destination.id) {
            _statusBannerMessage.value = "You are already at ${destination.name}!"
            return
        }
        val trekEnergy = UnnInteractiveMapData.calculateTrekEnergyCost(currentLm, destination)
        if (career.energy < trekEnergy) {
            _statusBannerMessage.value = "Too tired to trek (${trekEnergy} Energy needed)! Take a ₦200 Campus Shuttle or rest."
            return
        }

        val courses = UnnAcademicSystem.decodeCourses(career.relationships, career.department, career.level, career.semester)
        val npcs = UnnSocialAndClubsData.parseNpcStates(career.relationships)
        val clubs = UnnSocialAndClubsData.parseJoinedClubs(career.relationships)
        val clique = UnnSocialAndClubsData.parseActiveClique(career.relationships)

        val updatedMeta = buildRichMetadataList(courses, npcs, clubs, clique, destination.id)
        val dist = UnnInteractiveMapData.calculateDistanceMeters(currentLm, destination)
        val log = "L${career.level} S${career.semester} W${career.week}: Trekked ${dist}m from ${currentLm.shortLabel} to ${destination.name}."

        val updatedCareer = career.copy(
            energy = (career.energy - trekEnergy).coerceAtLeast(0L),
            relationships = updatedMeta,
            recentLogs = (listOf(log) + career.recentLogs).take(30)
        )
        saveCareerUpdate(updatedCareer, "Trekked to ${destination.shortLabel} (-$trekEnergy Energy)")
    }

    fun navigateMapByShuttle(career: StudentCareer, destination: UnnMapLandmark) {
        val currentLocId = UnnSocialAndClubsData.parseCurrentLocation(career.relationships)
        val currentLm = UnnInteractiveMapData.findLandmark(currentLocId)
        if (currentLm.id == destination.id) {
            _statusBannerMessage.value = "You are already at ${destination.name}!"
            return
        }
        val fare = UnnInteractiveMapData.SHUTTLE_FARE_NAIRA
        if (career.naira < fare) {
            _statusBannerMessage.value = "Need ₦$fare for the UNN Campus Shuttle! Trek instead."
            return
        }

        val courses = UnnAcademicSystem.decodeCourses(career.relationships, career.department, career.level, career.semester)
        val npcs = UnnSocialAndClubsData.parseNpcStates(career.relationships)
        val clubs = UnnSocialAndClubsData.parseJoinedClubs(career.relationships)
        val clique = UnnSocialAndClubsData.parseActiveClique(career.relationships)

        val updatedMeta = buildRichMetadataList(courses, npcs, clubs, clique, destination.id)
        val log = "L${career.level} S${career.semester} W${career.week}: Hopped a ₦$fare Campus Shuttle to ${destination.name}."

        val updatedCareer = career.copy(
            naira = career.naira - fare,
            relationships = updatedMeta,
            recentLogs = (listOf(log) + career.recentLogs).take(30)
        )
        saveCareerUpdate(updatedCareer, "Arrived at ${destination.shortLabel} by Shuttle (-₦$fare)")
    }

    fun executeLandmarkActivity(career: StudentCareer, landmark: UnnMapLandmark) {
        if (career.isGraduated) return
        if (landmark.costNaira > 0 && career.naira < landmark.costNaira) {
            _statusBannerMessage.value = "Need ₦${landmark.costNaira} for ${landmark.primaryActionLabel}!"
            return
        }
        if (landmark.energyDelta < 0 && career.energy < 10L) {
            _statusBannerMessage.value = "Low Energy! Visit Mary Slessor, Okija, Zik's Flats, or SUB Okpa stand to recharge."
            return
        }

        val courses = UnnAcademicSystem.decodeCourses(career.relationships, career.department, career.level, career.semester)
        val npcs = UnnSocialAndClubsData.parseNpcStates(career.relationships)
        val clubs = UnnSocialAndClubsData.parseJoinedClubs(career.relationships)
        val clique = UnnSocialAndClubsData.parseActiveClique(career.relationships)

        // If this landmark is a Lecture Theater or Library, also boost matching courses!
        val updatedCourses = courses.map { c ->
            when {
                !c.isRegistered -> c
                landmark.primaryActionCategory == "LECTURE" && (c.venueId == landmark.id || landmark.id == "faculty_social_sciences") -> {
                    c.copy(
                        classesAttended = (c.classesAttended + 1).coerceAtMost(6),
                        mastery = (c.mastery + 12).coerceAtMost(100)
                    )
                }
                landmark.primaryActionCategory == "STUDY" -> {
                    c.copy(mastery = (c.mastery + 10).coerceAtMost(100))
                }
                else -> c
            }
        }

        val updatedMeta = buildRichMetadataList(updatedCourses, npcs, clubs, clique, landmark.id)
        val log = "L${career.level} S${career.semester} W${career.week} [@${landmark.shortLabel}]: ${landmark.primaryActionLabel}."

        val baseUpdated = career.copy(
            naira = (career.naira - landmark.costNaira).coerceAtLeast(0L),
            energy = (career.energy + landmark.energyDelta).coerceIn(0L, 100L),
            sanity = (career.sanity + landmark.sanityDelta).coerceIn(0L, 100L),
            academicPrep = (career.academicPrep + landmark.prepDelta).coerceIn(0L, 100L),
            streetCred = (career.streetCred + landmark.credDelta).coerceIn(0L, 100L),
            politicalClout = (career.politicalClout + landmark.cloutDelta).coerceIn(0L, 100L),
            relationships = updatedMeta
        )

        val finalCareer = advanceClockOrPersist(baseUpdated, log, advanceWeek = true)
        saveCareerUpdate(finalCareer, "${landmark.primaryActionLabel} at ${landmark.shortLabel}!")
    }

    // --- ACADEMIC PROGRESSION: COURSE REGISTRATION, LECTURES, MID-TERMS & EXAMS ---

    fun toggleCourseRegistration(career: StudentCareer, courseCode: String) {
        val courses = UnnAcademicSystem.decodeCourses(career.relationships, career.department, career.level, career.semester)
        val target = courses.firstOrNull { it.code == courseCode } ?: return

        val standing = UnnAcademicSystem.evaluateAcademicStanding(career.cgpa)
        val maxUnits = if (standing == AcademicStanding.ACADEMIC_PROBATION) 15 else 21
        val currentUnits = courses.filter { it.isRegistered }.sumOf { it.creditUnits }

        if (!target.isRegistered && currentUnits + target.creditUnits > maxUnits) {
            _statusBannerMessage.value = "Credit load cap ($maxUnits Units) reached! Drop an elective first."
            return
        }
        if (target.isRegistered && courses.count { it.isRegistered } <= 2) {
            _statusBannerMessage.value = "You must stay registered for at least 2 courses per semester!"
            return
        }

        val updatedCourses = courses.map {
            if (it.code == courseCode) it.copy(isRegistered = !it.isRegistered) else it
        }
        val npcs = UnnSocialAndClubsData.parseNpcStates(career.relationships)
        val clubs = UnnSocialAndClubsData.parseJoinedClubs(career.relationships)
        val clique = UnnSocialAndClubsData.parseActiveClique(career.relationships)
        val loc = UnnSocialAndClubsData.parseCurrentLocation(career.relationships)

        val updatedMeta = buildRichMetadataList(updatedCourses, npcs, clubs, clique, loc)
        val actionWord = if (!target.isRegistered) "Registered" else "Dropped"
        val updatedCareer = career.copy(
            relationships = updatedMeta,
            recentLogs = (listOf("L${career.level} S${career.semester} W${career.week}: $actionWord course $courseCode (${target.creditUnits} Units) on UNN Portal.") + career.recentLogs).take(30)
        )
        saveCareerUpdate(updatedCareer, "$actionWord $courseCode (${target.creditUnits} Units)")
    }

    fun attendCourseLecture(career: StudentCareer, course: UnnCourseProgress) {
        if (career.energy < 12L) {
            _statusBannerMessage.value = "Too exhausted for lecture! Grab hot Okpa at SUB or rest in your hostel."
            return
        }
        val courses = UnnAcademicSystem.decodeCourses(career.relationships, career.department, career.level, career.semester)
        val updatedCourses = courses.map {
            if (it.code == course.code) {
                it.copy(
                    classesAttended = (it.classesAttended + 1).coerceAtMost(6),
                    mastery = (it.mastery + 15).coerceAtMost(100)
                )
            } else it
        }
        val npcs = UnnSocialAndClubsData.parseNpcStates(career.relationships)
        val clubs = UnnSocialAndClubsData.parseJoinedClubs(career.relationships)
        val clique = UnnSocialAndClubsData.parseActiveClique(career.relationships)

        val updatedMeta = buildRichMetadataList(updatedCourses, npcs, clubs, clique, course.venueId)
        val newAttended = (course.classesAttended + 1).coerceAtMost(6)
        val log = "L${career.level} S${career.semester} W${career.week}: Attended ${course.code} lecture at ${course.venueName} (Attendance: $newAttended/${course.requiredClasses})."

        val baseUpdated = career.copy(
            energy = (career.energy - 14L).coerceAtLeast(0L),
            academicPrep = (career.academicPrep + 10L).coerceIn(0L, 100L),
            relationships = updatedMeta
        )
        val finalCareer = advanceClockOrPersist(baseUpdated, log, advanceWeek = false)
        saveCareerUpdate(finalCareer, "Attended ${course.code} at ${course.venueName} ($newAttended/${course.requiredClasses})")
    }

    fun studyAtJohnPepperClarkLibrary(career: StudentCareer) {
        if (career.energy < 14L) {
            _statusBannerMessage.value = "Too tired to study! Rest at your hostel or visit SUB first."
            return
        }
        val courses = UnnAcademicSystem.decodeCourses(career.relationships, career.department, career.level, career.semester)
        val clique = UnnSocialAndClubsData.parseActiveClique(career.relationships)
        val efiweBonus = if (clique.contains("Efiwe")) 6 else 0

        val updatedCourses = courses.map {
            if (it.isRegistered) it.copy(mastery = (it.mastery + 12 + efiweBonus).coerceAtMost(100)) else it
        }
        val npcs = UnnSocialAndClubsData.parseNpcStates(career.relationships)
        val clubs = UnnSocialAndClubsData.parseJoinedClubs(career.relationships)

        val updatedMeta = buildRichMetadataList(updatedCourses, npcs, clubs, clique, "john_pepper_clark_lib")
        val log = "L${career.level} S${career.semester} W${career.week}: Deep revision session at John Pepper Clark Library (+${12 + efiweBonus}% Mastery on all registered courses)."

        val updatedCareer = career.copy(
            energy = (career.energy - 14L).coerceAtLeast(0L),
            sanity = (career.sanity + 4L).coerceIn(0L, 100L),
            academicPrep = (career.academicPrep + 16L).coerceIn(0L, 100L),
            relationships = updatedMeta,
            recentLogs = (listOf(log) + career.recentLogs).take(30)
        )
        saveCareerUpdate(updatedCareer, "Studied at John Pepper Clark Library (+Mastery)!")
    }

    fun sitMidTermContinuousAssessment(career: StudentCareer) {
        if (career.energy < 10L) {
            _statusBannerMessage.value = "Rest up before sitting your Mid-Term Continuous Assessment!"
            return
        }
        val courses = UnnAcademicSystem.decodeCourses(career.relationships, career.department, career.level, career.semester)
        val updatedCourses = courses.map { c ->
            if (c.isRegistered) {
                val attendanceBoost = (c.classesAttended * 2).coerceAtMost(6)
                val score30 = ((c.mastery * 0.22f) + (career.academicPrep * 0.06f) + attendanceBoost)
                    .roundToInt()
                    .coerceIn(10, 30)
                c.copy(midTermCaScore = score30, midTermTaken = true)
            } else c
        }

        val avgCa = updatedCourses.filter { it.isRegistered }.map { it.midTermCaScore }.average().roundToInt()
        val npcs = UnnSocialAndClubsData.parseNpcStates(career.relationships)
        val clubs = UnnSocialAndClubsData.parseJoinedClubs(career.relationships)
        val clique = UnnSocialAndClubsData.parseActiveClique(career.relationships)
        val loc = UnnSocialAndClubsData.parseCurrentLocation(career.relationships)

        val updatedMeta = buildRichMetadataList(updatedCourses, npcs, clubs, clique, loc)
        val log = "L${career.level} S${career.semester} W${career.week}: Completed Mid-Term Continuous Assessments (Avg CA: $avgCa/30)!"

        val updatedCareer = career.copy(
            energy = (career.energy - 12L).coerceAtLeast(0L),
            academicPrep = (career.academicPrep + 8L).coerceIn(0L, 100L),
            relationships = updatedMeta,
            recentLogs = (listOf(log) + career.recentLogs).take(30)
        )
        saveCareerUpdate(updatedCareer, "Mid-Term CA Results In! Average Score: $avgCa/30")
    }

    fun sitFinalSemesterExaminations(career: StudentCareer) {
        val advanced = executeFullSemesterExamination(career, "Sat Final Semester Examinations at Carver & Library CBT Halls.")
        saveCareerUpdate(advanced, "Semester Exam Results Posted on Noticeboard! New CGPA: ${String.format("%.2f", advanced.cgpa)}")
    }

    // --- SOCIAL LIFE, CLUBS, CLIQUES, EVENTS & RELATIONSHIPS ---

    fun interactWithNpc(career: StudentCareer, targetState: NpcRelationshipState, mode: String) {
        val cost = if (mode == "ROMANCE") 2000L else 0L
        if (cost > 0 && career.naira < cost) {
            _statusBannerMessage.value = "Need ₦$cost for a Bush Bar / SUB Shawarma date with ${targetState.npc.name}!"
            return
        }

        val courses = UnnAcademicSystem.decodeCourses(career.relationships, career.department, career.level, career.semester)
        val currentNpcs = UnnSocialAndClubsData.parseNpcStates(career.relationships)
        val clubs = UnnSocialAndClubsData.parseJoinedClubs(career.relationships)
        val clique = UnnSocialAndClubsData.parseActiveClique(career.relationships)
        val loc = UnnSocialAndClubsData.parseCurrentLocation(career.relationships)

        var prepBoost = 0L
        var credBoost = 0L
        var cloutBoost = 0L
        var sanityBoost = 0L
        var narrative = ""

        val updatedNpcs = currentNpcs.map { item ->
            if (item.npc.id != targetState.npc.id) item
            else {
                when (mode) {
                    "ROMANCE" -> {
                        val newScore = (item.affinity + 20).coerceIn(-100, 100)
                        sanityBoost = 14L
                        credBoost = 10L
                        narrative = "Romantic Shawarma & Acoustic date with ${item.npc.name}! Affinity is now $newScore."
                        item.copy(type = RelationshipType.ROMANCE, affinity = newScore)
                    }
                    "RIVAL" -> {
                        if (item.type == RelationshipType.RIVAL) {
                            val newScore = (item.affinity + 25).coerceIn(-100, 100)
                            val newType = if (newScore >= 15) RelationshipType.FRIEND else RelationshipType.RIVAL
                            cloutBoost = 14L
                            credBoost = 12L
                            narrative = if (newType == RelationshipType.FRIEND) {
                                "Publicly debated ${item.npc.name} at Freedom Square and earned their respect! Rivalry squashed into Friendship!"
                            } else {
                                "Fierce debate showdown with ${item.npc.name} at Faculty of Social Sciences (+14 Clout, +12 Cred)!"
                            }
                            item.copy(type = newType, affinity = newScore)
                        } else {
                            val newScore = (item.affinity - 30).coerceIn(-100, 100)
                            cloutBoost = 10L
                            narrative = "Challenged ${item.npc.name} in a heated campus debate! Sparks flew (+10 Clout)."
                            item.copy(
                                type = if (newScore < 0) RelationshipType.RIVAL else item.type,
                                affinity = newScore
                            )
                        }
                    }
                    else -> { // FRIEND / STUDY
                        val newScore = (item.affinity + 16).coerceIn(-100, 100)
                        prepBoost = if (item.npc.id == "Chuka_Efiwe" || item.npc.id == "Dr_Obinna") 10L else 4L
                        sanityBoost = 8L
                        credBoost = 6L
                        narrative = "Linked up with ${item.npc.name} at ${item.npc.hostelOrOffice} (Affinity: $newScore/100)."
                        item.copy(
                            type = if (item.type == RelationshipType.RIVAL && newScore >= 10) RelationshipType.FRIEND else item.type,
                            affinity = newScore
                        )
                    }
                }
            }
        }

        val updatedMeta = buildRichMetadataList(courses, updatedNpcs, clubs, clique, loc)
        val updatedCred = (career.streetCred + credBoost).coerceIn(0L, 100L)
        val updatedClout = (career.politicalClout + cloutBoost).coerceIn(0L, 100L)
        val updatedTier = UnnCampusCatalog.computeSocialTier(
            cred = updatedCred,
            clout = updatedClout,
            cgpa = career.cgpa,
            naira = career.naira - cost,
            sugOffice = career.sugOffice
        )

        val updatedCareer = career.copy(
            naira = (career.naira - cost).coerceAtLeast(0L),
            sanity = (career.sanity + sanityBoost).coerceIn(0L, 100L),
            academicPrep = (career.academicPrep + prepBoost).coerceIn(0L, 100L),
            streetCred = updatedCred,
            politicalClout = updatedClout,
            socialTier = updatedTier,
            relationships = updatedMeta,
            recentLogs = (listOf("L${career.level} S${career.semester} W${career.week}: $narrative") + career.recentLogs).take(30)
        )
        saveCareerUpdate(updatedCareer, narrative)
    }

    fun joinOrAttendClub(career: StudentCareer, club: StudentClub) {
        val courses = UnnAcademicSystem.decodeCourses(career.relationships, career.department, career.level, career.semester)
        val npcs = UnnSocialAndClubsData.parseNpcStates(career.relationships)
        val currentClubs = UnnSocialAndClubsData.parseJoinedClubs(career.relationships)
        val clique = UnnSocialAndClubsData.parseActiveClique(career.relationships)

        val isAlreadyMember = currentClubs.contains(club.id)
        if (!isAlreadyMember && career.naira < club.duesNaira) {
            _statusBannerMessage.value = "Need ₦${club.duesNaira} to pay membership dues for ${club.name}!"
            return
        }

        val updatedClubs = if (isAlreadyMember) currentClubs else (currentClubs + club.id)
        val cost = if (isAlreadyMember) 0L else club.duesNaira
        val updatedMeta = buildRichMetadataList(courses, npcs, updatedClubs, clique, club.headquartersLandmarkId)

        val updatedCred = (career.streetCred + club.credBonus).coerceIn(0L, 100L)
        val updatedClout = (career.politicalClout + club.cloutBonus).coerceIn(0L, 100L)
        val updatedSanity = (career.sanity + club.sanityBonus).coerceIn(0L, 100L)
        val updatedPrep = (career.academicPrep + club.prepBonus).coerceIn(0L, 100L)

        val log = if (isAlreadyMember) {
            "L${career.level} S${career.semester} W${career.week}: Attended weekly ${club.name} fellowship/meeting."
        } else {
            "L${career.level} S${career.semester} W${career.week}: Joined ${club.name} (${club.category})!"
        }

        val updatedCareer = career.copy(
            naira = career.naira - cost,
            streetCred = updatedCred,
            politicalClout = updatedClout,
            sanity = updatedSanity,
            academicPrep = updatedPrep,
            socialTier = UnnCampusCatalog.computeSocialTier(updatedCred, updatedClout, career.cgpa, career.naira - cost, career.sugOffice),
            relationships = updatedMeta,
            recentLogs = (listOf(log) + career.recentLogs).take(30)
        )
        saveCareerUpdate(updatedCareer, if (isAlreadyMember) "Attended ${club.name}!" else "Joined ${club.name}!")
    }

    fun selectSocialClique(career: StudentCareer, clique: SocialClique) {
        if (career.streetCred < clique.minCred || career.politicalClout < clique.minClout ||
            career.cgpa < clique.minCgpa || career.naira < clique.minNaira
        ) {
            _statusBannerMessage.value = "You don't meet ${clique.name} requirements yet (${clique.requirementDescription})!"
            return
        }

        val courses = UnnAcademicSystem.decodeCourses(career.relationships, career.department, career.level, career.semester)
        val npcs = UnnSocialAndClubsData.parseNpcStates(career.relationships)
        val clubs = UnnSocialAndClubsData.parseJoinedClubs(career.relationships)
        val loc = UnnSocialAndClubsData.parseCurrentLocation(career.relationships)

        val updatedMeta = buildRichMetadataList(courses, npcs, clubs, clique.name, loc)
        val badge = "Clique: ${clique.name.take(22)}"
        val updatedBadges = if (career.badges.contains(badge)) career.badges else (career.badges + badge).take(25)

        val updatedCareer = career.copy(
            streetCred = (career.streetCred + 8L).coerceIn(0L, 100L),
            politicalClout = (career.politicalClout + 6L).coerceIn(0L, 100L),
            badges = updatedBadges,
            relationships = updatedMeta,
            recentLogs = (listOf("L${career.level} S${career.semester} W${career.week}: Inducted into the '${clique.name}' social clique!") + career.recentLogs).take(30)
        )
        saveCareerUpdate(updatedCareer, "Now repping ${clique.name}!")
    }

    fun attendCampusSocialEvent(career: StudentCareer, event: CampusSocialEvent) {
        if (career.naira < event.ticketCostNaira) {
            _statusBannerMessage.value = "Need ₦${event.ticketCostNaira} for ${event.title} VIP ticket!"
            return
        }
        if (career.energy < 14L) {
            _statusBannerMessage.value = "Too tired to party! Rest in your hostel first."
            return
        }

        val courses = UnnAcademicSystem.decodeCourses(career.relationships, career.department, career.level, career.semester)
        val npcs = UnnSocialAndClubsData.parseNpcStates(career.relationships)
        val clubs = UnnSocialAndClubsData.parseJoinedClubs(career.relationships)
        val clique = UnnSocialAndClubsData.parseActiveClique(career.relationships)

        val updatedMeta = buildRichMetadataList(courses, npcs, clubs, clique, event.venueLandmarkId)
        val updatedCred = (career.streetCred + event.credDelta).coerceIn(0L, 100L)
        val updatedClout = (career.politicalClout + event.cloutDelta).coerceIn(0L, 100L)

        val baseUpdated = career.copy(
            naira = career.naira - event.ticketCostNaira,
            energy = (career.energy + event.energyDelta).coerceIn(0L, 100L),
            sanity = (career.sanity + event.sanityDelta).coerceIn(0L, 100L),
            streetCred = updatedCred,
            politicalClout = updatedClout,
            relationships = updatedMeta
        )

        val log = "L${career.level} S${career.semester} W${career.week}: ${event.highlightLog}"
        val finalCareer = advanceClockOrPersist(baseUpdated, log, advanceWeek = true)
        saveCareerUpdate(finalCareer, event.highlightLog)
    }

    // --- EXISTING ACTIONS & CORE CLOCK ---

    fun performSpotAction(career: StudentCareer, action: UnnSpotAction) {
        if (career.isGraduated) {
            _statusBannerMessage.value = "You have already graduated! Start a new career or view Hall of Fame."
            return
        }
        if (action.costNaira > 0 && career.naira < action.costNaira) {
            _statusBannerMessage.value = "Insufficient Naira (Need ₦${action.costNaira})! Run a side hustle at Odim/Roar Hub."
            return
        }
        if (action.energyDelta < 0 && career.energy < 12L) {
            _statusBannerMessage.value = "Exhausted! Rest at your hostel or grab hot Okpa at SUB first."
            return
        }

        val updatedNaira = (career.naira - action.costNaira).coerceAtLeast(0L)
        val updatedEnergy = (career.energy + action.energyDelta).coerceIn(0L, 100L)
        val updatedSanity = (career.sanity + action.sanityDelta).coerceIn(0L, 100L)
        val updatedPrep = (career.academicPrep + action.prepDelta).coerceIn(0L, 100L)
        val updatedCred = (career.streetCred + action.credDelta).coerceIn(0L, 100L)
        val updatedClout = (career.politicalClout + action.cloutDelta).coerceIn(0L, 100L)

        val logEntry = "L${career.level} S${career.semester} W${career.week}: ${action.logTemplate}"
        val updatedCareer = advanceClockOrPersist(
            base = career.copy(
                naira = updatedNaira,
                energy = updatedEnergy,
                sanity = updatedSanity,
                academicPrep = updatedPrep,
                streetCred = updatedCred,
                politicalClout = updatedClout
            ),
            newLog = logEntry,
            advanceWeek = true
        )

        saveCareerUpdate(updatedCareer, action.logTemplate)
    }

    fun resolveShenaniganChoice(career: StudentCareer, event: CampusShenaniganEvent, choice: ShenaniganChoice) {
        _activeEventModal.value = null
        if (choice.nairaDelta < 0 && career.naira + choice.nairaDelta < 0) {
            _statusBannerMessage.value = "Not enough Naira for that choice! Pick another move."
            return
        }

        val updatedNaira = (career.naira + choice.nairaDelta).coerceAtLeast(0L)
        val updatedEnergy = (career.energy + choice.energyDelta).coerceIn(0L, 100L)
        val updatedSanity = (career.sanity + choice.sanityDelta).coerceIn(0L, 100L)
        val updatedPrep = (career.academicPrep + choice.prepDelta).coerceIn(0L, 100L)
        val updatedCred = (career.streetCred + choice.credDelta).coerceIn(0L, 100L)
        val updatedClout = (career.politicalClout + choice.cloutDelta).coerceIn(0L, 100L)
        val updatedBadges = if (choice.badgeAwarded != null && !career.badges.contains(choice.badgeAwarded)) {
            (career.badges + choice.badgeAwarded).take(25)
        } else {
            career.badges
        }

        val logEntry = "L${career.level} S${career.semester} W${career.week} [${event.title}]: ${choice.outcomeNarrative}"
        val updatedCareer = advanceClockOrPersist(
            base = career.copy(
                naira = updatedNaira,
                energy = updatedEnergy,
                sanity = updatedSanity,
                academicPrep = updatedPrep,
                streetCred = updatedCred,
                politicalClout = updatedClout,
                badges = updatedBadges
            ),
            newLog = logEntry,
            advanceWeek = true
        )

        saveCareerUpdate(updatedCareer, choice.outcomeNarrative)
    }

    fun buyShopItem(career: StudentCareer, item: UnnShopItem) {
        if (career.inventory.contains(item.name)) {
            _statusBannerMessage.value = "You already own ${item.name}!"
            return
        }
        if (career.naira < item.priceNaira) {
            _statusBannerMessage.value = "Need ₦${item.priceNaira} to buy ${item.name}."
            return
        }

        val updatedNaira = career.naira - item.priceNaira
        val updatedPrep = (career.academicPrep + item.prepBonus).coerceIn(0L, 100L)
        val updatedEnergy = (career.energy + item.energyBonus).coerceIn(0L, 100L)
        val updatedSanity = (career.sanity + item.sanityBonus).coerceIn(0L, 100L)
        val updatedCred = (career.streetCred + item.credBonus).coerceIn(0L, 100L)
        val updatedClout = (career.politicalClout + item.cloutBonus).coerceIn(0L, 100L)
        val updatedInventory = (career.inventory + item.name).take(25)
        val updatedTier = UnnCampusCatalog.computeSocialTier(
            cred = updatedCred,
            clout = updatedClout,
            cgpa = career.cgpa,
            naira = updatedNaira,
            sugOffice = career.sugOffice
        )
        val logEntry = "L${career.level} S${career.semester} W${career.week}: Purchased ${item.name} at Odim Market for ₦${item.priceNaira}."

        val updatedCareer = career.copy(
            naira = updatedNaira,
            academicPrep = updatedPrep,
            energy = updatedEnergy,
            sanity = updatedSanity,
            streetCred = updatedCred,
            politicalClout = updatedClout,
            socialTier = updatedTier,
            inventory = updatedInventory,
            recentLogs = (listOf(logEntry) + career.recentLogs).take(30)
        )
        saveCareerUpdate(updatedCareer, "Bought ${item.name}!")
    }

    fun switchHostel(career: StudentCareer, hostel: UnnHostelOption) {
        if (career.hostel == hostel.name) {
            _statusBannerMessage.value = "You already stay at ${hostel.name}!"
            return
        }
        if (career.naira < hostel.rentNaira) {
            _statusBannerMessage.value = "Need ₦${hostel.rentNaira} rent to relocate to ${hostel.name}."
            return
        }
        val updatedNaira = career.naira - hostel.rentNaira
        val updatedEnergy = (career.energy + hostel.energyModifier).coerceIn(0L, 100L)
        val updatedSanity = (career.sanity + hostel.sanityModifier).coerceIn(0L, 100L)
        val updatedClout = (career.politicalClout + hostel.cloutModifier).coerceIn(0L, 100L)
        val updatedTier = UnnCampusCatalog.computeSocialTier(
            cred = career.streetCred,
            clout = updatedClout,
            cgpa = career.cgpa,
            naira = updatedNaira,
            sugOffice = career.sugOffice
        )
        val logEntry = "L${career.level} S${career.semester} W${career.week}: Relocated to ${hostel.name} (Rent: ₦${hostel.rentNaira})."
        val updatedCareer = career.copy(
            hostel = hostel.name,
            naira = updatedNaira,
            energy = updatedEnergy,
            sanity = updatedSanity,
            politicalClout = updatedClout,
            socialTier = updatedTier,
            recentLogs = (listOf(logEntry) + career.recentLogs).take(30)
        )
        saveCareerUpdate(updatedCareer, "Moved into ${hostel.name}!")
    }

    fun campaignForOffice(career: StudentCareer, office: UnnPoliticalOffice) {
        if (career.sugOffice == office.title) {
            _statusBannerMessage.value = "You already hold the office of ${office.title}!"
            return
        }
        if (career.level < office.minLevel) {
            _statusBannerMessage.value = "Must be at least ${office.minLevel} Level to contest for ${office.title}."
            return
        }
        if (career.politicalClout < office.requiredClout || career.streetCred < office.requiredCred) {
            _statusBannerMessage.value = "Need ${office.requiredClout} Clout & ${office.requiredCred} Street Cred to win ${office.title}!"
            return
        }
        if (career.naira < office.campaignCostNaira) {
            _statusBannerMessage.value = "Need ₦${office.campaignCostNaira} for campaign posters & manifesto logistics."
            return
        }

        val updatedNaira = career.naira - office.campaignCostNaira
        val updatedClout = (career.politicalClout + 12L).coerceIn(0L, 100L)
        val updatedCred = (career.streetCred + 10L).coerceIn(0L, 100L)
        val newBadge = "Elected ${office.title}"
        val updatedBadges = if (career.badges.contains(newBadge)) career.badges else (career.badges + newBadge).take(25)
        val updatedTier = UnnCampusCatalog.computeSocialTier(
            cred = updatedCred,
            clout = updatedClout,
            cgpa = career.cgpa,
            naira = updatedNaira,
            sugOffice = office.title
        )
        val logEntry = "L${career.level} S${career.semester} W${career.week}: WON ELECTION as ${office.title}! Weekly union allowance: +₦${office.weeklyStipendNaira}."

        val updatedCareer = career.copy(
            sugOffice = office.title,
            naira = updatedNaira,
            politicalClout = updatedClout,
            streetCred = updatedCred,
            socialTier = updatedTier,
            badges = updatedBadges,
            recentLogs = (listOf(logEntry) + career.recentLogs).take(30)
        )
        saveCareerUpdate(updatedCareer, "Victory! Elected as ${office.title}!")
    }

    private fun advanceClockOrPersist(
        base: StudentCareer,
        newLog: String,
        advanceWeek: Boolean
    ): StudentCareer {
        val officeStipend = UnnCampusCatalog.politicalOffices
            .firstOrNull { it.title == base.sugOffice }?.weeklyStipendNaira ?: 0L
        val homeAllowance = if (base.week % 4L == 0L) 8000L else 0L
        val stipendPlusNaira = base.naira + officeStipend + homeAllowance

        val logsWithAction = (listOf(newLog) + base.recentLogs).take(30)

        if (!advanceWeek) {
            val tier = UnnCampusCatalog.computeSocialTier(
                cred = base.streetCred,
                clout = base.politicalClout,
                cgpa = base.cgpa,
                naira = stipendPlusNaira,
                sugOffice = base.sugOffice
            )
            return base.copy(naira = stipendPlusNaira, socialTier = tier, recentLogs = logsWithAction)
        }

        val nextWeek = base.week + 1L
        if (nextWeek <= 8L) {
            val allowanceNote = if (homeAllowance > 0 || officeStipend > 0) {
                " [Weekly Inflow: +₦${homeAllowance + officeStipend}]"
            } else ""
            val tier = UnnCampusCatalog.computeSocialTier(
                cred = base.streetCred,
                clout = base.politicalClout,
                cgpa = base.cgpa,
                naira = stipendPlusNaira,
                sugOffice = base.sugOffice
            )
            // Automatic night-time hostel shenanigan trigger at week transition (70% probability)
            if (_activeNightEventModal.value == null && Random.nextInt(100) < 70) {
                _activeNightEventModal.value = HostelNightShenanigansGenerator.rollNightEvent(
                    hostelName = base.hostel,
                    recentLogs = base.recentLogs
                )
            }
            return base.copy(
                week = nextWeek,
                naira = stipendPlusNaira,
                socialTier = tier,
                recentLogs = (listOf(newLog + allowanceNote) + base.recentLogs).take(30)
            )
        }

        return executeFullSemesterExamination(base.copy(naira = stipendPlusNaira), newLog)
    }

    private fun executeFullSemesterExamination(base: StudentCareer, triggerLog: String): StudentCareer {
        val courses = UnnAcademicSystem.decodeCourses(base.relationships, base.department, base.level, base.semester)
        val npcs = UnnSocialAndClubsData.parseNpcStates(base.relationships)
        val clubs = UnnSocialAndClubsData.parseJoinedClubs(base.relationships)
        val clique = UnnSocialAndClubsData.parseActiveClique(base.relationships)
        val loc = UnnSocialAndClubsData.parseCurrentLocation(base.relationships)

        val examOutcome = UnnAcademicSystem.gradeRegisteredCourses(
            courses = courses,
            currentCgpa = base.cgpa,
            globalPrep = base.academicPrep,
            sanity = base.sanity,
            energy = base.energy,
            hasGsVault = base.inventory.any { it.contains("Past Questions") },
            hasLamp = base.inventory.any { it.contains("Lamp") }
        )

        val nextSemester: Long
        val nextLevel: Long
        val graduatedNow: Boolean

        if (base.semester == 1L) {
            nextSemester = 2L
            nextLevel = base.level
            graduatedNow = false
        } else {
            if (base.level >= 400L) {
                nextSemester = 2L
                nextLevel = 400L
                graduatedNow = true
            } else {
                nextSemester = 1L
                nextLevel = base.level + 100L
                graduatedNow = false
            }
        }

        val freshNextCourses = UnnAcademicSystem.defaultCoursesForStudent(
            department = base.department,
            level = nextLevel,
            semester = nextSemester
        ).mapIndexed { index, nextCourse ->
            val prevGrade = examOutcome.courseGrades.getOrNull(index)?.letterGrade ?: "-"
            nextCourse.copy(lastGrade = prevGrade)
        }

        val updatedMeta = buildRichMetadataList(freshNextCourses, npcs, clubs, clique, loc)
        val postExamNaira = base.naira + examOutcome.scholarshipNaira
        val updatedCarryOvers = base.carryOvers + examOutcome.failedCoursesCount
        val examLog = "L${base.level} S${base.semester} EXAMS: ${examOutcome.summaryNarrative}"

        val updatedBadges = buildList {
            addAll(base.badges)
            if (examOutcome.standing == AcademicStanding.DEANS_LIST && !contains("Dean's Honour Roll")) {
                add("Dean's Honour Roll")
            }
            if (examOutcome.standing == AcademicStanding.ACADEMIC_PROBATION && !contains("Probation Survivor")) {
                add("Probation Warning")
            }
            if (graduatedNow && !contains("Convocation Lion")) {
                add("Convocation Lion")
            }
        }.take(25)

        val updatedTier = UnnCampusCatalog.computeSocialTier(
            cred = base.streetCred,
            clout = base.politicalClout,
            cgpa = examOutcome.updatedCgpa,
            naira = postExamNaira,
            sugOffice = base.sugOffice
        )

        val finalLogs = buildList {
            if (graduatedNow) {
                add("CONVOCATION AT MARGARET EKPO REF: Graduated with ${String.format("%.2f", examOutcome.updatedCgpa)} CGPA (${UnnShenanigansEngine.degreeClassification(examOutcome.updatedCgpa, updatedCarryOvers)})!")
            }
            add(examLog)
            add(triggerLog)
            addAll(base.recentLogs)
        }.take(30)

        val resultCareer = base.copy(
            level = nextLevel,
            semester = nextSemester,
            week = if (graduatedNow) 8L else 1L,
            cgpa = examOutcome.updatedCgpa,
            naira = postExamNaira,
            energy = 85L,
            sanity = (base.sanity + 10L).coerceIn(40L, 100L),
            academicPrep = 38L,
            carryOvers = updatedCarryOvers,
            isGraduated = graduatedNow,
            socialTier = updatedTier,
            relationships = updatedMeta,
            badges = updatedBadges,
            recentLogs = finalLogs
        )

        if (graduatedNow) {
            recordConvocationInHallOfFame(resultCareer)
        }

        return resultCareer
    }

    private fun recordConvocationInHallOfFame(career: StudentCareer) {
        viewModelScope.launch {
            val degreeClass = UnnShenanigansEngine.degreeClassification(career.cgpa, career.carryOvers)
            val record = AlumniRecord(
                userId = currentUserId,
                studentName = career.studentName,
                regNumber = career.regNumber,
                department = "${career.department} (${career.campus})",
                finalCgpa = career.cgpa,
                degreeClass = degreeClass,
                finalTitle = career.socialTier,
                netWorthNaira = career.naira,
                summary = "Graduated from ${career.department} living at ${career.hostel}. Held office: ${career.sugOffice}. Earned ${career.badges.size} campus badges."
            )
            repository.addAlumniRecord(record)
        }
    }

    fun deleteCareer(careerId: String) {
        viewModelScope.launch {
            _isSaving.value = true
            repository.deleteCareer(careerId)
            _isSaving.value = false
            _statusBannerMessage.value = "Student career deleted."
        }
    }

    private fun saveCareerUpdate(updatedCareer: StudentCareer, bannerMsg: String) {
        viewModelScope.launch {
            _isSaving.value = true
            val res = repository.updateCareer(updatedCareer)
            _isSaving.value = false
            res.onSuccess {
                _statusBannerMessage.value = bannerMsg
            }.onFailure { err ->
                _statusBannerMessage.value = err.localizedMessage ?: "Failed to save progress"
            }
        }
    }

    companion object {
        private const val TAG = "LionDenVM"
        private const val STOP_TIMEOUT_MILLIS = 5000L
    }
}
