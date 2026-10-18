package com.example.data

enum class CampusZone(val label: String) {
    ACADEMIC_CORE("Academic Core"),
    HOSTEL_RING("Hostel Ring"),
    STUDENT_CENTER("SUB & Central Arena"),
    LECTURE_THEATERS("Lecture Theaters"),
    GATES_AND_LODGES("Gates & Off-Campus")
}

data class UnnMapLandmark(
    val id: String,
    val name: String,
    val shortLabel: String,
    val zone: CampusZone,
    val xFraction: Float, // 0.08f .. 0.92f on map canvas
    val yFraction: Float, // 0.10f .. 0.90f on map canvas
    val description: String,
    val facilities: List<String>,
    val connectedTo: List<String>,
    val primaryActionLabel: String,
    val primaryActionCategory: String, // LECTURE, STUDY, HOSTEL, SOCIAL, POLITICS, HUSTLE
    val energyDelta: Int,
    val sanityDelta: Int,
    val prepDelta: Int,
    val credDelta: Int,
    val cloutDelta: Int,
    val costNaira: Long
)

object UnnInteractiveMapData {

    val landmarks: List<UnnMapLandmark> = listOf(
        UnnMapLandmark(
            id = "faculty_social_sciences",
            name = "Faculty of Social Sciences (FASSA)",
            shortLabel = "Social Sciences",
            zone = CampusZone.ACADEMIC_CORE,
            xFraction = 0.34f,
            yFraction = 0.36f,
            description = "Bustling quadrangles housing Political Science, Sociology, Economics, Psychology, and Public Administration lecture rooms.",
            facilities = listOf("FASSA Lecture Theater", "Dean's Quadrangle", "Social Science Tutorial Huts"),
            connectedTo = listOf("john_pepper_clark_lib", "azikiwe_main_lib", "freedom_square", "abuja_gs_lt"),
            primaryActionLabel = "Attend FASSA Seminar & Tutorials",
            primaryActionCategory = "LECTURE",
            energyDelta = -14,
            sanityDelta = -2,
            prepDelta = +16,
            credDelta = +4,
            cloutDelta = +8,
            costNaira = 0L
        ),
        UnnMapLandmark(
            id = "john_pepper_clark_lib",
            name = "John Pepper Clark Library",
            shortLabel = "J.P. Clark Library",
            zone = CampusZone.ACADEMIC_CORE,
            xFraction = 0.22f,
            yFraction = 0.24f,
            description = "Serene literary and humanities research library named after iconic Nigerian poet and dramatist J.P. Clark. Ideal for deep exam revision and quiet study dates.",
            facilities = listOf("Rare Archives Reading Room", "Silent Study Carrels", "Literary Seminar Corner"),
            connectedTo = listOf("paul_robeson_lt", "faculty_social_sciences", "azikiwe_main_lib"),
            primaryActionLabel = "Deep Study at J.P. Clark Library",
            primaryActionCategory = "STUDY",
            energyDelta = -15,
            sanityDelta = +6,
            prepDelta = +20,
            credDelta = +3,
            cloutDelta = +2,
            costNaira = 0L
        ),
        UnnMapLandmark(
            id = "azikiwe_main_lib",
            name = "Nnamdi Azikiwe Library",
            shortLabel = "Azikiwe Library",
            zone = CampusZone.ACADEMIC_CORE,
            xFraction = 0.52f,
            yFraction = 0.26f,
            description = "Massive multi-story central library and CBT examination complex at the heart of the Nsukka campus.",
            facilities = listOf("500-PC CBT Exam Hall", "3rd Floor AC Reading Wing", "Past Question Reference Desk"),
            connectedTo = listOf("john_pepper_clark_lib", "faculty_social_sciences", "abuja_gs_lt", "freedom_square"),
            primaryActionLabel = "CBT Practice & Exam Cramming",
            primaryActionCategory = "STUDY",
            energyDelta = -18,
            sanityDelta = -4,
            prepDelta = +22,
            credDelta = +2,
            cloutDelta = 0,
            costNaira = 0L
        ),
        UnnMapLandmark(
            id = "sub_building",
            name = "Students' Union Building (SUB) & Ekpo Ref",
            shortLabel = "SUB & Ekpo Ref",
            zone = CampusZone.STUDENT_CENTER,
            xFraction = 0.50f,
            yFraction = 0.54f,
            description = "Commercial and social heartbeat of UNN. Home to hot Nsukka Okpa vendors, photocopy centers, union lounges, and Margaret Ekpo Refectory.",
            facilities = listOf("Hot Okpa & Coke Stalls", "SUG Welfare Lounge", "Business & Photocopy Center"),
            connectedTo = listOf("freedom_square", "mary_slessor_hall", "okija_hall", "carver_lt"),
            primaryActionLabel = "Eat Hot Okpa & Catch Campus Gist",
            primaryActionCategory = "SOCIAL",
            energyDelta = +24,
            sanityDelta = +16,
            prepDelta = 0,
            credDelta = +6,
            cloutDelta = +4,
            costNaira = 1200L
        ),
        UnnMapLandmark(
            id = "mary_slessor_hall",
            name = "Mary Slessor Hall",
            shortLabel = "Mary Slessor",
            zone = CampusZone.HOSTEL_RING,
            xFraction = 0.28f,
            yFraction = 0.72f,
            description = "Iconic female hostel famous for its lively courtyard market, strict 10 PM porters' lodge curfew, and weekend shawarma & hair styling hangouts.",
            facilities = listOf("Courtyard Mini-Mart", "Common Room TV Lounge", "Water Tank Tap Queue"),
            connectedTo = listOf("sub_building", "okija_hall", "paul_robeson_lt", "paa_auditorium"),
            primaryActionLabel = "Hostel Rest & Courtyard Socials",
            primaryActionCategory = "HOSTEL",
            energyDelta = +32,
            sanityDelta = +18,
            prepDelta = 0,
            credDelta = +6,
            cloutDelta = +4,
            costNaira = 500L
        ),
        UnnMapLandmark(
            id = "okija_hall",
            name = "Okija Hall & Aluta Hostels",
            shortLabel = "Okija Hall",
            zone = CampusZone.HOSTEL_RING,
            xFraction = 0.56f,
            yFraction = 0.78f,
            description = "High-energy student residence hub known for midnight football arguments, fierce hostel politics, bucket-bath solidarity, and fearless comrades.",
            facilities = listOf("Okija Congress Grounds", "Viewing Center", "Borehole Tap Arena"),
            connectedTo = listOf("mary_slessor_hall", "sub_building", "ziks_flats_odenigwe", "carver_lt"),
            primaryActionLabel = "Hostel Congress & Stamina Recovery",
            primaryActionCategory = "HOSTEL",
            energyDelta = +28,
            sanityDelta = +12,
            prepDelta = 0,
            credDelta = +8,
            cloutDelta = +12,
            costNaira = 500L
        ),
        UnnMapLandmark(
            id = "ziks_flats_odenigwe",
            name = "Zik's Flats & Odenigwe Gate",
            shortLabel = "Zik's Flats",
            zone = CampusZone.GATES_AND_LODGES,
            xFraction = 0.84f,
            yFraction = 0.80f,
            description = "Historic independence-era blocks bordering the bustling Odenigwe off-campus student village and mama-put joints.",
            facilities = listOf("Block A–M Flats", "Odenigwe Bush Bar", "Shuttle Park"),
            connectedTo = listOf("okija_hall", "carver_lt", "hilltop_second_gate"),
            primaryActionLabel = "Chill & Recharge at Zik's Flats",
            primaryActionCategory = "HOSTEL",
            energyDelta = +30,
            sanityDelta = +15,
            prepDelta = 0,
            credDelta = +5,
            cloutDelta = +6,
            costNaira = 600L
        ),
        UnnMapLandmark(
            id = "freedom_square",
            name = "Freedom Square",
            shortLabel = "Freedom Sq.",
            zone = CampusZone.STUDENT_CENTER,
            xFraction = 0.44f,
            yFraction = 0.44f,
            description = "Open-air political amphitheater opposite Ekpo Ref where SUG Manifesto Nights, student congresses, and cultural carnivals take place.",
            facilities = listOf("Aluta Podium", "Shuttle Drop-Off", "Open Rally Grounds"),
            connectedTo = listOf("faculty_social_sciences", "azikiwe_main_lib", "sub_building", "paa_auditorium"),
            primaryActionLabel = "Speak at Freedom Square Congress",
            primaryActionCategory = "POLITICS",
            energyDelta = -12,
            sanityDelta = +6,
            prepDelta = 0,
            credDelta = +10,
            cloutDelta = +16,
            costNaira = 1000L
        ),
        UnnMapLandmark(
            id = "paul_robeson_lt",
            name = "Paul Robeson & Arts Lecture Theater",
            shortLabel = "Paul Robeson LT",
            zone = CampusZone.LECTURE_THEATERS,
            xFraction = 0.14f,
            yFraction = 0.48f,
            description = "Historic Humanities, Mass Communication, and Theatre Arts complex featuring drama stages and crowded lecture halls.",
            facilities = listOf("Arts Main Auditorium", "Jacksonite Media Studio", "Rehearsal Stage"),
            connectedTo = listOf("john_pepper_clark_lib", "faculty_social_sciences", "mary_slessor_hall", "paa_auditorium"),
            primaryActionLabel = "Attend Arts & Humanities Lectures",
            primaryActionCategory = "LECTURE",
            energyDelta = -14,
            sanityDelta = +4,
            prepDelta = +15,
            credDelta = +8,
            cloutDelta = +4,
            costNaira = 0L
        ),
        UnnMapLandmark(
            id = "abuja_gs_lt",
            name = "Abuja Building & GS Lecture Theater",
            shortLabel = "Abuja / GS LT",
            zone = CampusZone.LECTURE_THEATERS,
            xFraction = 0.68f,
            yFraction = 0.32f,
            description = "Major lecture theater complex for General Studies (GS 101–208), Computer Science, and Physical Sciences classes.",
            facilities = listOf("GS Amphitheater", "Abuja Hall A & B", "Roar Hub Annex"),
            connectedTo = listOf("azikiwe_main_lib", "faculty_social_sciences", "carver_lt", "hilltop_second_gate"),
            primaryActionLabel = "Attend GS & Science Lectures",
            primaryActionCategory = "LECTURE",
            energyDelta = -16,
            sanityDelta = -4,
            prepDelta = +18,
            credDelta = +4,
            cloutDelta = +3,
            costNaira = 0L
        ),
        UnnMapLandmark(
            id = "carver_lt",
            name = "Carver Building & Engineering LT",
            shortLabel = "Carver LT",
            zone = CampusZone.LECTURE_THEATERS,
            xFraction = 0.74f,
            yFraction = 0.56f,
            description = "Legendary home of Engineering & Biological Sciences lectures by day and 'Night till Day Break' (NDB) Night Class by night.",
            facilities = listOf("Carver Night Class Halls", "Engineering Drawing Studio", "Microbiology Labs"),
            connectedTo = listOf("abuja_gs_lt", "sub_building", "okija_hall", "ziks_flats_odenigwe"),
            primaryActionLabel = "Attend Carver Lecture / Night Class",
            primaryActionCategory = "LECTURE",
            energyDelta = -18,
            sanityDelta = -6,
            prepDelta = +21,
            credDelta = +5,
            cloutDelta = +3,
            costNaira = 0L
        ),
        UnnMapLandmark(
            id = "paa_auditorium",
            name = "Princess Alexandra Auditorium (PAA)",
            shortLabel = "PAA & Franco",
            zone = CampusZone.STUDENT_CENTER,
            xFraction = 0.16f,
            yFraction = 0.68f,
            description = "Historic venue for Matriculation, Convocation, Mr. & Miss UNN pageants, fellowship crusades, and Afrobeats concerts.",
            facilities = listOf("PAA Main Bowl", "Franco Football Pitch", "Red Carpet Plaza"),
            connectedTo = listOf("paul_robeson_lt", "freedom_square", "mary_slessor_hall"),
            primaryActionLabel = "Vibe at PAA Show & Franco Pitch",
            primaryActionCategory = "SOCIAL",
            energyDelta = -10,
            sanityDelta = +20,
            prepDelta = 0,
            credDelta = +14,
            cloutDelta = +8,
            costNaira = 1500L
        ),
        UnnMapLandmark(
            id = "hilltop_second_gate",
            name = "Hilltop & Second Gate Hub",
            shortLabel = "Hilltop Gate",
            zone = CampusZone.GATES_AND_LODGES,
            xFraction = 0.88f,
            yFraction = 0.22f,
            description = "Upscale off-campus student district with self-con lodges, lounges,shawarma spots, and tech freelancer hubs.",
            facilities = listOf("Hilltop Penthouse Lounges", "Second Gate Shuttle Terminus", "Odim Tech Kiosks"),
            connectedTo = listOf("abuja_gs_lt", "ziks_flats_odenigwe"),
            primaryActionLabel = "Run Tech / Campus Hustle (+₦10,000)",
            primaryActionCategory = "HUSTLE",
            energyDelta = -18,
            sanityDelta = -2,
            prepDelta = -2,
            credDelta = +8,
            cloutDelta = +4,
            costNaira = -10000L
        )
    )

    fun findLandmark(id: String): UnnMapLandmark {
        return landmarks.firstOrNull { it.id == id } ?: landmarks[3] // Default SUB
    }

    fun calculateDistanceMeters(from: UnnMapLandmark, to: UnnMapLandmark): Int {
        if (from.id == to.id) return 0
        val dx = (from.xFraction - to.xFraction) * 1800f
        val dy = (from.yFraction - to.yFraction) * 1600f
        return kotlin.math.sqrt(dx * dx + dy * dy).toInt().coerceAtLeast(150)
    }

    fun calculateTrekEnergyCost(from: UnnMapLandmark, to: UnnMapLandmark): Int {
        val dist = calculateDistanceMeters(from, to)
        return (dist / 140).coerceIn(4, 14)
    }

    const val SHUTTLE_FARE_NAIRA = 200L
}
