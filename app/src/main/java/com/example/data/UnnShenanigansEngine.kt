package com.example.data

data class ShenaniganChoice(
    val label: String,
    val outcomeNarrative: String,
    val nairaDelta: Long = 0L,
    val energyDelta: Int = 0,
    val sanityDelta: Int = 0,
    val prepDelta: Int = 0,
    val credDelta: Int = 0,
    val cloutDelta: Int = 0,
    val badgeAwarded: String? = null
)

data class CampusShenaniganEvent(
    val id: String,
    val title: String,
    val category: String, // SHENANIGAN, POLITICS, ACADEMIC, SOCIAL
    val locationTag: String,
    val description: String,
    val choices: List<ShenaniganChoice>
)

object UnnShenanigansEngine {

    val allEvents = listOf(
        CampusShenaniganEvent(
            id = "gs_101_stampede",
            title = "The Great GS 101 CBT Stampede",
            category = "ACADEMIC",
            locationTag = "Nnamdi Azikiwe Library CBT Center",
            description = "3,000 Freshers are crammed outside the Library CBT wing under blazing Nsukka sun. Your batch is called, but the Course Rep of another faculty is sneaking his friends through the side door!",
            choices = listOf(
                ShenaniganChoice(
                    label = "Use Aluta voice & rally the queue to enforce order",
                    outcomeNarrative = "You bellowed 'Greatest Lions!' and forced the invigilators to open the main gate fairly. You wrote your CBT in peace and became an instant hero!",
                    energyDelta = -12,
                    sanityDelta = +6,
                    prepDelta = +10,
                    credDelta = +8,
                    cloutDelta = +14,
                    badgeAwarded = "GS Stampede Conqueror"
                ),
                ShenaniganChoice(
                    label = "Squeeze through the window gap like an Odim ninja",
                    outcomeNarrative = "Your shirt button popped, but you secured a working desktop PC before the server lagged and smashed 85%!",
                    energyDelta = -15,
                    sanityDelta = -4,
                    prepDelta = +14,
                    credDelta = +5
                ),
                ShenaniganChoice(
                    label = "Tip the portal cafe guy ₦2,000 for VIP batch re-slotting",
                    outcomeNarrative = "Smooth operator! You strolled into the afternoon AC batch with zero sweat.",
                    nairaDelta = -2000L,
                    energyDelta = +5,
                    sanityDelta = +10,
                    prepDelta = +10
                )
            )
        ),
        CampusShenaniganEvent(
            id = "zik_flats_water_crisis",
            title = "Midnight Water Tanker & Bucket Politics",
            category = "SHENANIGAN",
            locationTag = "Zik's Flats / Okpara Hall Courtyard",
            description = "The borehole pump hasn't run in two days, and you have an 8:00 AM lecture tomorrow. At 11:45 PM, someone yells 'WATER DEY RUN FOR BLOCK C!'",
            choices = listOf(
                ShenaniganChoice(
                    label = "Sprint downstairs with 4 buckets & defend your tap spot",
                    outcomeNarrative = "You debated Champions League football in the queue till 1:30 AM and hauled 4 full buckets upstairs. Pure Lion resilience!",
                    energyDelta = -16,
                    sanityDelta = +8,
                    credDelta = +6,
                    cloutDelta = +5,
                    badgeAwarded = "Zik's Flats Bucket General"
                ),
                ShenaniganChoice(
                    label = "Pay hostel 'Water Boys' ₦1,500 to deliver to your door",
                    outcomeNarrative = "Money stops nonsense! Your drums were filled while you revised lecture slides in bed.",
                    nairaDelta = -1500L,
                    energyDelta = +8,
                    sanityDelta = +10,
                    credDelta = +4
                ),
                ShenaniganChoice(
                    label = "Do 'French Bath' with sachet pure water & perfume",
                    outcomeNarrative = "Three sachets of pure water and heavy body spray saved the morning, though your roommate side-eyed you all day.",
                    nairaDelta = -300L,
                    energyDelta = +4,
                    sanityDelta = -6,
                    prepDelta = +5
                )
            )
        ),
        CampusShenaniganEvent(
            id = "impromptu_tear_paper",
            title = "'Tear a Sheet of Paper!' — Impromptu Test",
            category = "ACADEMIC",
            locationTag = "Abuja Building Lecture Theatre",
            description = "The strictest lecturer in the faculty walked in 15 minutes early, locked the back door, and barked: 'Everybody tear a sheet of paper, write your Reg Number, and answer Question 1 in 5 minutes!'",
            choices = listOf(
                ShenaniganChoice(
                    label = "Unleash your Night Class cramming & write 2 full pages",
                    outcomeNarrative = "Your pen was smoking! You even derived the bonus equation and donated extra foolscap sheets to your row.",
                    energyDelta = -10,
                    sanityDelta = +12,
                    prepDelta = +16,
                    credDelta = +8,
                    badgeAwarded = "Impromptu Quiz Assassin"
                ),
                ShenaniganChoice(
                    label = "Whisper tactical teamwork with the Efiwe sitting beside you",
                    outcomeNarrative = "Smooth peripheral vision and coded whispers saved your continuous assessment score!",
                    energyDelta = -5,
                    sanityDelta = +4,
                    prepDelta = +10,
                    credDelta = +4
                ),
                ShenaniganChoice(
                    label = "Start a Course Rep plea about 'Network & Portal Registration'",
                    outcomeNarrative = "You stood up boldly and convinced the lecturer to convert the test into a take-home group assignment! The hall erupted in applause.",
                    energyDelta = -6,
                    sanityDelta = +10,
                    prepDelta = +6,
                    credDelta = +14,
                    cloutDelta = +15
                )
            )
        ),
        CampusShenaniganEvent(
            id = "sug_manifesto_drama",
            title = "Freedom Square Manifesto Night Showdown",
            category = "POLITICS",
            locationTag = "Freedom Square",
            description = "It's SUG Election week! Two rival hostel coalitions (Bello/Eni Njoku vs Zik's Flats/Off-Campus) are deadlocked, and both presidential camps want your public endorsement.",
            choices = listOf(
                ShenaniganChoice(
                    label = "Demand a student welfare charter & endorse the Reform Camp",
                    outcomeNarrative = "Your fiery speech went viral on UNN WhatsApp status TVs! Your political clout skyrocketed across every faculty.",
                    energyDelta = -10,
                    sanityDelta = +8,
                    credDelta = +14,
                    cloutDelta = +20,
                    badgeAwarded = "Freedom Square Kingmaker"
                ),
                ShenaniganChoice(
                    label = "Collect 'Mobilization Logistics' (₦15,000) from the incumbent camp",
                    outcomeNarrative = "Your bank alert chimed with ₦15,000 'water money', though purist comrades whispered about your behind-the-scenes deal.",
                    nairaDelta = +15000L,
                    energyDelta = -6,
                    sanityDelta = -4,
                    credDelta = -3,
                    cloutDelta = +10
                ),
                ShenaniganChoice(
                    label = "Stay neutral & sell shawarma and drinks to the crowd",
                    outcomeNarrative = "While politicians shouted 'Aluta!', you sold out 4 coolers of drinks at Freedom Square and made ₦10,000 profit!",
                    nairaDelta = +10000L,
                    energyDelta = -14,
                    sanityDelta = +6,
                    credDelta = +8
                )
            )
        ),
        CampusShenaniganEvent(
            id = "faculty_jeans_carnival",
            title = "Departmental Week: Jeans Carnival & Bush Bar Night",
            category = "SOCIAL",
            locationTag = "Paul Robeson / Bush Bar",
            description = "It's Faculty Cultural & Jeans Carnival Day! Customized rugged denim, music blasting from giant speakers, and the campus paparazzi at Ekpo Ref are snapping the freshest Lions and Lionesses.",
            choices = listOf(
                ShenaniganChoice(
                    label = "Pull up in custom painted denim & sponsor a table (₦6,000)",
                    outcomeNarrative = "Unmatched steeze! Every campus blog posted your fit, and your social hierarchy status climbed to the roof.",
                    nairaDelta = -6000L,
                    energyDelta = -10,
                    sanityDelta = +20,
                    credDelta = +22,
                    cloutDelta = +8,
                    badgeAwarded = "Jeans Carnival Royalty"
                ),
                ShenaniganChoice(
                    label = "Join the Cultural Dance troupe & vibe on a budget (₦1,000)",
                    outcomeNarrative = "You danced Ogene and Atilogwu with your coursemates, ate free party Jollof, and had the time of your life!",
                    nairaDelta = -1000L,
                    energyDelta = -8,
                    sanityDelta = +16,
                    credDelta = +12,
                    cloutDelta = +6
                ),
                ShenaniganChoice(
                    label = "Skip the carnival to revise in an empty lecture hall",
                    outcomeNarrative = "Quiet productivity! You covered three textbook chapters while music echoed in the distance.",
                    energyDelta = -8,
                    sanityDelta = -2,
                    prepDelta = +18,
                    credDelta = -4
                )
            )
        ),
        CampusShenaniganEvent(
            id = "october_rush_craziness",
            title = "Hostel Cooking Patrol & The Smoking Hotplate",
            category = "SHENANIGAN",
            locationTag = "Mary Slessor / Eni Njoku Hostel",
            description = "Your Sunday stew is bubbling nicely on an electric hotplate when somebody screams down the corridor: 'HALL PORTERS AND SECURITY DEY DO ROOM-TO-ROOM INSPECTION!'",
            choices = listOf(
                ShenaniganChoice(
                    label = "Hide the hot pot inside your wardrobe & spray air freshener",
                    outcomeNarrative = "Stealth level 100! The porters sniffed the air, complimented your 'citrus perfume', and walked past. Your wardrobe smells like fried Titus fish though!",
                    energyDelta = -5,
                    sanityDelta = +12,
                    credDelta = +10
                ),
                ShenaniganChoice(
                    label = "Dish a generous plate of stew + rice for the Chief Porter",
                    outcomeNarrative = "Diplomacy in action! The Chief Porter smiled, blessed your CGPA, and warned you to cook quietly next time.",
                    nairaDelta = -1000L,
                    energyDelta = +10,
                    sanityDelta = +10,
                    credDelta = +8,
                    cloutDelta = +8
                ),
                ShenaniganChoice(
                    label = "Invoke Hall Government protocol & negotiate immunity",
                    outcomeNarrative = "You cited student welfare clauses with the Hall Governor on speed dial. Not a single pot was confiscated on your floor!",
                    energyDelta = -6,
                    sanityDelta = +8,
                    credDelta = +12,
                    cloutDelta = +14
                )
            )
        ),
        CampusShenaniganEvent(
            id = "nepa_transformer_blackout",
            title = "Odim & Hilltop Transformer Blackout Before Exams",
            category = "ACADEMIC",
            locationTag = "Hilltop / Odenigwe / Carver Building",
            description = "Just 48 hours before semester exams, the transformer blows! Phone batteries are at 9%, laptops are dead, and everyone is migrating toward Carver Building and the Library.",
            choices = listOf(
                ShenaniganChoice(
                    label = "Camp at Carver Building Night Class with a rechargeable lamp",
                    outcomeNarrative = "True Lion grit! You studied till 4:00 AM surrounded by 500 determined students and mastered every past question.",
                    energyDelta = -20,
                    sanityDelta = -5,
                    prepDelta = +22,
                    credDelta = +6,
                    badgeAwarded = "Night Class Legend"
                ),
                ShenaniganChoice(
                    label = "Pay ₦1,000 at SUB charging kiosk & form a study group",
                    outcomeNarrative = "While your phone and laptop charged at SUB, your study group decoded the toughest departmental questions together.",
                    nairaDelta = -1000L,
                    energyDelta = -10,
                    sanityDelta = +8,
                    prepDelta = +16,
                    credDelta = +6
                ),
                ShenaniganChoice(
                    label = "Rent a small Tiger (" + "\"I Better Pass My Neighbour\"" + ") generator for your lodge",
                    outcomeNarrative = "Half your floor came to plug their phones in your room! You charged ₦200 per socket and recouped your fuel money.",
                    nairaDelta = +2500L,
                    energyDelta = -6,
                    sanityDelta = +10,
                    prepDelta = +12,
                    credDelta = +14,
                    cloutDelta = +8
                )
            )
        ),
        CampusShenaniganEvent(
            id = "lecturer_handout_dilemma",
            title = "The 'Compulsory' Textbook & Sorting Whisper",
            category = "ACADEMIC",
            locationTag = "Departmental Office Corridor",
            description = "A notoriously tough course coordinator announces a ₦5,500 practical manual that carries 20 marks of Continuous Assessment, while shady seniors whisper about 'sorting' the list.",
            choices = listOf(
                ShenaniganChoice(
                    label = "Buy the official manual legitimately & submit the practical early",
                    outcomeNarrative = "Clean hands and solid marks! Your Reg Number was ticked in green ink on the master list.",
                    nairaDelta = -5500L,
                    energyDelta = -6,
                    sanityDelta = +10,
                    prepDelta = +18,
                    credDelta = +4
                ),
                ShenaniganChoice(
                    label = "Organize a class bulk purchase discount with the lecturer",
                    outcomeNarrative = "You negotiated a 30% discount for indigent students and earned massive respect from both the class and the HOD!",
                    nairaDelta = -3800L,
                    energyDelta = -10,
                    sanityDelta = +12,
                    prepDelta = +15,
                    credDelta = +12,
                    cloutDelta = +14
                ),
                ShenaniganChoice(
                    label = "Rely purely on Nnamdi Azikiwe Library reference copies",
                    outcomeNarrative = "Risky move, but you hand-copied all the diagrams in the library and aced the lab defense on pure brainpower!",
                    energyDelta = -18,
                    sanityDelta = -4,
                    prepDelta = +16,
                    credDelta = +6
                )
            )
        )
    )

