package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AcademicStanding
import com.example.data.StudentCareer
import com.example.data.UnnAcademicSystem
import com.example.data.UnnCourseProgress
import com.example.ui.theme.AlertCrimson
import com.example.ui.theme.CloutCyan
import com.example.ui.theme.LionGold
import com.example.ui.theme.LionGoldBright
import com.example.ui.theme.UnnDeepGreen
import com.example.ui.theme.UnnEmerald
import com.example.ui.theme.UnnMintAccent

@Composable
fun AcademicProgressionTabContent(
    career: StudentCareer,
    courses: List<UnnCourseProgress>,
    onToggleCourseRegistration: (String) -> Unit,
    onAttendCourseLecture: (UnnCourseProgress) -> Unit,
    onStudyAtJpClarkLibrary: () -> Unit,
    onSitMidTermExams: () -> Unit,
    onSitFinalSemesterExams: () -> Unit
) {
    val standing = remember(career.cgpa) {
        UnnAcademicSystem.evaluateAcademicStanding(career.cgpa)
    }
    val registeredCourses = remember(courses) { courses.filter { it.isRegistered } }
    val totalRegisteredUnits = remember(registeredCourses) { registeredCourses.sumOf { it.creditUnits } }
    val maxAllowedUnits = if (standing == AcademicStanding.ACADEMIC_PROBATION) 15 else 21
    val allMidTermsDone = remember(registeredCourses) {
        registeredCourses.isNotEmpty() && registeredCourses.all { it.midTermTaken }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Academic Standing & Probation / Dean's List Status Card
        item {
            val cardColor = when (standing) {
                AcademicStanding.DEANS_LIST -> UnnEmerald
                AcademicStanding.GOOD_STANDING -> MaterialTheme.colorScheme.surfaceVariant
                AcademicStanding.ACADEMIC_PROBATION -> AlertCrimson.copy(alpha = 0.24f)
            }
            val borderColor = when (standing) {
                AcademicStanding.DEANS_LIST -> LionGold
                AcademicStanding.GOOD_STANDING -> UnnMintAccent.copy(alpha = 0.5f)
                AcademicStanding.ACADEMIC_PROBATION -> AlertCrimson
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                border = BorderStroke(1.5.dp, borderColor)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (standing) {
                                    AcademicStanding.DEANS_LIST -> Icons.Default.WorkspacePremium
                                    AcademicStanding.ACADEMIC_PROBATION -> Icons.Default.Warning
                                    AcademicStanding.GOOD_STANDING -> Icons.Default.School
                                },
                                contentDescription = null,
                                tint = if (standing == AcademicStanding.ACADEMIC_PROBATION) AlertCrimson else LionGold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = standing.label,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (standing == AcademicStanding.ACADEMIC_PROBATION) AlertCrimson else LionGoldBright
                            )
                        }
                        Text(
                            text = "CGPA ${String.format("%.2f", career.cgpa)} / 5.00",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = standing.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Registered Load: $totalRegisteredUnits / $maxAllowedUnits Credit Units",
                            style = MaterialTheme.typography.labelLarge,
                            color = UnnMintAccent
                        )
                        Text(
                            text = "Carry-Overs: ${career.carryOvers}",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (career.carryOvers > 0) AlertCrimson else LionGoldBright
                        )
                    }
                }
            }
        }

        // 2. Semester Course Registration & Attendance Tracker
        item {
            Text(
                text = "${career.level}L Semester ${career.semester} Course Portal & Attendance",
                style = MaterialTheme.typography.headlineMedium,
                color = LionGold
            )
            Text(
                text = "UNN Senate Rule: You need at least 75% lecture attendance (3/4 classes) per registered course to avoid an Exam Query (-16 marks penalty).",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(courses, key = { it.code }) { course ->
            CourseProgressionCard(
                course = course,
                isGraduated = career.isGraduated,
                onToggleRegistration = { onToggleCourseRegistration(course.code) },
                onAttendLecture = { onAttendCourseLecture(course) }
            )
        }

        // 3. Library Study, Mid-Term CA & Final Semester Exam Console
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, LionGold)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = LionGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Exam Prep, Mid-Term CA & Final Exams",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = "Study at John Pepper Clark Library to boost mastery across all registered courses, take your 30-mark Mid-Term Continuous Assessment, then sit the 70-mark Final Semester Exams!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedButton(
                        onClick = onStudyAtJpClarkLibrary,
                        enabled = !career.isGraduated,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("academic_study_jpc_library_button"),
                        border = BorderStroke(1.dp, LionGold)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = LionGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Group Revision at J.P. Clark Library (+12 Mastery All Courses)",
                            color = LionGoldBright
                        )
                    }

                    Button(
                        onClick = onSitMidTermExams,
                        enabled = !career.isGraduated && !allMidTermsDone && registeredCourses.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("sit_midterm_ca_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UnnEmerald,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.AssignmentTurnedIn, contentDescription = null, tint = LionGoldBright)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (allMidTermsDone) "Mid-Term Continuous Assessment Completed (30 Marks)"
                            else "Write Mid-Term Continuous Assessment (30 Marks)",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = onSitFinalSemesterExams,
                        enabled = !career.isGraduated && registeredCourses.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("sit_final_semester_exams_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LionGold,
                            contentColor = UnnDeepGreen
                        )
                    ) {
                        Icon(Icons.Default.School, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sit Final Semester Examinations (70 Marks + GPA Grading)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun CourseProgressionCard(
    course: UnnCourseProgress,
    isGraduated: Boolean,
    onToggleRegistration: () -> Unit,
    onAttendLecture: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (course.isRegistered) MaterialTheme.colorScheme.surfaceVariant
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        border = if (course.isRegistered) BorderStroke(1.dp, LionGold.copy(alpha = 0.45f)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    IconButton(
                        onClick = onToggleRegistration,
                        enabled = !isGraduated,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("toggle_reg_${course.code.replace(" ", "_")}")
                    ) {
                        Icon(
                            imageVector = if (course.isRegistered) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                            contentDescription = "Toggle Course Registration",
                            tint = if (course.isRegistered) LionGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${course.code} (${course.creditUnits} Units)",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (course.isRegistered) LionGoldBright else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (course.lastGrade != "-") {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = UnnEmerald,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "Last Grade: ${course.lastGrade}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = LionGoldBright,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = course.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Surface(
                    color = if (course.isCore) UnnEmerald else MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (course.isCore) "CORE" else "ELECTIVE",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (course.isCore) LionGoldBright else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "📍 Venue: ${course.venueName}",
                style = MaterialTheme.typography.labelMedium,
                color = UnnMintAccent
            )

            if (course.isRegistered) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Attendance: ${course.classesAttended}/${course.requiredClasses} (${course.attendancePercent}%)",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (course.meetsAttendanceRequirement) UnnMintAccent else AlertCrimson
                    )
                    Text(
                        text = if (course.midTermTaken) "CA: ${course.midTermCaScore}/30" else "CA: Pending",
                        style = MaterialTheme.typography.labelMedium,
                        color = LionGoldBright
                    )
                    Text(
                        text = "Mastery: ${course.mastery}%",
                        style = MaterialTheme.typography.labelMedium,
                        color = CloutCyan
                    )
                }

                LinearProgressIndicator(
                    progress = { (course.mastery / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (course.meetsAttendanceRequirement) UnnMintAccent else LionGold,
                    trackColor = MaterialTheme.colorScheme.surface,
                    strokeCap = StrokeCap.Round
                )

                Button(
                    onClick = onAttendLecture,
                    enabled = !isGraduated,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("attend_lecture_${course.code.replace(" ", "_")}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LionGold,
                        contentColor = UnnDeepGreen
                    )
                ) {
                    Text(
                        text = if (course.classesAttended >= course.requiredClasses) {
                            "Attend Extra Revision Lecture (+Mastery)"
                        } else {
                            "Attend Lecture at ${course.venueName} (+Attendance & Mastery)"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    text = "Check the box above to add this course to your semester registration.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
