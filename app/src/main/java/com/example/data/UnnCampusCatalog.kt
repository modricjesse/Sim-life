package com.example.data

data class UnnDepartmentOption(
    val campus: String,
    val faculty: String,
    val department: String,
    val cutoffDifficulty: String,
    val signatureCourse: String,
    val perkDescription: String
)

data class UnnHostelOption(
    val name: String,
    val campus: String,
    val rentNaira: Long,
    val vibe: String,
    val energyModifier: Int,
    val cloutModifier: Int,
    val sanityModifier: Int
)

data class UnnArchetypeOption(
    val title: String,
    val subtitle: String,
    val startingNaira: Long,
    val startingPrep: Long,
    val startingCred: Long,
    val startingClout: Long,
    val startingItem: String
)

data class UnnSpotAction(
    val id: String,
    val name: String,
    val landmark: String,
    val category: String, // ACADEMIC, HUSTLE, POLITICS, SOCIAL, WELLNESS
    val description: String,
    val costNaira: Long,
    val energyDelta: Int,
    val sanityDelta: Int,
    val prepDelta: Int,
    val credDelta: Int,
    val cloutDelta: Int,
    val logTemplate: String
)

data class UnnShopItem(
    val id: String,
    val name: String,
    val priceNaira: Long,
    val category: String,
    val benefit: String,
    val prepBonus: Int = 0,
    val energyBonus: Int = 0,
    val sanityBonus: Int = 0,
    val credBonus: Int = 0,
    val cloutBonus: Int = 0
)

data class UnnPoliticalOffice(
    val title: String,
    val minLevel: Long,
    val requiredClout: Int,
    val requiredCred: Int,
    val campaignCostNaira: Long,
    val weeklyStipendNaira: Long,
    val manifestoPitch: String
)

data class CampusRelationshipNpc(
    val id: String,
    val name: String,
    val role: String,
    val location: String,
    val perk: String,
    val giftCostNaira: Long
)

object UnnCampusCatalog {

    val departments = listOf(
        UnnDepartmentOption(
            campus = "Nsukka",
            faculty = "Engineering",
            department = "Electronic Engineering",
            cutoffDifficulty = "Extreme (Carver Building Grind)",
            signatureCourse = "ECE 321 & THERMO",
            perkDescription = "+10 Academic grit, high tech-hustle potential at Odim."
        ),
        UnnDepartmentOption(
            campus = "Nsukka",
            faculty = "Biological Sciences",
            department = "Microbiology",
            cutoffDifficulty = "High (Lab Coat Battalion)",
            signatureCourse = "MCB 201 & BIO 151",
            perkDescription = "Balanced lab life, strong departmental politics."
        ),
        UnnDepartmentOption(
            campus = "Nsukka",
            faculty = "Physical Sciences",
            department = "Computer Science",
            cutoffDifficulty = "High (Abuja Building Coders)",
            signatureCourse = "COS 201 & MTH 111",
            perkDescription = "Unlocks remote tech gigs & Roar Nigeria Hub pitch nights."
        ),
        UnnDepartmentOption(
            campus = "Nsukka",
            faculty = "Arts",
            department = "Mass Communication",
            cutoffDifficulty = "Competitive (Jacksonites)",
            signatureCourse = "MAC 101 & Jackson Weekly",
            perkDescription = "+15 Street Cred, natural edge in SUG media & social scenes."
        ),
        UnnDepartmentOption(
            campus = "Nsukka",
            faculty = "Social Sciences",
            department = "Political Science",
            cutoffDifficulty = "Strategic (Aluta Heavyweights)",
            signatureCourse = "POL 101 & Nigerian Govt",
            perkDescription = "+20 Political Clout, faster rise in SUG Senate & Executive."
        ),
        UnnDepartmentOption(
            campus = "Enugu",
            faculty = "Law (UNEC)",
            department = "Law (LL.B)",
            cutoffDifficulty = "Elite (White & Black Monochrome)",
            signatureCourse = "Constitutional Law & Moot Court",
            perkDescription = "+15 Clout & Street Cred on Enugu Campus, elite LAWSAN network."
        ),
        UnnDepartmentOption(
            campus = "Enugu",
            faculty = "College of Medicine (UNEC)",
            department = "Medicine & Surgery",
            cutoffDifficulty = "Legendary (MBBS Marathon)",
            signatureCourse = "Anatomy Cadaver Lab & Physiology",
            perkDescription = "Massive prestige, brutal exam pressure, high family allowance."
        )
    )

