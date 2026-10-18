package com.example.data

enum class RelationshipType(val label: String) {
    FRIEND("Friend"),
    ROMANCE("Romantic Interest"),
    RIVAL("Campus Rival"),
    MENTOR("Academic Mentor")
}

data class StudentClub(
    val id: String,
    val name: String,
    val category: String, // Religious, Academic, Cultural, Tech, Social
    val headquartersLandmarkId: String,
    val duesNaira: Long,
    val weeklyPerkDescription: String,
    val credBonus: Int,
    val cloutBonus: Int,
    val sanityBonus: Int,
    val prepBonus: Int
)

data class SocialClique(
    val id: String,
    val name: String,
    val motto: String,
    val turfLandmark: String,
    val requirementDescription: String,
    val minCred: Int,
    val minClout: Int,
    val minCgpa: Double,
    val minNaira: Long,
    val perkSummary: String
)

data class CampusSocialEvent(
    val id: String,
    val title: String,
    val venueName: String,
    val venueLandmarkId: String,
    val category: String, // Concert, Party, Cultural, Pageant, Fellowship
    val ticketCostNaira: Long,
    val energyDelta: Int,
    val sanityDelta: Int,
    val credDelta: Int,
    val cloutDelta: Int,
    val description: String,
    val highlightLog: String
)

data class RichCampusNpc(
    val id: String,
    val name: String,
    val title: String,
    val hostelOrOffice: String,
    val cliqueName: String,
    val defaultType: RelationshipType,
    val defaultAffinity: Int, // -100 to +100
    val bio: String
)

data class NpcRelationshipState(
    val npc: RichCampusNpc,
    val type: RelationshipType,
    val affinity: Int // -100 to +100
) {
    val statusBadge: String
        get() = when (type) {
            RelationshipType.ROMANCE -> when {
                affinity >= 85 -> "Soulmate / Campus Couple Goals"
                affinity >= 55 -> "Steady Bae / Boo"
                else -> " Situationship / Crush"
            }
            RelationshipType.RIVAL -> when {
                affinity <= -60 -> "Sworn Nemesis"
                affinity < 0 -> "Fierce Rival"
                else -> "Respected Frenemy"
            }
            RelationshipType.MENTOR -> when {
                affinity >= 75 -> "Star Protege"
                affinity >= 45 -> "Trusted Mentee"
                else -> "Known Student"
            }
            RelationshipType.FRIEND -> when {
                affinity >= 80 -> "Ride-or-Die Bestie"
                affinity >= 45 -> "Solid Coursemate"
                else -> "Acquaintance"
            }
        }
}

object UnnSocialAndClubsData {

    val clubs: List<StudentClub> = listOf(
        StudentClub(
            id = "club_nifes_casor",
            name = "Campus Christian Fellowship (NIFES / CASOR / NFCS)",
            category = "Religious Group",
            headquartersLandmarkId = "paa_auditorium",
            duesNaira = 1000L,
            weeklyPerkDescription = "Uplifts Sanity, provides free exam prayer & past-question tutorials, and builds trusted welfare networks.",
            credBonus = 6,
            cloutBonus = 8,
            sanityBonus = 18,
            prepBonus = 8
        ),
        StudentClub(
            id = "club_fassa_debate",
            name = "UNN Debate, Moot & Academic Society",
            category = "Academic Association",
            headquartersLandmarkId = "faculty_social_sciences",
            duesNaira = 2000L,
            weeklyPerkDescription = "Sharpens lecture mastery, unlocks inter-faculty debate trophies at Faculty of Social Sciences, and impresses lecturers.",
            credBonus = 10,
            cloutBonus = 14,
            sanityBonus = 4,
            prepBonus = 16
        ),
        StudentClub(
            id = "club_kegites_cultural",
            name = "Palm Wine Drinkards & Cultural Ensemble (Kegites)",
            category = "Cultural Club",
            headquartersLandmarkId = "sub_building",
            duesNaira = 1500L,
            weeklyPerkDescription = "Legendary Ogene & Atilogwu gyration behind SUB! Massive boost to Street Cred, Sanity, and grassroots hostel popularity.",
            credBonus = 16,
            cloutBonus = 12,
            sanityBonus = 16,
            prepBonus = 0
        ),
        StudentClub(
            id = "club_jpc_literary",
            name = "J.P. Clark Literary & Theatre Arts Guild",
            category = "Creative & Literary",
            headquartersLandmarkId = "john_pepper_clark_lib",
            duesNaira = 1500L,
            weeklyPerkDescription = "Stage plays at Paul Robeson and poetry slams at John Pepper Clark Library (+Prep & +Street Cred).",
            credBonus = 14,
            cloutBonus = 8,
            sanityBonus = 10,
            prepBonus = 12
        ),
        StudentClub(
            id = "club_roar_tech",
            name = "Roar Nigeria Tech & Founders Community",
            category = "Innovation & Tech",
            headquartersLandmarkId = "abuja_gs_lt",
            duesNaira = 3000L,
            weeklyPerkDescription = "Hackathons, remote startup gigs, and dev meetups between Abuja Building and Hilltop.",
            credBonus = 14,
            cloutBonus = 10,
            sanityBonus = 6,
            prepBonus = 12
        )
    )

