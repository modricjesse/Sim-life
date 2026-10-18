package com.example.data

import kotlin.random.Random

enum class HostelNightScenarioType(val label: String) {
    WATER_SHORTAGE("Water Shortage & Bucket Queue"),
    ROOM_PARTY("Impromptu Room Party"),
    ROOMMATE_ARGUMENT("Roommate & Corner Argument"),
    PORTER_INSPECTION("Porters' Inspection & Curfew"),
    NEPA_BLACKOUT("Midnight NEPA Blackout")
}

data class HostelNightEvent(
    val id: String,
    val title: String,
    val scenarioType: HostelNightScenarioType,
    val timestampLabel: String,
    val description: String,
    val helpfulGearKeyword: String? = null,
    val gearBonusDescription: String? = null,
    val preferredHostelKeywords: List<String> = emptyList(),
    val excludedHostelKeywords: List<String> = emptyList(),
    val choices: List<ShenaniganChoice>
)

data class GearSynergyBonus(
    val isActive: Boolean,
    val matchedItemName: String?,
    val extraEnergy: Int = 0,
    val extraSanity: Int = 0,
    val extraPrep: Int = 0,
    val extraCred: Int = 0
)

object HostelNightShenanigansGenerator {

    val nightEvents: List<HostelNightEvent> = listOf(
        // 1. WATER SHORTAGES & BUCKET QUEUES
        HostelNightEvent(
            id = "night_borehole_stampede",
            title = "01:15 AM Borehole Tap Stampede",
            scenarioType = HostelNightScenarioType.WATER_SHORTAGE,
            timestampLabel = "01:15 AM",
            description = "The hostel taps have been dry for 48 hours. Suddenly, a pipe gurgles and someone screams from the ground floor: 'WATER DEY RUN FOR TANK B!' Forty buckets are already flying down the stairs.",
            helpfulGearKeyword = "Lamp",
            gearBonusDescription = "Rechargeable Lamp lights the dark staircase (+4 Energy, +4 Sanity)",
            preferredHostelKeywords = listOf("Zik", "Okija", "Bello", "Slessor", "Okpara"),
            excludedHostelKeywords = listOf("Hilltop"),
            choices = listOf(
                ShenaniganChoice(
                    label = "Sprint down with 3 buckets & hold your ground in the queue",
                    outcomeNarrative = "You guarded your tap turn against three final-year heavyweights and hauled 3 brimming buckets upstairs before pressure dropped!",
                    energyDelta = -14,
                    sanityDelta = +10,
                    credDelta = +8,
                    cloutDelta = +6,
                    badgeAwarded = "Midnight Bucket General"
                ),
                ShenaniganChoice(
                    label = "Form a bucket relay alliance with your roommates",
                    outcomeNarrative = "Teamwork made the dream work! One person filled at the tap while others ferried buckets up the stairs in record time.",
                    energyDelta = -8,
                    sanityDelta = +12,
                    credDelta = +6,
                    cloutDelta = +4
                ),
                ShenaniganChoice(
                    label = "Pay hostel 'Water Boys' ₦1,500 to fill your drum while you sleep",
                    outcomeNarrative = "Soft life in the trenches! You stayed under your mosquito net while two full drums were delivered to your door.",
                    nairaDelta = -1500L,
                    energyDelta = +10,
                    sanityDelta = +14,
                    credDelta = +4
                )
            )
        ),
        HostelNightEvent(
            id = "night_missing_bucket_heist",
            title = "The Mystery of the Hijacked Bath Bucket",
            scenarioType = HostelNightScenarioType.WATER_SHORTAGE,
            timestampLabel = "11:50 PM",
            description = "You left your prized 20-liter bucket of clean water outside your door for 5 minutes to grab your sponge case. When you step back out, the water is gone and an empty paint rubber is sitting in its place!",
            preferredHostelKeywords = listOf("Okija", "Bello", "Zik", "Slessor", "Okpara", "Manuwa"),
            choices = listOf(
                ShenaniganChoice(
                    label = "Conduct a forensic wet-floor trace down the corridor",
                    outcomeNarrative = "You followed the water droplets to Room 214, knocked with full Aluta authority, and reclaimed your water with an apology!",
                    energyDelta = -6,
                    sanityDelta = +12,
                    credDelta = +10,
                    cloutDelta = +8
                ),
                ShenaniganChoice(
                    label = "Borrow half a bucket from your nicest floor neighbor",
                    outcomeNarrative = "Your neighbor came through with half a bucket of warm water. You managed a tactical bath and vowed to padlock your drum tomorrow.",
                    energyDelta = -4,
                    sanityDelta = +6,
                    credDelta = +4
                ),
                ShenaniganChoice(
                    label = "Do a '3-Sachet Pure Water' tactical wash & go to bed",
                    outcomeNarrative = "Extreme campus survival! Three sachets of ₦100 pure water got the job done, though your sanity took a mild bruise.",
                    nairaDelta = -300L,
                    energyDelta = +4,
                    sanityDelta = -8,
                    prepDelta = +4
                )
            )
        ),

        // 2. IMPROMPTU ROOM PARTIES & LOUD MUSIC
        HostelNightEvent(
            id = "night_corridor_birthday_rave",
            title = "Impromptu Room 108 Birthday Bash & Suya Night",
            scenarioType = HostelNightScenarioType.ROOM_PARTY,
            timestampLabel = "12:20 AM",
            description = "A giant JBL speaker starts shaking the entire wing! Your floor neighbor is celebrating a birthday with peppered gizzard, suya platters, chilled drinks, and a freestyle rap cypher.",
            preferredHostelKeywords = listOf("Okpara", "Slessor", "Hilltop", "Odenigwe", "Okija"),
            choices = listOf(
                ShenaniganChoice(
                    label = "Jump into the dance circle & drop a legendary freestyle",
                    outcomeNarrative = "You owned the corridor cypher! Everyone recorded your moves for WhatsApp status TVs and handed you extra suya.",
                    energyDelta = -12,
                    sanityDelta = +18,
                    credDelta = +16,
                    cloutDelta = +8,
                    badgeAwarded = "Hostel Groove Master"
                ),
                ShenaniganChoice(
                    label = "Chip in ₦2,000 for drinks & network with senior Lions",
                    outcomeNarrative = "High-value networking! You clinked cups with two faculty senators and a First-Class senior who promised you past questions.",
                    nairaDelta = -2000L,
                    energyDelta = -8,
                    sanityDelta = +14,
                    prepDelta = +6,
                    credDelta = +12,
                    cloutDelta = +10
                ),
                ShenaniganChoice(
                    label = "Put on noise-canceling earbuds & grind lecture notes",
                    outcomeNarrative = "Iron discipline! While Afrobeats shook the louvres, you finished two tough tutorial sheets.",
                    energyDelta = -6,
                    sanityDelta = -2,
                    prepDelta = +14,
                    credDelta = -2
                )
            )
        ),
        HostelNightEvent(
            id = "night_hilltop_speaker_wars",
            title = "Midnight Subwoofer Showdown Next Door",
            scenarioType = HostelNightScenarioType.ROOM_PARTY,
            timestampLabel = "01:40 AM",
            description = "The room next door has been blasting Amapiano basslines on repeat for three hours while you have an 8:00 AM lecture at Carver Building tomorrow.",
            helpfulGearKeyword = "Varsity Jacket",
            gearBonusDescription = "Varsity Jacket drip commands instant VIP respect (+6 Street Cred)",
            preferredHostelKeywords = listOf("Hilltop", "Odenigwe", "Zik", "Bello"),
            choices = listOf(
                ShenaniganChoice(
                    label = "Knock, join the vibe for 30 mins & negotiate a 2 AM volume drop",
                    outcomeNarrative = "Smooth diplomacy! You grabbed a free drink, hailed the DJ, and convinced them to switch to low-volume chill tunes by 2:00 AM.",
                    energyDelta = -4,
                    sanityDelta = +12,
                    credDelta = +10,
                    cloutDelta = +8
                ),
                ShenaniganChoice(
                    label = "Mount a full Aluta corridor protest with sleepy neighbors",
                    outcomeNarrative = "Five rooms backed you up! The party hosts apologized and turned down the woofer immediately.",
                    energyDelta = -6,
                    sanityDelta = +8,
                    cloutDelta = +12
                ),
                ShenaniganChoice(
                    label = "Pack your backpack & relocate to Night Class till dawn",
                    outcomeNarrative = "If you can't sleep, out-study the competition! You turned a noisy night into +16 Academic Prep at night class.",
                    energyDelta = -14,
                    sanityDelta = -4,
                    prepDelta = +16
                )
            )
        ),

        // 3. HEATED ROOMMATE ARGUMENTS
        HostelNightEvent(
            id = "night_roommate_pot_war",
            title = "The Unwashed Beans Pot & Missing Garri Cold War",
            scenarioType = HostelNightScenarioType.ROOMMATE_ARGUMENT,
            timestampLabel = "10:55 PM",
            description = "Tension explodes in your room! Someone used your last cup of yellow Ijebu Garri AND left a crusted pot of burnt beans soaking in the corner for three days.",
            helpfulGearKeyword = "Gas Cylinder",
            gearBonusDescription = "Own 6kg Gas Cylinder + Pot gives you kitchen authority (+6 Sanity, +5 Energy)",
            preferredHostelKeywords = listOf("Zik", "Bello", "Okija", "Slessor", "Okpara", "Manuwa"),
            choices = listOf(
                ShenaniganChoice(
                    label = "Call an emergency 'Room Parliament' & draft a strict roster",
                    outcomeNarrative = "Order restored! The culprit confessed, replaced your Garri with two tins of Peak Milk, and scrubbed the pot sparkling clean.",
                    energyDelta = -4,
                    sanityDelta = +14,
                    credDelta = +6,
                    cloutDelta = +10,
                    badgeAwarded = "Room Parliament Speaker"
                ),
                ShenaniganChoice(
                    label = "Turn it into a midnight Indomie & egg peace-treaty cookout",
                    outcomeNarrative = "Nothing heals hostel beef faster than spiced Indomie and fried egg at midnight! Everyone laughed it off and bonded.",
                    nairaDelta = -1200L,
                    energyDelta = +12,
                    sanityDelta = +16,
                    credDelta = +8
                ),
                ShenaniganChoice(
                    label = "Lock your food locker with a heavy padlock & give silent treatment",
                    outcomeNarrative = "Your provisions are now Fort Knox secure, though the room atmosphere stayed frosty till morning.",
                    nairaDelta = -800L,
                    energyDelta = -2,
                    sanityDelta = -6,
                    prepDelta = +6
                )
            )
        ),
        HostelNightEvent(
            id = "night_roommate_squatter_debate",
            title = "The 'Cousin from Enugu' Permanent Squatter Crisis",
            scenarioType = HostelNightScenarioType.ROOMMATE_ARGUMENT,
            timestampLabel = "11:30 PM",
            description = "Your roommate's 'visiting friend' has been sleeping on the centre rug for two weeks, using your pressing iron, and debating football loudly at 11:30 PM!",
            preferredHostelKeywords = listOf("Bello", "Okija", "Zik", "Slessor", "Okpara", "Odenigwe"),
            choices = listOf(
                ShenaniganChoice(
                    label = "Demand a ₦2,500 'Utility & Welfare Levy' from the squatter",
                    outcomeNarrative = "Sharp business move! The squatter respected your hustle, paid ₦2,500 toward room bulbs and water, and volunteered to fetch water all week!",
                    nairaDelta = +2500L,
                    energyDelta = +6,
                    sanityDelta = +10,
                    credDelta = +8,
                    cloutDelta = +8
                ),
                ShenaniganChoice(
                    label = "Set strict 10:30 PM lights-out rules for exam preparation",
                    outcomeNarrative = "You laid down the law firmly. The room went dead silent by 10:30 PM and everyone got quality sleep and study time.",
                    energyDelta = +8,
                    sanityDelta = +8,
                    prepDelta = +10
                ),
                ShenaniganChoice(
                    label = "Join the midnight Champions League argument till 1:30 AM",
                    outcomeNarrative = "You defended your club with legendary stats! Zero studying happened, but your hostel street cred went up.",
                    energyDelta = -10,
                    sanityDelta = +10,
                    prepDelta = -4,
                    credDelta = +10
                )
            )
        ),

        // 4. HALL PORTER INSPECTIONS & CURFEW LOCKOUTS
        HostelNightEvent(
            id = "night_porter_hotplate_raid",
            title = "Midnight Porters' Whistle & Hotplate Raid",
            scenarioType = HostelNightScenarioType.PORTER_INSPECTION,
            timestampLabel = "10:40 PM",
            description = "Whistles echo down the hostel quadrangle! The Chief Porter and security team are conducting a surprise room-to-room contraband hotplate and squatter check!",
            helpfulGearKeyword = "Megaphone",
            gearBonusDescription = "Aluta Megaphone & Senator Cap intimidates the inspection team (+6 Clout)",
            preferredHostelKeywords = listOf("Slessor", "Okpara", "Bello", "Okija", "Manuwa"),
            excludedHostelKeywords = listOf("Hilltop", "Odenigwe"),
            choices = listOf(
                ShenaniganChoice(
                    label = "Execute a 15-second hotplate hideout inside the ceiling/wardrobe",
                    outcomeNarrative = "Clean sweep! By the time the porters knocked, you were innocently reading a GS textbook on your bunk.",
                    energyDelta = -4,
                    sanityDelta = +10,
                    prepDelta = +6,
                    credDelta = +8
                ),
                ShenaniganChoice(
                    label = "Greet the Chief Porter with chilled Malta & Hall Government protocol",
                    outcomeNarrative = "Elite political packaging! The Chief Porter hailed you as a 'disciplined Lion' and skipped inspecting your entire block.",
                    nairaDelta = -1000L,
                    energyDelta = +4,
                    sanityDelta = +12,
                    credDelta = +10,
                    cloutDelta = +12,
                    badgeAwarded = "Porters' Lodge Diplomat"
                ),
                ShenaniganChoice(
                    label = "Alert the whole floor via WhatsApp group & coordinate dark-room stealth",
                    outcomeNarrative = "You saved 12 rooms from having their cooking pots seized! The entire floor owes you a favor.",
                    energyDelta = -6,
                    sanityDelta = +8,
                    credDelta = +14,
                    cloutDelta = +10
                )
            )
        ),
        HostelNightEvent(
            id = "night_curfew_gate_lockout",
            title = "10:15 PM Hostel Gate Lockout After Night Gist",
            scenarioType = HostelNightScenarioType.PORTER_INSPECTION,
            timestampLabel = "10:15 PM",
            description = "You lost track of time eating shawarma at SUB and reviewing notes. You arrive at your hostel gate at 10:15 PM to find the giant padlock snapped shut and the stern night porter frowning behind the bars!",
            preferredHostelKeywords = listOf("Slessor", "Okpara", "Manuwa", "Bello"),
            excludedHostelKeywords = listOf("Hilltop", "Odenigwe"),
            choices = listOf(
                ShenaniganChoice(
                    label = "Flash your lab coat / library notes & plead 'Academic Emergency'",
                    outcomeNarrative = "Your convincing Efiwe speech melted the porter's heart! The small pedestrian wicket gate creaked open for you.",
                    energyDelta = -4,
                    sanityDelta = +10,
                    prepDelta = +4,
                    credDelta = +6
                ),
                ShenaniganChoice(
                    label = "Use the secret low-fence shortcut behind the quadrangle",
                    outcomeNarrative = "Parkour in the Den! You vaulted over the back hedge like an action star and landed safely by your block.",
                    energyDelta = -10,
                    sanityDelta = +6,
                    credDelta = +12,
                    badgeAwarded = "Curfew Fence Ninja"
                ),
                ShenaniganChoice(
                    label = "Turn back to Carver Building for an all-night study marathon",
                    outcomeNarrative = "Locked out of bed, locked into First Class! You studied till 6:00 AM and mastered an entire course module.",
                    energyDelta = -18,
                    sanityDelta = -4,
                    prepDelta = +20
                )
            )
        ),

        // 5. MIDNIGHT NEPA BLACKOUTS & PHONE CHARGING SCRAMBLES
        HostelNightEvent(
            id = "night_transformer_spark_blackout",
            title = "Midnight Transformer Boom & 4% Battery Panic",
            scenarioType = HostelNightScenarioType.NEPA_BLACKOUT,
            timestampLabel = "12:05 AM",
            description = "A loud 'BOOM!' echoes from the Odim/Hostel transformer and every ceiling fan groans to a halt. Pitch darkness blankets the hostel, mosquitoes are revving up, and your phone battery is at 4%!",
            helpfulGearKeyword = "Powerbank",
            gearBonusDescription = "30,000mAh Oraimo Powerbank keeps your devices at 100% (+8 Sanity, +6 Energy)",
            preferredHostelKeywords = listOf("Zik", "Odenigwe", "Okija", "Bello", "Slessor", "Okpara", "Hilltop"),
            choices = listOf(
                ShenaniganChoice(
                    label = "Break out your Rechargeable Lamp / Powerbank & study on the balcony",
                    outcomeNarrative = "Cool night breeze on the balcony + steady LED light = peak productivity while others sweated indoors!",
                    energyDelta = -6,
                    sanityDelta = +8,
                    prepDelta = +14,
                    credDelta = +6
                ),
                ShenaniganChoice(
                    label = "Plug into the lodge's 'I Better Pass My Neighbour' gen extension cord",
                    outcomeNarrative = "You contributed ₦800 for fuel and ran a 6-way extension box that charged your phone, laptop, and rechargeable fan!",
                    nairaDelta = -800L,
                    energyDelta = +10,
                    sanityDelta = +12,
                    prepDelta = +8,
                    credDelta = +6
                ),
                ShenaniganChoice(
                    label = "Join the corridor acoustic sing-along & campus ghost stories",
                    outcomeNarrative = "Half the floor sat on the balcony railing singing throwback hits until 'UP NEPA!' roared at 1:45 AM!",
                    energyDelta = +4,
                    sanityDelta = +16,
                    credDelta = +10,
                    cloutDelta = +6
                )
            )
        ),
        HostelNightEvent(
            id = "night_up_nepa_socket_rush",
            title = "02:30 AM 'UP NEPA!' & The 8-Socket Extension Scramble",
            scenarioType = HostelNightScenarioType.NEPA_BLACKOUT,
            timestampLabel = "02:30 AM",
            description = "At 2:30 AM, the security bulb flashes bright yellow and a thunderous 'UPP NEPAAA!' shakes the hostel! Every roommate dives out of bed simultaneously for the only two working wall sockets.",
            helpfulGearKeyword = "Powerbank",
            gearBonusDescription = "Fast Powerbank charges simultaneously with your phone (+6 Sanity)",
            preferredHostelKeywords = listOf("Okija", "Bello", "Zik", "Slessor", "Okpara", "Odenigwe", "Manuwa"),
            choices = listOf(
                ShenaniganChoice(
                    label = "Deploy your multi-plug hub & become the room's 'Socket Landlord'",
                    outcomeNarrative = "Everyone plugged into your adapter! Your roommates hailed you and promised to wake you up for your 8 AM class.",
                    energyDelta = +4,
                    sanityDelta = +12,
                    credDelta = +10,
                    cloutDelta = +8
                ),
                ShenaniganChoice(
                    label = "Iron 4 shirts immediately & boil water before power trips again",
                    outcomeNarrative = "Maximum efficiency! You pressed your entire week's outfits and made hot tea in 20 minutes flat.",
                    energyDelta = -4,
                    sanityDelta = +10,
                    prepDelta = +6,
                    credDelta = +12
                ),
                ShenaniganChoice(
                    label = "Ignore the socket scramble & enjoy the spinning ceiling fan sleep",
                    outcomeNarrative = "With the ceiling fan finally whirring at full speed, you drifted into the deepest, most refreshing sleep of the semester.",
                    energyDelta = +20,
                    sanityDelta = +14
                )
            )
        )
    )