    fun pickEventForWeek(level: Long, semester: Long, week: Long): CampusShenaniganEvent {
        val index = ((level / 100L) * 7L + semester * 5L + week * 3L).toInt().mod(allEvents.size)
        return allEvents[index]
    }

    data class SemesterExamResult(
        val semesterGpa: Double,
        val newCgpa: Double,
        val newCarryOvers: Long,
        val narrative: String,
        val scholarshipBonusNaira: Long
    )

    fun evaluateSemesterExams(
        currentCgpa: Double,
        academicPrep: Long,
        sanity: Long,
        energy: Long,
        hasGsVault: Boolean,
        hasLamp: Boolean,
        drObinnaRapport: Int,
        chukaRapport: Int
    ): SemesterExamResult {
        val itemBonus = (if (hasGsVault) 8 else 0) + (if (hasLamp) 5 else 0)
        val npcBonus = ((drObinnaRapport + chukaRapport) / 12).coerceAtMost(12)
        val wellnessFactor = ((sanity + energy) / 10L).toInt()
        val effectiveScore = (academicPrep.toInt() + itemBonus + npcBonus + wellnessFactor).coerceIn(15, 115)

        val semesterGpa = when {
            effectiveScore >= 98 -> 4.92
            effectiveScore >= 88 -> 4.68
            effectiveScore >= 76 -> 4.25
            effectiveScore >= 64 -> 3.70
            effectiveScore >= 52 -> 3.10
            effectiveScore >= 40 -> 2.45
            else -> 1.65
        }

        val blendedCgpa = ((currentCgpa * 0.55) + (semesterGpa * 0.45)).coerceIn(1.0, 5.0)
        val roundedCgpa = Math.round(blendedCgpa * 100.0) / 100.0

        val carryOverAdded = if (effectiveScore < 45) 1L else 0L
        val scholarship = if (semesterGpa >= 4.65) 25000L else 0L

        val narrative = when {
            semesterGpa >= 4.65 ->
                "FIRST CLASS ROAR! You smashed Semester Exams with a ${String.format("%.2f", semesterGpa)} GPA! The Dean's Honour List awarded you a ₦25,000 merit grant!"
            semesterGpa >= 3.60 ->
                "SOLID 2:1 PERFORMANCE! You survived the exam halls at Carver & Ekpo Ref with a ${String.format("%.2f", semesterGpa)} GPA and zero carry-overs."
            semesterGpa >= 2.50 ->
                "Phew! Results pasted on the departmental noticeboard show a ${String.format("%.2f", semesterGpa)} GPA. Time to tighten your belt next semester."
            else ->
                "RESULT SHOCK! The noticeboard crowd was brutal — ${String.format("%.2f", semesterGpa)} GPA and 1 Carry-Over course added. Hit Night Class hard next semester!"
        }

        return SemesterExamResult(
            semesterGpa = semesterGpa,
            newCgpa = roundedCgpa,
            newCarryOvers = carryOverAdded,
            narrative = narrative,
            scholarshipBonusNaira = scholarship
        )
    }

    fun degreeClassification(cgpa: Double, carryOvers: Long): String {
        if (carryOvers > 0L) return "Extra Year / Pending Clearance ($carryOvers Carry-Over)"
        return when {
            cgpa >= 4.50 -> "First Class Honours"
            cgpa >= 3.50 -> "Second Class Upper (2:1)"
            cgpa >= 2.40 -> "Second Class Lower (2:2)"
            cgpa >= 1.50 -> "Third Class Honours"
            else -> "Pass Degree"
        }
    }
}
