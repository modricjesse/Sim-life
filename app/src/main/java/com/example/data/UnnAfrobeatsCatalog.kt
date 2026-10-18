package com.example.data

data class AfrobeatsTrack(
    val id: String,
    val title: String,
    val artist: String,
    val subgenre: String,
    val bpm: Int,
    val campusVibeLocation: String,
    val vibeDescription: String,
    val energyDelta: Int = 0,
    val sanityDelta: Int = 0,
    val prepDelta: Int = 0,
    val credDelta: Int = 0,
    val cloutDelta: Int = 0,
    val bassNotesHz: List<Double>,
    val melodyNotesHz: List<Double>
)

object UnnAfrobeatsCatalog {

    val tracks: List<AfrobeatsTrack> = listOf(
        AfrobeatsTrack(
            id = "afro_last_last",
            title = "Last Last (Breakfast Anthem)",
            artist = "Burna Boy",
            subgenre = "Afro-Fusion",
            bpm = 108,
            campusVibeLocation = "Okija Hall & Bush Bar",
            vibeDescription = "'E don cast, last last na everybody go chop breakfast!' The ultimate UNN recovery anthem after tough exams or relationship drama.",
            energyDelta = 4,
            sanityDelta = 12,
            credDelta = 8,
            bassNotesHz = listOf(110.0, 130.81, 146.83, 130.81),
            melodyNotesHz = listOf(440.0, 523.25, 587.33, 659.25, 587.33, 523.25, 440.0, 392.0)
        ),
        AfrobeatsTrack(
            id = "afro_unavailable",
            title = "Unavailable (Log Drum Cruise)",
            artist = "Davido ft. Musa Keys",
            subgenre = "Afro-Piano",
            bpm = 114,
            campusVibeLocation = "Hilltop Penthouse & SUB",
            vibeDescription = "Infectious Amapiano log drums shaking every hostel balcony from Mary Slessor to Odenigwe on Friday night.",
            energyDelta = 10,
            sanityDelta = 8,
            credDelta = 10,
            bassNotesHz = listOf(98.0, 98.0, 116.54, 130.81),
            melodyNotesHz = listOf(392.0, 466.16, 523.25, 587.33, 523.25, 466.16, 392.0, 349.23)
        ),
        AfrobeatsTrack(
            id = "afro_calm_down",
            title = "Calm Down (Night Breeze Mix)",
            artist = "Rema",
            subgenre = "Afrorave",
            bpm = 107,
            campusVibeLocation = "Vet Mountain & JP Clark Library",
            vibeDescription = "Smooth melodic guitar and shaker groove for late-evening shawarma dates or focused coding at Carver Building.",
            energyDelta = 4,
            sanityDelta = 10,
            prepDelta = 8,
            credDelta = 6,
            bassNotesHz = listOf(123.47, 146.83, 164.81, 146.83),
            melodyNotesHz = listOf(493.88, 587.33, 659.25, 739.99, 659.25, 587.33, 493.88, 440.0)
        ),
        AfrobeatsTrack(
            id = "afro_essence",
            title = "Essence (Nsukka Sunset Groove)",
            artist = "Wizkid ft. Tems",
            subgenre = "Afrobeats R&B",
            bpm = 104,
            campusVibeLocation = "Mary Slessor & Okpara Hall",
            vibeDescription = "Lush, warm Afrobeats classic drifting across the Franco quadrangle as golden hour hits the Nsukka hills.",
            energyDelta = 6,
            sanityDelta = 14,
            credDelta = 8,
            bassNotesHz = listOf(103.83, 116.54, 130.81, 155.56),
            melodyNotesHz = listOf(415.30, 466.16, 523.25, 622.25, 523.25, 466.16, 415.30, 392.0)
        ),
        AfrobeatsTrack(
            id = "afro_terminator",
            title = "Terminator / Lonely At The Top",
            artist = "Asake",
            subgenre = "Street-Hop Amapiano",
            bpm = 116,
            campusVibeLocation = "PAA Auditorium & Freedom Square",
            vibeDescription = "Choir harmonies meet thunderous street-hop log drums—guaranteed to turn any corridor into a full concert.",
            energyDelta = 12,
            sanityDelta = 8,
            credDelta = 10,
            cloutDelta = 6,
            bassNotesHz = listOf(110.0, 123.47, 130.81, 146.83),
            melodyNotesHz = listOf(440.0, 493.88, 523.25, 587.33, 659.25, 587.33, 523.25, 440.0)
        ),
        AfrobeatsTrack(
            id = "afro_rush",
            title = "Rush (Sabi Lioness Energy)",
            artist = "Ayra Starr",
            subgenre = "Afro-Pop",
            bpm = 110,
            campusVibeLocation = "Faculty of Arts & FASSA Quad",
            vibeDescription = "'E dey rush!' High-energy main-character soundtrack before stepping into GS exams or SUG manifesto debates.",
            energyDelta = 6,
            sanityDelta = 6,
            prepDelta = 8,
            cloutDelta = 8,
            bassNotesHz = listOf(130.81, 146.83, 164.81, 196.0),
            melodyNotesHz = listOf(523.25, 587.33, 659.25, 783.99, 659.25, 587.33, 523.25, 493.88)
        ),
        AfrobeatsTrack(
            id = "afro_soweto",
            title = "Soweto (Odim Gate Chill)",
            artist = "Victony & Tempoe",
            subgenre = "Melodic Afrobeats",
            bpm = 100,
            campusVibeLocation = "Odim Gate & Zik's Flats",
            vibeDescription = "Silky keyboard chords and laid-back percussion for unwinding after 6 hours of practicals and lecture marathons.",
            energyDelta = 8,
            sanityDelta = 12,
            prepDelta = 4,
            bassNotesHz = listOf(116.54, 130.81, 146.83, 174.61),
            melodyNotesHz = listOf(466.16, 523.25, 587.33, 698.46, 587.33, 523.25, 466.16, 440.0)
        ),
        AfrobeatsTrack(
            id = "afro_highlife_eastern",
            title = "Sweet Mother / Ogene Highlife Groove",
            artist = "Prince Nico & Lion FM Band",
            subgenre = "Eastern Highlife",
            bpm = 118,
            campusVibeLocation = "Margaret Ekpo Ref & Staff Club",
            vibeDescription = "Authentic Eastern guitar highlife and Ogene percussion—beloved by Deans, Hall Porters, and Cultural Night crowds.",
            energyDelta = 6,
            sanityDelta = 10,
            credDelta = 6,
            cloutDelta = 10,
            bassNotesHz = listOf(130.81, 164.81, 146.83, 196.0),
            melodyNotesHz = listOf(523.25, 659.25, 587.33, 783.99, 659.25, 523.25, 587.33, 523.25)
        )
    )

    fun findTrack(id: String): AfrobeatsTrack {
        return tracks.firstOrNull { it.id == id } ?: tracks.first()
    }
}
