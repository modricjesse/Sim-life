# Night-Time Hostel Shenanigans Random Event Generator

This plan introduces a dedicated **Night-Time Hostel Shenanigans Generator** to **Lion Den Sim**, bringing the unpredictable midnight reality of University of Nigeria residences (Mary Slessor, Okija, Bello, Okpara, Zik's Flats, Odenigwe, Hilltop, and Manuwa) to life through dynamic midnight dilemmas that directly impact player Energy, Sanity, Academic Prep, Street Cred, Political Clout, and Naira.

## User Review & Critical Decisions

> [!IMPORTANT]
> All core gameplay mechanics below reflect your confirmed choices from our design interview:

- **Confirmed Decision 1 — Dual Trigger Mechanism**: Night-time hostel shenanigans will trigger **both** automatically during week transitions (representing midnight in the hostel before the new week dawns) **and** on-demand via a dedicated **"Turn In for the Night (Hostel Rest & Midnight Roll)"** action card in the Den Hub and Interactive Campus Map.
- **Confirmed Decision 2 — Complete 5-Scenario Shenanigan Suite**: The generator includes all five requested midnight categories:
  1. **Borehole Water Shortages & Midnight Bucket Queues** (defending tap spots at 12:30 AM, hiring "Water Boys", or rationing sachet water).
  2. **Impromptu Hostel Room Parties & Loud Music** (corridor birthday bashes, Bluetooth speaker wars, and suya/shawarma link-ups vs. exam sleep).
  3. **Heated Roommate & Squatting Arguments** (conflicts over unwashed pots, disappearing Garri/Indomie, alarm clocks, and unauthorized "squatters").
  4. **Hall Porter Hotplate Inspections & 10 PM Curfew Lockouts** (hiding bubbling stew pots from porters, scaling hostel fences after night class, or negotiating via Hall Government).
  5. **Midnight NEPA Blackouts & Phone Charging Scrambles** (blown transformers, "UP NEPA!" roars, Tiger generator socket extensions, and Powerbank survival).
- **Confirmed Decision 3 — Hostel-Specific Weighting & Tailored Flavor**: Events and probability weights adapt dynamically to the player's current residence:
  - *Mary Slessor & Okpara*: Higher odds of strict 10 PM porter curfew lockouts, hotplate raids, and courtyard social drama.
  - *Okija & Bello*: Higher odds of midnight Aluta congresses, noisy viewing-center arguments, and borehole bucket politics.
  - *Zik's Flats & Odenigwe*: Higher odds of transformer blackouts, roommate/neighbor cooking arguments, and water tanker scrambles.
  - *Hilltop Self-Con Lodge*: Immunity to porters and bucket queues, replaced by generator fuel contributions, penthouse corridor raves, and neighbor sound-system face-offs.

---

## 1. Overview & Core Concept

- **What It Does**: Simulates the iconic midnight chaos of UNN student housing. Whenever night falls—either when the player rests at their hostel or when a week advances—the generator evaluates the student's current residence, owned survival gear (such as a *30,000mAh Oraimo Powerbank*, *Rechargeable Reading Lamp*, or *6kg Hostel Gas Cylinder*), and roommate standing to trigger a contextual midnight event with 3 tactical choices.
- **Target Audience / Persona**: Players of **Lion Den Sim** who want authentic Nigerian university nostalgia where surviving hostel life is just as strategic as passing exams.
- **Key Value**: Transforms hostel selection and Odim Market survival gear from passive stat boosts into active gameplay shields and opportunities during midnight emergencies.

---

## 2. User Experience & Visual Design

### Key User Flows
1. **Manual Hostel Night Rest ("Turn In for the Night")**:
   - From the **Den Hub** or when located at a Hostel landmark on the **UNN Map**, the player can tap **"Hostel Night Rest & Midnight Roll"**.
   - Resting restores base Energy, then rolls the hostel-weighted night generator. If a shenanigan triggers, the **Midnight Hostel Shenanigan Modal** slides in immediately.
2. **Automatic End-of-Week Night Trigger**:
   - When the player completes a weekly move that advances the semester clock, the night engine checks for a midnight hostel occurrence (high probability each week).
   - If triggered, the player resolves the midnight event before the next week begins.
3. **Interactive Resolution & Gear Perks**:
   - Each midnight event presents 3 distinct choices (e.g., *Aggressive/Aluta*, *Diplomatic/Financial*, or *Stealth/Gear-Assisted*).
   - If the player owns relevant Odim Market gear (e.g., *Rechargeable Reading Lamp* during a NEPA blackout or *6kg Gas Cylinder* during a cooking dispute), a special **Gear Bonus Badge** highlights reduced penalties or bonus Academic Prep/Sanity.
   - Resolving a choice updates all stats in real time, logs the story to **The Roar Chronicle**, and displays an immediate outcome summary banner.

### Visual Identity & Theme
- **Aesthetic Direction**: Atmospheric "Midnight in the Den" dark UI mode within the Royal UNN Heritage Green & Lion Gold palette.
- **Color Palette & Mood**:
  - Deep midnight backdrop (`#062819` / `#04150D`) paired with warm lantern amber (`LionGold` `#F5B700` / `#FFD166`) to evoke rechargeable lamps in a dark hostel corridor.
  - Status accents: `UnnMintAccent` (`#52B788`) for Energy/Wellness gains, `SanityViolet` (`#A78BFA`) for Sanity shifts, `CloutCyan` (`#38BDF8`) for Aluta/Hostel Clout, and `AlertCrimson` (`#EF4444`) for porter raids or roommate clashes.
- **Typography & Hierarchy**:
  - **Playfair Display** for dramatic midnight event headlines, **Outfit** for narrative descriptions and choice cards, and **JetBrains Mono** for exact stat delta badges (`Energy -12 • Sanity +15 • Cred +8`).
- **Interactive Feedback & Motion**:
  - Smooth `AnimatedVisibility` banners and an elevated M3 dialog card featuring a **"MIDNIGHT AT [HOSTEL NAME]"** header badge, timestamp tag (e.g., `01:15 AM`), and clear stat preview pills on every choice button.

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Hostel-Weighted Event Selection with Anti-Repetition**
  - *Chosen Approach*: Combine weighted random selection (based on the student's current `hostel` and `week`) with a check against `recentLogs` so the exact same midnight event never repeats back-to-back.
  - *Why*: Ensures relocating from *Zik's Flats* to *Mary Slessor* or *Hilltop* feels tangibly different while keeping every night fresh and unpredictable.
  - *Alternatives Considered*: Purely uniform random selection, which was passed over because a student in an off-campus Hilltop Self-Con shouldn't face 10 PM hostel porter lockouts.
- **Decision 2: Seamless Cloud Persistence via Existing Firestore Career Document**
  - *Chosen Approach*: Persist all stat changes, awarded midnight badges (e.g., *Bucket Queue General*, *Curfew Ninja*, *Roommate Peacemaker*), and chronicle entries directly into the authenticated user's `StudentCareer` document in Firestore.
  - *Why*: Preserves instant cloud sync and full compatibility with existing Firestore security rules without requiring schema migrations.

---

## 4. Technical Architecture & Data Strategy *(Technical Reference)*

### Architecture & Component Diagram
```
┌─────────────────────────────────────────────────────────────────────────┐
│                        Jetpack Compose UI Layer                         │
│  ┌──────────────────────────────┐   ┌────────────────────────────────┐  │
│  │   Den Hub & UNN Map Tabs     │   │  Midnight Hostel Event Modal   │  │
│  │ • "Hostel Night Rest" Card   │   │ • Hostel & Timestamp Header    │  │
│  │ • Weekly Action Triggers     │   │ • Gear Bonus Indicator Pill    │  │
│  │ • Roar Chronicle Night Logs  │   │ • 3 Interactive Choice Cards   │  │
│  └──────────────┬───────────────┘   └───────────────┬────────────────┘  │
└─────────────────┼───────────────────────────────────┼───────────────────┘
                  │ User Action / Choice              │ Resolve Choice
                  ▼                                   ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                     LionDenViewModel (State Holder)                     │
│  • triggerHostelNightRest(career)                                       │
│  • maybeTriggerAutomaticNightShenanigan(career)                         │
│  • resolveHostelNightShenanigan(career, event, choice)                  │
└─────────────────┬───────────────────────────────────┬───────────────────┘
                  │ Query Weighted Event              │ Persist Career
                  ▼                                   ▼
┌──────────────────────────────────────┐  ┌───────────────────────────────┐
│   HostelNightShenanigansGenerator    │  │       LionDenRepository       │
│  • 5 Core Scenarios (12+ Events)     │  │  • Firestore StudentCareer    │
│  • Hostel-Specific Odds & Filters    │  │  • Real-time Snapshot Flow    │
│  • Inventory Gear Bonus Evaluator    │  │  • Security Rule Compliance   │
└──────────────────────────────────────┘  └───────────────────────────────┘
```

### Data Model & State
- **HostelNightEvent**: Represents a night-time shenanigan with `id`, `title`, `scenarioType` (`WATER_SHORTAGE`, `ROOM_PARTY`, `ROOMMATE_ARGUMENT`, `PORTER_INSPECTION`, `NEPA_BLACKOUT`), `timestampLabel` (e.g., `"11:45 PM"`, `"02:10 AM"`), `applicableHostels` (or all hostels with custom weights), `description`, `helpfulGearKeyword` (optional inventory item that buffs outcomes), and a list of `ShenaniganChoice` options.
- **ViewModel State**:
  - `activeNightEventModal: StateFlow<HostelNightEvent?>` holds the currently triggered midnight event so the UI can display a distinct midnight-styled modal dialog.

### Interactive Component & State Mapping
1. **Manual "Hostel Night Rest" Button** (`DenHubTabContent` & Hostel Landmarks):
   - Grants an immediate rest recovery (`+22 Energy`, `+8 Sanity`) and invokes `HostelNightShenanigansGenerator.rollNightEvent(hostel, inventory, recentLogs)`.
   - Opens the **Midnight Hostel Shenanigan Modal** with 3 choices.
2. **Automatic Night Roll on Week Advance** (`advanceClockOrPersist`):
   - Whenever a week advances (Weeks 1–7), rolls a 65% chance to trigger a hostel-tailored night shenanigan after the daytime action completes, surfacing the modal immediately.
3. **Choice Resolution & Gear Synergy**:
   - Selecting any choice applies `nairaDelta`, `energyDelta`, `sanityDelta`, `prepDelta`, `credDelta`, and `cloutDelta` (with automatic bonus mitigation if the player owns the event's `helpfulGearKeyword`), awards any special badge, prepends a `[NIGHT @ Hostel]` entry to `recentLogs`, and saves to Firestore.