    val hostels = listOf(
        UnnHostelOption(
            name = "Zik's Flats",
            campus = "Nsukka",
            rentNaira = 15000L,
            vibe = "Historic independence-era flats. Cheap rent, raw brotherhood/sisterhood, occasional water tank trek.",
            energyModifier = -2,
            cloutModifier = +8,
            sanityModifier = +2
        ),
        UnnHostelOption(
            name = "Bello Hall",
            campus = "Nsukka",
            rentNaira = 18000L,
            vibe = "The political volcano of UNN. Midnight Aluta chants, football viewing banter, instant SUG connections.",
            energyModifier = -3,
            cloutModifier = +12,
            sanityModifier = 0
        ),
        UnnHostelOption(
            name = "Okpara Hall",
            campus = "Nsukka",
            rentNaira = 18000L,
            vibe = "Bustling central hall near SUB & Ekpo Ref. Prime spot for campus socialites, hair vendors, and night gist.",
            energyModifier = +2,
            cloutModifier = +7,
            sanityModifier = +4
        ),
        UnnHostelOption(
            name = "Mary Slessor Hall",
            campus = "Nsukka",
            rentNaira = 18000L,
            vibe = "Strict 10 PM porters' curfew, vibrant courtyard market, and legendary room cooking shenanigans.",
            energyModifier = +3,
            cloutModifier = +6,
            sanityModifier = +5
        ),
        UnnHostelOption(
            name = "Okija Hall",
            campus = "Nsukka",
            rentNaira = 16500L,
            vibe = "High-voltage comrade hostel famous for fierce solidarity, midnight football debates, and fearless Aluta energy.",
            energyModifier = 0,
            cloutModifier = +14,
            sanityModifier = +2
        ),
        UnnHostelOption(
            name = "Hilltop Self-Con Lodge",
            campus = "Nsukka",
            rentNaira = 45000L,
            vibe = "Off-campus luxury behind Second Gate. Private bathroom, generator noise, and 'Hilltop Big Boy/Girl' status.",
            energyModifier = +8,
            cloutModifier = +4,
            sanityModifier = +10
        ),
        UnnHostelOption(
            name = "Odenigwe Off-Campus",
            campus = "Nsukka",
            rentNaira = 22000L,
            vibe = "Budget off-campus hustle village. Cheap mama-put food, trek to Carver Building, zero hostel curfew.",
            energyModifier = 0,
            cloutModifier = +5,
            sanityModifier = +3
        ),
        UnnHostelOption(
            name = "Manuwa Hall (UNEC)",
            campus = "Enugu",
            rentNaira = 20000L,
            vibe = "Classic Enugu Campus residence right beside Law and Management lecture theatres.",
            energyModifier = +3,
            cloutModifier = +8,
            sanityModifier = +4
        )
    )

    val archetypes = listOf(
        UnnArchetypeOption(
            title = "Efiwe (Night Class Warrior)",
            subtitle = "You came to UNN with 4 highlighters, past questions from 2012, and a dream of 5.00 CGPA.",
            startingNaira = 25000L,
            startingPrep = 60L,
            startingCred = 15L,
            startingClout = 10L,
            startingItem = "Rechargeable Reading Lamp"
        ),
        UnnArchetypeOption(
            title = "Aluta Politician",
            subtitle = "Your voice echoes from Ekpo Ref to Freedom Square. 'Greatest Lions and Lionesses!' is your birthright.",
            startingNaira = 30000L,
            startingPrep = 35L,
            startingCred = 40L,
            startingClout = 45L,
            startingItem = "SUG Constitution Handbook"
        ),
        UnnArchetypeOption(
            title = "Odim / Hilltop Hustler",
            subtitle = "From crypto P2P to selling shawarma, thrift hoodies, and past-question PDFs, you never go broke.",
            startingNaira = 55000L,
            startingPrep = 35L,
            startingCred = 45L,
            startingClout = 20L,
            startingItem = "20,000mAh Fast Powerbank"
        ),
        UnnArchetypeOption(
            title = "Campus Socialite",
            subtitle = "Mr./Miss Faculty contender, Jeans Carnival headliner, known at every Bush Bar and SUB hangout.",
            startingNaira = 40000L,
            startingPrep = 30L,
            startingCred = 60L,
            startingClout = 30L,
            startingItem = "VIP Faculty Dinner Pass"
        )
    )