    val socialCliques: List<SocialClique> = listOf(
        SocialClique(
            id = "clique_efiwe",
            name = "Efiwe Scholars Syndicate",
            motto = "First Class or Nothing — See You at J.P. Clark Library",
            turfLandmark = "John Pepper Clark & Azikiwe Libraries",
            requirementDescription = "CGPA 4.20+ • Academic Prep 50+",
            minCred = 15,
            minClout = 10,
            minCgpa = 4.20,
            minNaira = 0L,
            perkSummary = "+12 Exam Prep on study actions & automatic respect from faculty lecturers."
        ),
        SocialClique(
            id = "clique_aluta",
            name = "Freedom Square Aluta Vanguard",
            motto = "Greatest Lions & Lionesses! Aluta Continua!",
            turfLandmark = "Freedom Square, SUB & Okija Hall",
            requirementDescription = "Political Clout 45+ • Street Cred 35+",
            minCred = 35,
            minClout = 45,
            minCgpa = 2.00,
            minNaira = 0L,
            perkSummary = "Dominates Hall & SUG elections with bloc votes from Okija, Bello & Zik's Flats."
        ),
        SocialClique(
            id = "clique_hilltop_royals",
            name = "Hilltop & Bush Bar Elites",
            motto = "Soft Life Only — No Bucket Queues Allowed",
            turfLandmark = "Hilltop Second Gate & Mary Slessor Boulevard",
            requirementDescription = "Street Cred 55+ • Pocket ₦35,000+",
            minCred = 55,
            minClout = 25,
            minCgpa = 2.00,
            minNaira = 35000L,
            perkSummary = "VIP access to Hilltop parties, PAA front-row concerts, and celebrity status."
        ),
        SocialClique(
            id = "clique_fellowship",
            name = "Fellowship Pillars & Welfare Core",
            motto = "Grace, Excellence & Integrity in the Den",
            turfLandmark = "PAA Auditorium & Hostel Chapels",
            requirementDescription = "Sanity 60+ • CGPA 3.00+",
            minCred = 25,
            minClout = 20,
            minCgpa = 3.00,
            minNaira = 0L,
            perkSummary = "High Sanity recovery, loyal friendship circle, and zero campus scandal."
        )
    )

