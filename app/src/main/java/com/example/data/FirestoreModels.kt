package com.example.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

@IgnoreExtraProperties
data class StudentCareer(
    @DocumentId val id: String = "",
    val userId: String = "",
    val studentName: String = "",
    val regNumber: String = "",
    val campus: String = "Nsukka",
    val faculty: String = "Engineering",
    val department: String = "Electronic Engineering",
    val hostel: String = "Zik's Flats",
    val archetype: String = "Efiwe (Bookworm)",
    val level: Long = 100L,
    val semester: Long = 1L,
    val week: Long = 1L,
    val cgpa: Double = 4.50,
    val naira: Long = 25000L,
    val energy: Long = 85L,
    val sanity: Long = 85L,
    val academicPrep: Long = 35L,
    val streetCred: Long = 20L,
    val politicalClout: Long = 10L,
    val socialTier: String = "Jambite / JJC",
    val sugOffice: String = "None",
    val carryOvers: Long = 0L,
    @get:PropertyName("isGraduated")
    @set:PropertyName("isGraduated")
    var isGraduated: Boolean = false,
    val inventory: List<String> = emptyList(),
    val relationships: List<String> = emptyList(),
    val recentLogs: List<String> = emptyList(),
    val badges: List<String> = emptyList(),
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class AlumniRecord(
    @DocumentId val id: String = "",
    val userId: String = "",
    val studentName: String = "",
    val regNumber: String = "",
    val department: String = "",
    val finalCgpa: Double = 0.0,
    val degreeClass: String = "",
    val finalTitle: String = "",
    val netWorthNaira: Long = 0L,
    val summary: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)
