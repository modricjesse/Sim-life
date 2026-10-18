package com.example.data

import kotlin.math.roundToInt

data class UnnCourseProgress(
    val code: String,
    val title: String,
    val creditUnits: Int,
    val venueId: String,
    val venueName: String,
    val isCore: Boolean,
    val isRegistered: Boolean = false,
    val classesAttended: Int = 0,
    val requiredClasses: Int = 4, // 75% rule = 3 out of 4 classes minimum
    val midTermCaScore: Int = 0, // Out of 30
    val midTermTaken: Boolean = false,
    val mastery: Int = 35, // 0..100
    val lastGrade: String = "-"
) {
    val attendancePercent: Int
        get() = ((classesAttended.toFloat() / requiredClasses.toFloat()) * 100f).roundToInt().coerceIn(0, 100)

    val meetsAttendanceRequirement: Boolean
        get() = classesAttended >= 3 // >= 75% of 4 classes
}

enum class AcademicStanding(val label: String, val description: String) {
    DEANS_LIST(
        "Dean's Honour List (First Class Track)",
        "Exceptional academic excellence! Eligible for merit scholarships and priority research."
    ),
    GOOD_STANDING(
        "Good Academic Standing",
        "Your CGPA is above 1.50. Keep attending lectures and clearing General Studies courses."
    ),
    ACADEMIC_PROBATION(
        "ACADEMIC PROBATION (Senate Warning!)",
        "CGPA or Semester GPA dropped below 1.50! You must raise your GPA above 1.50 next semester or face withdrawal/extra year."
    )
}

object UnnAcademicSystem {

    fun defaultCoursesForStudent(department: String, level: Long, semester: Long): List<UnnCourseProgress> {
        val lvlPrefix = (level / 100L).coerceIn(1L, 5L)
        val semDigit = if (semester == 1L) "1" else "2"

        val deptCodePrefix = when {
            department.contains("Electronic") -> "ECE"
            department.contains("Microbiology") -> "MCB"
            department.contains("Computer") -> "COS"
            department.contains("Mass Comm") -> "MAC"
            department.contains("Political") -> "POL"
            department.contains("Law") -> "LAW"
            department.contains("Medicine") -> "MED"
            else -> "UNN"
        }

        val deptVenue = when (deptCodePrefix) {
            "POL", "MAC" -> "faculty_social_sciences" to "Faculty of Social Sciences LT"
            "ECE", "MCB" -> "carver_lt" to "Carver Building LT"
            "COS" -> "abuja_gs_lt" to "Abuja Building LT"
            else -> "paul_robeson_lt" to "Paul Robeson LT"
        }

        return listOf(
            UnnCourseProgress(
                code = "$deptCodePrefix ${lvlPrefix}0$semDigit",
                title = "Core $department Theory & Practice I",
                creditUnits = 4,
                venueId = deptVenue.first,
                venueName = deptVenue.second,
                isCore = true,
                isRegistered = true,
                mastery = 40
            ),
            UnnCourseProgress(
                code = "$deptCodePrefix ${lvlPrefix}1$semDigit",
                title = "Applied $department Methods & Lab",
                creditUnits = 3,
                venueId = deptVenue.first,
                venueName = deptVenue.second,
                isCore = true,
                isRegistered = true,
                mastery = 38
            ),
            UnnCourseProgress(
                code = "GS ${lvlPrefix}0$semDigit",
                title = if (semester == 1L) "Use of English & Research Skills" else "Nigerian Peoples, Culture & Philosophy",
                creditUnits = 2,
                venueId = "abuja_gs_lt",
                venueName = "Abuja Building & GS LT",
                isCore = true,
                isRegistered = true,
                mastery = 45
            ),
            UnnCourseProgress(
                code = "SOC ${lvlPrefix}0$semDigit",
                title = "Social Structure & Nigerian Politics",
                creditUnits = 3,
                venueId = "faculty_social_sciences",
                venueName = "Faculty of Social Sciences LT",
                isCore = false,
                isRegistered = true,
                mastery = 42
            ),
            UnnCourseProgress(
                code = "LIT ${lvlPrefix}0$semDigit",
                title = "African Literature & Critical Seminar",
                creditUnits = 3,
                venueId = "john_pepper_clark_lib",
                venueName = "John Pepper Clark Library Seminar Room",
                isCore = false,
                isRegistered = false,
                mastery = 35
            ),
            UnnCourseProgress(
                code = "$deptCodePrefix ${lvlPrefix}2$semDigit",
                title = "Special Topics & Quantitative Analysis",
                creditUnits = 3,
                venueId = "carver_lt",
                venueName = "Carver Building LT",
                isCore = false,
                isRegistered = false,
                mastery = 30
            )
        )
    }