    val socialEvents: List<CampusSocialEvent> = listOf(
        CampusSocialEvent(
            id = "event_paa_concert",
            title = "PAA Afrobeats & Campus Star Concert",
            venueName = "Princess Alexandra Auditorium (PAA)",
            venueLandmarkId = "paa_auditorium",
            category = "Concert",
            ticketCostNaira = 5000L,
            energyDelta = -16,
            sanityDelta = +24,
            credDelta = +18,
            cloutDelta = +8,
            description = "A packed night of live Afrobeats, campus hype-men, and dazzling stage lights inside PAA!",
            highlightLog = "Turned up at the PAA Afrobeats Star Concert! The entire auditorium was shaking."
        ),
        CampusSocialEvent(
            id = "event_hilltop_party",
            title = "Hilltop Penthouse & Poolside Rave",
            venueName = "Hilltop Off-Campus VIP Lounge",
            venueLandmarkId = "hilltop_second_gate",
            category = "Party",
            ticketCostNaira = 8000L,
            energyDelta = -20,
            sanityDelta = +20,
            credDelta = +24,
            cloutDelta = +10,
            description = "Exclusive Friday night link-up for Hilltop big boys, Mary Slessor fashionistas, and campus influencers.",
            highlightLog = "Shut down the Hilltop Penthouse Party! Your campus Street Cred went through the roof."
        ),
        CampusSocialEvent(
            id = "event_okija_slessor_bash",
            title = "Okija & Mary Slessor Inter-Hostel Night Fiesta",
            venueName = "Okija Hall & Mary Slessor Boulevard",
            venueLandmarkId = "okija_hall",
            category = "Hostel Bash",
            ticketCostNaira = 2500L,
            energyDelta = -14,
            sanityDelta = +18,
            credDelta = +14,
            cloutDelta = +14,
            description = "DJ battles, barbecue suya, dancing competitions, and romance sparks between Okija and Mary Slessor residents.",
            highlightLog = "Vibed at the Okija & Mary Slessor Inter-Hostel Fiesta till curfew!"
        ),
        CampusSocialEvent(
            id = "event_sub_gyration",
            title = "SUB Palmwine & Cultural Gyration Night",
            venueName = "Students' Union Building (SUB) Arena",
            venueLandmarkId = "sub_building",
            category = "Cultural",
            ticketCostNaira = 1500L,
            energyDelta = -10,
            sanityDelta = +20,
            credDelta = +12,
            cloutDelta = +12,
            description = "Traditional Ogene rhythms, roasted plantain, palmwine calabashes, and comrade solidarity chants at SUB.",
            highlightLog = "Joined the Cultural Gyration Night at SUB — pure African heritage and zero stress!"
        )
    )

    val npcs: List<RichCampusNpc> = listOf(
        RichCampusNpc(
            id = "Amaka_Hilltop",
            name = "Amaka 'Drip Queen' Eze",
            title = "Miss FASSA & Mary Slessor Socialite",
            hostelOrOffice = "Mary Slessor Hall",
            cliqueName = "Hilltop & Bush Bar Elites",
            defaultType = RelationshipType.ROMANCE,
            defaultAffinity = 35,
            bio = "Turns heads from Mary Slessor Hall to Faculty of Social Sciences. Loves acoustic nights, shawarma dates, and ambitious Lions."
        ),
        RichCampusNpc(
            id = "Kelechi_Tech",
            name = "Kelechi 'Crypto & Code' Okafor",
            title = "Hilltop Tech Bro & Startup Founder",
            hostelOrOffice = "Hilltop Self-Con",
            cliqueName = "Hilltop & Bush Bar Elites",
            defaultType = RelationshipType.ROMANCE,
            defaultAffinity = 30,
            bio = "Codes fintech apps at Roar Hub by day and hosts rooftop game nights at Hilltop by weekend."
        ),
        RichCampusNpc(
            id = "Chuka_Efiwe",
            name = "Chuka '5.0' Nnamdi",
            title = "J.P. Clark Library Study Captain",
            hostelOrOffice = "John Pepper Clark Library",
            cliqueName = "Efiwe Scholars Syndicate",
            defaultType = RelationshipType.FRIEND,
            defaultAffinity = 45,
            bio = "Has solved every past question since 2010. A loyal coursemate who will save you a library seat and explain tough theorems."
        ),
        RichCampusNpc(
            id = "Zainab_Slessor",
            name = "Zainab 'Vibes' Mohammed",
            title = "Mary Slessor Floor Rep & Vlogger",
            hostelOrOffice = "Mary Slessor Hall",
            cliqueName = "PAA Entertainment & Creative Stars",
            defaultType = RelationshipType.FRIEND,
            defaultAffinity = 40,
            bio = "Runs the biggest campus TikTok & vlog channel. Always knows where the best parties and free food are happening."
        ),
        RichCampusNpc(
            id = "Comrade_Kalu",
            name = "Comrade 'Tornado' Kalu",
            title = "Okija Hall Political Heavyweight",
            hostelOrOffice = "Okija Hall",
            cliqueName = "Freedom Square Aluta Vanguard",
            defaultType = RelationshipType.FRIEND,
            defaultAffinity = 35,
            bio = "Commands the megaphone at Okija Hall and Freedom Square. A vital ally—or dangerous opponent—in SUG politics."
        ),
        RichCampusNpc(
            id = "Dike_Rival",
            name = "Barrister Dike 'Macho' Nwankwo",
            title = "Rival SUG Aspirant & Debate Champion",
            hostelOrOffice = "Faculty of Social Sciences",
            cliqueName = "Freedom Square Aluta Vanguard",
            defaultType = RelationshipType.RIVAL,
            defaultAffinity = -35,
            bio = "Arrogant, eloquent, and determined to outshine you in both CGPA rankings and Student Union elections."
        ),
        RichCampusNpc(
            id = "Dr_Obinna",
            name = "Dr. Obinna Agu",
            title = "Senior Lecturer & Faculty Exam Officer",
            hostelOrOffice = "Faculty of Social Sciences / Carver",
            cliqueName = "Efiwe Scholars Syndicate",
            defaultType = RelationshipType.MENTOR,
            defaultAffinity = 30,
            bio = "No-nonsense academic who respects 75%+ lecture attendance, original term papers, and disciplined students."
        )
    )