    fun evaluateGearBonus(event: HostelNightEvent, inventory: List<String>): GearSynergyBonus {
        val keyword = event.helpfulGearKeyword ?: return GearSynergyBonus(isActive = false, matchedItemName = null)
        val matched = inventory.firstOrNull { it.contains(keyword, ignoreCase = true) }
            ?: return GearSynergyBonus(isActive = false, matchedItemName = null)

        return when (event.scenarioType) {
            HostelNightScenarioType.WATER_SHORTAGE -> GearSynergyBonus(
                isActive = true,
                matchedItemName = matched,
                extraEnergy = 4,
                extraSanity = 4
            )
            HostelNightScenarioType.NEPA_BLACKOUT -> GearSynergyBonus(
                isActive = true,
                matchedItemName = matched,
                extraEnergy = 6,
                extraSanity = 8,
                extraPrep = 4
            )
            HostelNightScenarioType.ROOMMATE_ARGUMENT -> GearSynergyBonus(
                isActive = true,
                matchedItemName = matched,
                extraEnergy = 5,
                extraSanity = 6
            )
            HostelNightScenarioType.PORTER_INSPECTION -> GearSynergyBonus(
                isActive = true,
                matchedItemName = matched,
                extraSanity = 5,
                extraCred = 6
            )
            HostelNightScenarioType.ROOM_PARTY -> GearSynergyBonus(
                isActive = true,
                matchedItemName = matched,
                extraSanity = 4,
                extraCred = 6
            )
        }
    }

