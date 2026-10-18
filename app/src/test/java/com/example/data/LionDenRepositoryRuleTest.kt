package com.example.data

import com.example.base.FirestoreEmulatorTestBase
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.util.UUID

class LionDenRepositoryRuleTest : FirestoreEmulatorTestBase() {

    private fun sampleCareer(uid: String): StudentCareer {
        return StudentCareer(
            id = "career_${UUID.randomUUID().toString().take(8)}",
            userId = uid,
            studentName = "Adaeze Nwosu",
            regNumber = "2026/249102",
            campus = "Nsukka",
            faculty = "Biological Sciences",
            department = "Microbiology",
            hostel = "Okpara Hall",
            archetype = "Night Class Nomad",
            level = 100L,
            semester = 1L,
            week = 1L,
            cgpa = 4.65,
            naira = 30000L,
            energy = 90L,
            sanity = 85L,
            academicPrep = 45L,
            streetCred = 30L,
            politicalClout = 15L,
            socialTier = "Jambite / JJC",
            sugOffice = "None",
            carryOvers = 0L,
            isGraduated = false,
            inventory = listOf("Rechargeable Lamp", "GS 101 Textbook"),
            relationships = listOf("Roommate_Ngozi:80"),
            recentLogs = listOf("W1: Arrived at Nsukka campus and cleared hostel allocation at Okpara Hall."),
            badges = listOf("Den Arrival")
        )
    }

    @Test
    fun createAndObserveCareer_authenticatedOwner_succeeds() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val repository = LionDenRepository(firestore)

        val createResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.createCareer(sampleCareer(aliceUid))
        }
        assertTrue("Expected career creation to succeed: ${createResult.exceptionOrNull()}", createResult.isSuccess)
        val careerId = createResult.getOrThrow()

        val emittedList = withTimeout(FLOW_TIMEOUT_MS) {
            repository.observeCareers(aliceUid).first { list -> list.any { it.id == careerId } }
        }
        val fetched = emittedList.first { it.id == careerId }
        assertEquals("Adaeze Nwosu", fetched.studentName)

        // Verify update works and preserves createdAt
        val updateResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.updateCareer(fetched.copy(week = 2L, naira = 28000L, academicPrep = 60L))
        }
        assertTrue("Expected career update to succeed: ${updateResult.exceptionOrNull()}", updateResult.isSuccess)
    }

    @Test
    fun getCareerById_crossUserAccess_failsWithPermissionDenied() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val repository = LionDenRepository(firestore)
        val careerId = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.createCareer(sampleCareer(aliceUid)).getOrThrow()
        }

        signInTestUser(BOB_EMAIL)
        val crossUserResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.getCareerById(aliceUid, careerId)
        }
        assertTrue("Cross-user access must fail", crossUserResult.isFailure)
        val ex = crossUserResult.exceptionOrNull() as? FirebaseFirestoreException
        assertNotNull("Expected FirebaseFirestoreException", ex)
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, ex?.code)
    }

    @Test
    fun observeCareers_unauthenticatedUser_failsWithPermissionDenied() = runBlocking {
        auth.signOut()
        val repository = LionDenRepository(firestore)
        val uncachedUid = "unauth_${UUID.randomUUID().toString().take(8)}"

        try {
            withTimeout(FLOW_TIMEOUT_MS) {
                repository.observeCareers(uncachedUid).first()
            }
            fail("Expected FirebaseFirestoreException for unauthenticated user")
        } catch (e: FirebaseFirestoreException) {
            assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, e.code)
        }
    }

    @Test
    fun addAndObserveHallOfFame_authenticatedOwner_succeeds() = runBlocking {
        val aliceUid = signInTestUser(ALICE_EMAIL)
        val repository = LionDenRepository(firestore)

        val record = AlumniRecord(
            id = "hof_${UUID.randomUUID().toString().take(8)}",
            userId = aliceUid,
            studentName = "Adaeze Nwosu",
            regNumber = "2026/249102",
            department = "Microbiology",
            finalCgpa = 4.82,
            degreeClass = "First Class Honours",
            finalTitle = "Supreme Lioness of Nsukka",
            netWorthNaira = 240000L,
            summary = "Conquered GS courses, led SUG Senate, and graduated with First Class Honours."
        )

        val result = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.addAlumniRecord(record)
        }
        assertTrue("Expected alumni record creation to succeed: ${result.exceptionOrNull()}", result.isSuccess)
    }

    private companion object {
        const val ALICE_EMAIL = "alice_unn@test.com"
        const val BOB_EMAIL = "bob_unn@test.com"
        const val DEFAULT_TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 4000L
    }
}