    // Encode/Decode helpers stored in StudentCareer.relationships
    // Format for NPC: "REL|<id>|<TYPE>|<affinity>"
    // Format for Club: "CLUB|<clubId>"
    // Format for Clique: "CLIQUE|<cliqueName>"
    // Format for Location: "LOC|<landmarkId>"
    fun parseNpcStates(rawEntries: List<String>): List<NpcRelationshipState> {
        val mapById = mutableMapOf<String, Pair<RelationshipType, Int>>()
        rawEntries.forEach { entry ->
            if (entry.startsWith("REL|")) {
                val p = entry.split("|")
                if (p.size >= 4) {
                    val id = p[1]
                    val type = runCatching { RelationshipType.valueOf(p[2]) }.getOrDefault(RelationshipType.FRIEND)
                    val score = p[3].toIntOrNull() ?: 30
                    mapById[id] = type to score
                }
            } else if (entry.contains(":") && !entry.contains("|")) {
                // Backward compatibility with v1 "NpcId:Score" format
                val p = entry.split(":")
                if (p.size == 2) {
                    val id = p[0]
                    val score = p[1].toIntOrNull() ?: 30
                    val defaultNpc = npcs.firstOrNull { it.id == id }
                    mapById[id] = (defaultNpc?.defaultType ?: RelationshipType.FRIEND) to score
                }
            }
        }

        return npcs.map { npc ->
            val saved = mapById[npc.id]
            NpcRelationshipState(
                npc = npc,
                type = saved?.first ?: npc.defaultType,
                affinity = (saved?.second ?: npc.defaultAffinity).coerceIn(-100, 100)
            )
        }
    }

    fun parseJoinedClubs(rawEntries: List<String>): Set<String> {
        return rawEntries.filter { it.startsWith("CLUB|") }.map { it.removePrefix("CLUB|") }.toSet()
    }

    fun parseActiveClique(rawEntries: List<String>): String {
        return rawEntries.firstOrNull { it.startsWith("CLIQUE|") }?.removePrefix("CLIQUE|")
            ?: "Unaligned Fresher"
    }

    fun parseCurrentLocation(rawEntries: List<String>): String {
        return rawEntries.firstOrNull { it.startsWith("LOC|") }?.removePrefix("LOC|")
            ?: "sub_building"
    }
}