    val spotActions = listOf(
        UnnSpotAction(
            id = "azikiwe_library",
            name = "Nnamdi Azikiwe Library",
            landmark = "Main Library (3rd Floor AC Hall)",
            category = "ACADEMIC",
            description = "Secure a table at 7:00 AM and grind lecture notes & GS past questions in cool AC silence.",
            costNaira = 0L,
            energyDelta = -18,
            sanityDelta = -4,
            prepDelta = +18,
            credDelta = +2,
            cloutDelta = 0,
            logTemplate = "Locked in at Nnamdi Azikiwe Library for 5 hours. Academic Prep soared!"
        ),
        UnnSpotAction(
            id = "night_class_carver",
            name = "Night Class (NDB) at Carver",
            landmark = "Carver Building / Paul Robeson",
            category = "ACADEMIC",
            description = "Pull an all-nighter ('Night till Day Break') with fellow Efiwes fighting mosquitoes and sleep.",
            costNaira = 500L,
            energyDelta = -26,
            sanityDelta = -10,
            prepDelta = +25,
            credDelta = +4,
            cloutDelta = +2,
            logTemplate = "Survived Night Class at Carver Building on Nescafe and pure determination."
        ),
        UnnSpotAction(
            id = "sub_okpa_run",
            name = "Hot Okpa & Coke at SUB",
            landmark = "Student Union Building / Ekpo Ref",
            category = "WELLNESS",
            description = "Devour steaming Nsukka Okpa wrapped in banana leaf with chilled bottle Coke and catch campus gist.",
            costNaira = 1500L,
            energyDelta = +24,
            sanityDelta = +16,
            prepDelta = 0,
            credDelta = +5,
            cloutDelta = +3,
            logTemplate = "Recharged with legendary hot Nsukka Okpa and bottle Coke at SUB."
        ),
        UnnSpotAction(
            id = "freedom_square_aluta",
            name = "SUG Rally at Freedom Square",
            landmark = "Freedom Square",
            category = "POLITICS",
            description = "Grab the mic, chant 'Greatest Lions & Lionesses!', and network with Hall Governors and Senators.",
            costNaira = 2000L,
            energyDelta = -15,
            sanityDelta = +5,
            prepDelta = -3,
            credDelta = +10,
            cloutDelta = +16,
            logTemplate = "Delivered a fiery speech at Freedom Square! 'Aluta Continua, Victoria Ascerta!'"
        ),
        UnnSpotAction(
            id = "roar_hub_gig",
            name = "Roar Hub & Odim Side Hustle",
            landmark = "Roar Nigeria Hub / Odim Gate",
            category = "HUSTLE",
            description = "Run freelance tech tasks, print course clearances, or flip gadgets to fellow students for quick Naira.",
            costNaira = -12000L, // Negative cost means player earns Naira!
            energyDelta = -20,
            sanityDelta = -5,
            prepDelta = -4,
            credDelta = +6,
            cloutDelta = +2,
            logTemplate = "Closed a profitable campus hustle at Roar Hub / Odim Gate (+₦12,000)!"
        ),
        UnnSpotAction(
            id = "franco_pitch_chill",
            name = "Inter-Faculty Match at Franco",
            landmark = "Franco Pitch / Vet Mountain",
            category = "SOCIAL",
            description = "Cheer at the heated Engineering vs Biological Sciences match and link up with campus heavyweights.",
            costNaira = 1000L,
            energyDelta = +10,
            sanityDelta = +20,
            prepDelta = 0,
            credDelta = +12,
            cloutDelta = +6,
            logTemplate = "Vibed at Franco Pitch during the Inter-Faculty Cup and boosted Street Cred."
        ),
        UnnSpotAction(
            id = "hostel_recovery",
            name = "Deep Sleep & Bucket Bath",
            landmark = "Hostel / Lodge",
            category = "WELLNESS",
            description = "Fetch two buckets of water before the tap stops, cook Indomie with egg, and sleep for 10 hours.",
            costNaira = 800L,
            energyDelta = +38,
            sanityDelta = +22,
            prepDelta = 0,
            credDelta = 0,
            cloutDelta = 0,
            logTemplate = "Fetched water early, cooked spiced Indomie, and slept like a baby in the hostel."
        )
    )

    val shopItems = listOf(
        UnnShopItem(
            id = "lamp",
            name = "Rechargeable Reading Lamp",
            priceNaira = 6500L,
            category = "Academic",
            benefit = "Immunity to NEPA blackouts during Night Class (+8 Prep, +5 Sanity).",
            prepBonus = 8,
            sanityBonus = 5
        ),
        UnnShopItem(
            id = "gs_compendium",
            name = "GS 101–208 Past Questions Vault",
            priceNaira = 4500L,
            category = "Academic",
            benefit = "Cracks the CBT question bank at Nnamdi Azikiwe Library (+14 Prep).",
            prepBonus = 14
        ),
        UnnShopItem(
            id = "powerbank",
            name = "30,000mAh Oraimo Powerbank",
            priceNaira = 18000L,
            category = "Survival",
            benefit = "Phone never dies during Bello/Okpara transformer outage (+10 Sanity, +8 Cred).",
            sanityBonus = 10,
            credBonus = 8
        ),
        UnnShopItem(
            id = "dept_hoodie",
            name = "Custom 'Lion Den' Varsity Jacket",
            priceNaira = 15000L,
            category = "Social",
            benefit = "Peak drip for Harmattan mornings & Jeans Carnival (+18 Street Cred, +6 Clout).",
            credBonus = 18,
            cloutBonus = 6
        ),
        UnnShopItem(
            id = "megaphone",
            name = "Aluta Megaphone & Senator Cap",
            priceNaira = 22000L,
            category = "Politics",
            benefit = "Dominate hostel congresses and Freedom Square rallies (+22 Political Clout).",
            cloutBonus = 22,
            credBonus = 8
        ),
        UnnShopItem(
            id = "mini_gas",
            name = "6kg Hostel Gas Cylinder + Pot",
            priceNaira = 24000L,
            category = "Survival",
            benefit = "Cook Jollof & beans in the hostel anytime (+15 Energy, +12 Sanity).",
            energyBonus = 15,
            sanityBonus = 12
        )
    )