    // Serialization into StudentCareer.relationships / metadata entries:
    // Format: "CRS|code|title|units|venueId|venueName|isCore|isReg|attended|caScore|caTaken|mastery|lastGrade"
    fun encodeCourse(c: UnnCourseProgress): String {
        val safeTitle = c.title.replace("|", "/")
        val safeVenue = c.venueName.replace("|", "/")
        return "CRS|${c.code}|$safeTitle|${c.creditUnits}|${c.venueId}|$safeVenue|${c.isCore}|${c.isRegistered}|${c.classesAttended}|${c.midTermCaScore}|${c.midTermTaken}|${c.mastery}|${c.lastGrade}"
    }

    fun decodeCourses(entries: List<String>, department: String, level: Long, semester: Long): List<UnnCourseProgress> {
        val parsed = entries.filter { it.startsWith("CRS|") }.mapNotNull { raw ->
            val p = raw.split("|")
            if (p.size >= 13) {
                UnnCourseProgress(
                    code = p[1],
                    title = p[2],
                    creditUnits = p[3].toIntOrNull() ?: 3,
                    venueId = p[4],
                    venueName = p[5],
                    isCore = p[6].toBooleanStrictOrNull() ?: true,
                    isRegistered = p[7].toBooleanStrictOrNull() ?: true,
                    classesAttended = p[8].toIntOrNull() ?: 0,
                    requiredClasses = 4,
                    midTermCaScore = p[9].toIntOrNull() ?: 0,
                    midTermTaken = p[10].toBooleanStrictOrNull() ?: false,
                    mastery = p[11].toIntOrNull() ?: 35,
                    lastGrade = p[12]
                )
            } else null
        }
        return parsed.ifEmpty { defaultCoursesForStudent(department, level, semester) }
    }

    fun evaluateAcademicStanding(cgpa: Double): AcademicStanding {
        return when {
            cgpa >= 4.50 -> AcademicStanding.DEANS_LIST
            cgpa < 1.50 -> AcademicStanding.ACADEMIC_PROBATION
            else -> AcademicStanding.GOOD_STANDING
        }
    }

    data class CourseGradeBreakdown(
        val code: String,
        val creditUnits: Int,
        val totalScore: Int,
        val letterGrade: String,
        val gradePoint: Int,
        val attendancePenaltyApplied: Boolean
    )

    data class DetailedExamOutcome(
        val courseGrades: List<CourseGradeBreakdown>,
        val semesterGpa: Double,
        val updatedCgpa: Double,
        val failedCoursesCount: Int,
        val standing: AcademicStanding,
        val summaryNarrative: String,
        val scholarshipNaira: Long
    )

