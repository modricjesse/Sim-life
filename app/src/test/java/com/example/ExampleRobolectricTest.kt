package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.example.data.AcademicStanding
import com.example.data.RelationshipType
import com.example.data.StudentCareer
import com.example.data.UnnAcademicSystem
import com.example.data.UnnInteractiveMapData
import com.example.data.UnnSocialAndClubsData
import com.example.ui.screens.LionDenGameScreen
import com.example.ui.theme.MyApplicationTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class ExampleRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verifyAppNameResource() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertEquals("Lion Den Sim", context.getString(R.string.app_name))
    }

    @Test
    fun verifyInteractiveUnnCampusMapLandmarksAndNavigationMath() {
        val landmarks = UnnInteractiveMapData.landmarks
        assertTrue(landmarks.any { it.name.contains("Faculty of Social Sciences") })
        assertTrue(landmarks.any { it.name.contains("John Pepper Clark Library") })
        assertTrue(landmarks.any { it.name.contains("Mary Slessor Hall") })
        assertTrue(landmarks.any { it.name.contains("Okija Hall") })
        assertTrue(landmarks.any { it.name.contains("Students' Union Building") })
        assertTrue(landmarks.any { it.name.contains("Carver Building") })
        assertTrue(landmarks.any { it.name.contains("Paul Robeson") })

        val fassa = UnnInteractiveMapData.findLandmark("faculty_social_sciences")
        val jpcLib = UnnInteractiveMapData.findLandmark("john_pepper_clark_lib")
        val okija = UnnInteractiveMapData.findLandmark("okija_hall")

        val distMeters = UnnInteractiveMapData.calculateDistanceMeters(fassa, okija)
        val trekCost = UnnInteractiveMapData.calculateTrekEnergyCost(fassa, okija)
        assertTrue("Distance between FASSA and Okija should be > 150m", distMeters >= 150)
        assertTrue("Trek energy cost should be between 4 and 14", trekCost in 4..14)
        assertEquals(0, UnnInteractiveMapData.calculateDistanceMeters(jpcLib, jpcLib))
    }

    @Test
    fun verifyAcademicProgressionGradingAttendanceAndProbation() {
        val defaultCourses = UnnAcademicSystem.defaultCoursesForStudent(
            department = "Political Science",
            level = 100L,
            semester = 1L
        )
        assertTrue(defaultCourses.size >= 5)

        // Encode and decode round-trip check
        val encoded = defaultCourses.map { UnnAcademicSystem.encodeCourse(it) }
        val decoded = UnnAcademicSystem.decodeCourses(encoded, "Political Science", 100L, 1L)
        assertEquals(defaultCourses.size, decoded.size)
        assertEquals(defaultCourses.first().code, decoded.first().code)

        // High attendance & mastery -> Dean's Honour List
        val strongCourses = defaultCourses.map {
            it.copy(isRegistered = true, classesAttended = 4, midTermTaken = true, midTermCaScore = 27, mastery = 92)
        }
        val strongOutcome = UnnAcademicSystem.gradeRegisteredCourses(
            courses = strongCourses,
            currentCgpa = 4.60,
            globalPrep = 88L,
            sanity = 85L,
            energy = 80L,
            hasGsVault = true,
            hasLamp = true
        )
        assertTrue(strongOutcome.semesterGpa >= 4.50)
        assertEquals(AcademicStanding.DEANS_LIST, strongOutcome.standing)
        assertFalse(strongOutcome.courseGrades.first().attendancePenaltyApplied)

        // Poor attendance & low mastery -> Academic Probation
        val weakCourses = defaultCourses.map {
            it.copy(isRegistered = true, classesAttended = 0, midTermTaken = true, midTermCaScore = 8, mastery = 15)
        }
        val weakOutcome = UnnAcademicSystem.gradeRegisteredCourses(
            courses = weakCourses,
            currentCgpa = 1.20,
            globalPrep = 12L,
            sanity = 20L,
            energy = 20L,
            hasGsVault = false,
            hasLamp = false
        )
        assertTrue(weakOutcome.courseGrades.first().attendancePenaltyApplied)
        assertEquals(AcademicStanding.ACADEMIC_PROBATION, weakOutcome.standing)
    }

    @Test
    fun verifySocialClubsCliquesAndRelationshipsEncoding() {
        assertTrue(UnnSocialAndClubsData.clubs.any { it.category.contains("Religious") })
        assertTrue(UnnSocialAndClubsData.clubs.any { it.category.contains("Academic") })
        assertTrue(UnnSocialAndClubsData.clubs.any { it.category.contains("Cultural") })
        assertTrue(UnnSocialAndClubsData.socialEvents.isNotEmpty())
        assertTrue(UnnSocialAndClubsData.socialCliques.size >= 4)

        val rawMetadata = listOf(
            "LOC|mary_slessor_hall",
            "CLIQUE|Hilltop & Bush Bar Elites",
            "CLUB|club_fassa_debate",
            "REL|Amaka_Hilltop|ROMANCE|88",
            "REL|Dike_Rival|RIVAL|-65"
        )
        assertEquals("mary_slessor_hall", UnnSocialAndClubsData.parseCurrentLocation(rawMetadata))
        assertEquals("Hilltop & Bush Bar Elites", UnnSocialAndClubsData.parseActiveClique(rawMetadata))
        assertTrue(UnnSocialAndClubsData.parseJoinedClubs(rawMetadata).contains("club_fassa_debate"))

        val parsedNpcs = UnnSocialAndClubsData.parseNpcStates(rawMetadata)
        val amaka = parsedNpcs.firstOrNull { it.npc.id == "Amaka_Hilltop" }
        val dike = parsedNpcs.firstOrNull { it.npc.id == "Dike_Rival" }
        assertNotNull(amaka)
        assertEquals(RelationshipType.ROMANCE, amaka?.type)
        assertEquals(88, amaka?.affinity)
        assertEquals(RelationshipType.RIVAL, dike?.type)
    }

    @Test
    fun verifyGameScreenTabsRenderMapAcademicsAndSocial() {
        val sampleCareer = StudentCareer(
            id = "career_test_1",
            userId = "user_1",
            studentName = "Adaeze Nwosu",
            regNumber = "2026/248901",
            campus = "Nsukka",
            faculty = "Social Sciences",
            department = "Political Science",
            hostel = "Mary Slessor Hall",
            archetype = "Aluta Politician",
            level = 200L,
            semester = 1L,
            week = 3L,
            cgpa = 4.62,
            naira = 42000L
        )

        composeTestRule.setContent {
            MyApplicationTheme(darkTheme = true) {
                LionDenGameScreen(
                    career = sampleCareer,
                    allCareers = listOf(sampleCareer),
                    hallOfFame = emptyList(),
                    statusBanner = null,
                    activeEventModal = null,
                    onDismissBanner = {},
                    onTriggerShenanigan = {},
                    onDismissShenanigan = {},
                    onResolveShenanigan = { _, _ -> },
                    onPerformSpotAction = {},
                    onNavigateMapTrek = {},
                    onNavigateMapShuttle = {},
                    onExecuteLandmarkAction = {},
                    onToggleCourseRegistration = {},
                    onAttendCourseLecture = {},
                    onStudyAtJpClarkLibrary = {},
                    onSitMidTermExams = {},
                    onSitFinalSemesterExams = {},
                    onInteractNpc = { _, _ -> },
                    onJoinOrAttendClub = {},
                    onSelectClique = {},
                    onAttendSocialEvent = {},
                    onBuyShopItem = {},
                    onSwitchHostel = {},
                    onCampaignOffice = {},
                    onSelectCareer = {},
                    onStartNewCareer = {},
                    onDeleteCareer = {},
                    onSignOut = {}
                )
            }
        }

        // Verify Student Header
        composeTestRule.onNodeWithText("Adaeze Nwosu").assertIsDisplayed()

        // Navigate to Interactive UNN Map Tab
        composeTestRule.onNodeWithTag("nav_tab_campus").performClick()
        composeTestRule.onNodeWithText("UNN Nsukka Interactive Campus Map").assertIsDisplayed()
        composeTestRule.onNodeWithTag("interactive_unn_campus_map_canvas").assertExists()

        // Navigate to Academic Progression Tab
        composeTestRule.onNodeWithTag("nav_tab_academics").performClick()
        composeTestRule.onNodeWithText("Dean's Honour List (First Class Track)").assertIsDisplayed()

        // Navigate to Social & Clubs Tab
        composeTestRule.onNodeWithTag("nav_tab_politics").performClick()
        composeTestRule.onNodeWithText("CAMPUS SOCIAL STRATA & CLIQUE").assertIsDisplayed()
        composeTestRule.onNodeWithTag("social_subtab_clubs_and_cliques").assertExists()
    }
}