    fun eligibleEventsForHostel(hostelName: String): List<HostelNightEvent> {
        return nightEvents.filter { ev ->
            ev.excludedHostelKeywords.none { ex -> hostelName.contains(ex, ignoreCase = true) }
        }.ifEmpty { nightEvents }
    }

    fun hostelRiskProfileSummary(hostelName: String): String {
        return when {
            hostelName.contains("Slessor", ignoreCase = true) || hostelName.contains("Bello", ignoreCase = true) ->
                "High odds of Hall Porter hotplate raids, 10:30 PM gate curfews, and corner arguments."
            hostelName.contains("Okija", ignoreCase = true) || hostelName.contains("Zik", ignoreCase = true) ->
                "High odds of midnight borehole bucket queues, NEPA blackout scrambles, and room arguments."
            hostelName.contains("Hilltop", ignoreCase = true) || hostelName.contains("Odenigwe", ignoreCase = true) ->
                "No Hall Porter curfews! Higher odds of loud balcony parties, generator fumes, and NEPA transformer trips."
            else ->
                "Unpredictable UNN hostel nights: water queues, roommate cooking disputes, room parties, or NEPA blackouts."
        }
    }

    /**
     * Selects a night-time hostel shenanigan tailored to the player's current hostel,
     * avoiding repeating the most recently logged night event.
     */
    fun rollNightEvent(
        hostelName: String,
        recentLogs: List<String>,
        seed: Int = Random.nextInt(10000)
    ): HostelNightEvent {
        // Filter out events explicitly excluded for this hostel (e.g., Porter lockouts at Hilltop off-campus)
        val eligible = eligibleEventsForHostel(hostelName)

        // Avoid immediate back-to-back repeat if possible
        val lastLog = recentLogs.firstOrNull { it.contains("[NIGHT") } ?: ""
        val nonRepeating = eligible.filter { !lastLog.contains(it.title) }.ifEmpty { eligible }

        // Build weighted pool: events matching preferredHostelKeywords get 3x weight
        val weightedPool = mutableListOf<HostelNightEvent>()
        nonRepeating.forEach { ev ->
            val matchesHostel = ev.preferredHostelKeywords.any { kw ->
                hostelName.contains(kw, ignoreCase = true)
            }
            val weight = if (matchesHostel) 3 else 1
            repeat(weight) { weightedPool.add(ev) }
        }

        val index = kotlin.math.abs(seed) % weightedPool.size
        return weightedPool[index]
    }
}