    fun gradeRegisteredCourses(
        courses: List<UnnCourseProgress>,
        currentCgpa: Double,
        globalPrep: Long,
        sanity: Long,
        energy: Long,
        hasGsVault: Boolean,
        hasLamp: Boolean
    ): DetailedExamOutcome {
        val registered = courses.filter { it.isRegistered }
        if (registered.isEmpty()) {
            val penalizedCgpa = (currentCgpa * 0.65).coerceAtLeast(0.80)
            val rounded = (penalizedCgpa * 100.0).roundToInt() / 100.0
            return DetailedExamOutcome(
                courseGrades = emptyList(),
                semesterGpa = 0.0,
                updatedCgpa = rounded,
                failedCoursesCount = 2,
                standing = evaluateAcademicStanding(rounded),
                summaryNarrative = "PORTAL DISASTER! You failed to register any courses before exams and scored 0.00 GPA this semester!",
                scholarshipNaira = 0L
            )
        }

        val gearBonus = (if (hasGsVault) 6 else 0) + (if (hasLamp) 5 else 0)
        val wellnessBonus = ((sanity + energy) / 14L).toInt()

        var totalQualityPoints = 0
        var totalCreditUnits = 0
        var failedCount = 0

        val breakdowns = registered.map { course ->
            val caScore = if (course.midTermTaken) {
                course.midTermCaScore
            } else {
                // Auto-estimate lowered CA if student skipped mid-term
                ((course.mastery * 0.20f) + (globalPrep * 0.08f)).roundToInt().coerceIn(6, 22)
            }

            val attendancePenalty = !course.meetsAttendanceRequirement
            val rawExamOutOf70 = ((course.mastery * 0.42f) + (globalPrep * 0.24f) + gearBonus + wellnessBonus)
                .roundToInt()
                .coerceIn(12, 68)
            val penalizedExam = if (attendancePenalty) (rawExamOutOf70 - 16).coerceAtLeast(8) else rawExamOutOf70
            val total100 = (caScore + penalizedExam).coerceIn(0, 100)

            val (letter, gp) = when {
                total100 >= 70 -> "A" to 5
                total100 >= 60 -> "B" to 4
                total100 >= 50 -> "C" to 3
                total100 >= 45 -> "D" to 2
                total100 >= 40 -> "E" to 1
                else -> "F" to 0
            }

            if (gp == 0) failedCount++
            totalQualityPoints += gp * course.creditUnits
            totalCreditUnits += course.creditUnits

            CourseGradeBreakdown(
                code = course.code,
                creditUnits = course.creditUnits,
                totalScore = total100,
                letterGrade = letter,
                gradePoint = gp,
                attendancePenaltyApplied = attendancePenalty
            )
        }

        val semGpa = if (totalCreditUnits > 0) {
            ((totalQualityPoints.toDouble() / totalCreditUnits.toDouble()) * 100.0).roundToInt() / 100.0
        } else 0.0

        val newCgpa = (((currentCgpa * 0.55) + (semGpa * 0.45)) * 100.0).roundToInt() / 100.0
        val standing = evaluateAcademicStanding(newCgpa)
        val scholarship = if (semGpa >= 4.50 && failedCount == 0) 25000L else 0L

        val gradesSummary = breakdowns.joinToString(", ") { "${it.code}: ${it.letterGrade} (${it.totalScore}%)" }
        val narrative = when {
            standing == AcademicStanding.ACADEMIC_PROBATION ->
                "SENATE PROBATION WARNING! Semester GPA: ${String.format("%.2f", semGpa)} | CGPA: ${String.format("%.2f", newCgpa)} [$gradesSummary]. Raise your CGPA above 1.50 next semester!"
            semGpa >= 4.50 ->
                "DEAN'S HONOUR LIST! Semester GPA: ${String.format("%.2f", semGpa)} | CGPA: ${String.format("%.2f", newCgpa)} [$gradesSummary]. Merit Grant: +₦25,000!"
            else ->
                "Semester Results Released! GPA: ${String.format("%.2f", semGpa)} | CGPA: ${String.format("%.2f", newCgpa)} [$gradesSummary]."
        }

        return DetailedExamOutcome(
            courseGrades = breakdowns,
            semesterGpa = semGpa,
            updatedCgpa = newCgpa.coerceIn(0.0, 5.0),
            failedCoursesCount = failedCount,
            standing = standing,
            summaryNarrative = narrative,
            scholarshipNaira = scholarship
        )
    }
}