    val politicalOffices = listOf(
        UnnPoliticalOffice(
            title = "Course Rep",
            minLevel = 100L,
            requiredClout = 20,
            requiredCred = 20,
            campaignCostNaira = 3000L,
            weeklyStipendNaira = 2500L,
            manifestoPitch = "Coordinate lecture venues, negotiate quiz postponements with lecturers, and control the microphone."
        ),
        UnnPoliticalOffice(
            title = "Hall Governor",
            minLevel = 200L,
            requiredClout = 42,
            requiredCred = 38,
            campaignCostNaira = 12000L,
            weeklyStipendNaira = 6000L,
            manifestoPitch = "Champion water pumping schedules, hostel cultural night, and command 1,000 hostel votes."
        ),
        UnnPoliticalOffice(
            title = "SUG Senator",
            minLevel = 200L,
            requiredClout = 58,
            requiredCred = 48,
            campaignCostNaira = 22000L,
            weeklyStipendNaira = 9500L,
            manifestoPitch = "Represent your faculty in the Student Senate and scrutinized union budgets."
        ),
        UnnPoliticalOffice(
            title = "SUG Director of Socials",
            minLevel = 300L,
            requiredClout = 72,
            requiredCred = 75,
            campaignCostNaira = 38000L,
            weeklyStipendNaira = 16000L,
            manifestoPitch = "Host Mr. & Miss UNN, Freshers' Night at Ekpo Ref, and command celebrity campus artistes."
        ),
        UnnPoliticalOffice(
            title = "SUG President (001 Lion)",
            minLevel = 400L,
            requiredClout = 88,
            requiredCred = 82,
            campaignCostNaira = 65000L,
            weeklyStipendNaira = 28000L,
            manifestoPitch = "Lead 40,000 Lions & Lionesses, negotiate with the VC's Senate Building, and leave an immortal legacy."
        )
    )

    val npcs = listOf(
        CampusRelationshipNpc(
            id = "Dr_Obinna",
            name = "Dr. Obinna Agu",
            role = "Strict Departmental Exam Officer",
            location = "Faculty Dean's Corridor",
            perk = "High rapport prevents missing script drama and grants project supervision priority.",
            giftCostNaira = 4000L
        ),
        CampusRelationshipNpc(
            id = "Comrade_Kalu",
            name = "Comrade 'Aluta' Kalu",
            role = "Bello Hall Political Kingmaker",
            location = "Freedom Square / SUB",
            perk = "Delivers bloc hostel votes during SUG elections and tips you off on campus strikes.",
            giftCostNaira = 3500L
        ),
        CampusRelationshipNpc(
            id = "Amaka_Hilltop",
            name = "Amaka 'Drip Queen' Eze",
            role = "Faculty Pageant Winner & Influencer",
            location = "Hilltop / Bush Bar",
            perk = "Boosts your Street Cred at every Faculty Week & Jeans Carnival.",
            giftCostNaira = 5000L
        ),
        CampusRelationshipNpc(
            id = "Chuka_Efiwe",
            name = "Chuka '5.0' Nnamdi",
            role = "First-Class Night Class Legend",
            location = "Nnamdi Azikiwe Library",
            perk = "Shares solved tutorial sheets and midnight past-question breakdowns before exams.",
            giftCostNaira = 2500L
        )
    )

    fun computeSocialTier(cred: Long, clout: Long, cgpa: Double, naira: Long, sugOffice: String): String {
        return when {
            sugOffice == "SUG President (001 Lion)" -> "001 Supreme Lion of the Den"
            cgpa >= 4.70 && cred >= 70 -> "First-Class Royalty"
            clout >= 80 -> "Aluta Heavyweight & Kingmaker"
            cred >= 80 && naira >= 80000L -> "Hilltop & Campus Mogul"
            cgpa >= 4.50 -> "Legendary Efiwe Scholar"
            cred >= 55 || clout >= 55 -> "Senior Lion / Campus Face"
            cred >= 35 -> "Odim & Ekpo Ref Regular"
            else -> "Jambite / JJC"
        }
    }
}
