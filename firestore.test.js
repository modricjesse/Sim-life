const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

function validCareerPayload(userId, now) {
  return {
    userId,
    studentName: "Chukwudi Okafor",
    regNumber: "2026/241098",
    campus: "Nsukka",
    faculty: "Engineering",
    department: "Electronic Engineering",
    hostel: "Zik's Flats",
    archetype: "Efiwe (Bookworm)",
    level: 100,
    semester: 1,
    week: 1,
    cgpa: 4.5,
    naira: 25000,
    energy: 85,
    sanity: 80,
    academicPrep: 40,
    streetCred: 25,
    politicalClout: 10,
    socialTier: "Jambite / JJC",
    sugOffice: "None",
    carryOvers: 0,
    isGraduated: false,
    inventory: ["Rechargeable Lamp", "GS 101 Textbook"],
    relationships: ["Roommate_Emeka:70"],
    recentLogs: ["W1: Cleared biometrics at Ekpo Ref and survived October Rush."],
    badges: ["First Week Lion"],
    createdAt: now,
    updatedAt: now,
  };
}

function validAlumniPayload(userId, now) {
  return {
    userId,
    studentName: "Chukwudi Okafor",
    regNumber: "2026/241098",
    department: "Electronic Engineering",
    finalCgpa: 4.72,
    degreeClass: "First Class Honours",
    finalTitle: "Grand Roar of Nsukka",
    netWorthNaira: 185000,
    summary: "Survived Zik's Flats, conquered GS exams at Nnamdi Azikiwe Library, and graduated top of Engineering.",
    createdAt: now,
    updatedAt: now,
  };
}

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user cannot read or write student careers", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(
    unauthDb.collection("users").doc(ALICE_UID).collection("careers").doc("career_1").get()
  );
});

test("Authenticated owner can create, read, list, and update their own career", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();
  const docRef = aliceDb.collection("users").doc(ALICE_UID).collection("careers").doc("career_1");

  await assertSucceeds(docRef.set(validCareerPayload(ALICE_UID, now)));
  await assertSucceeds(docRef.get());
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("careers").where("userId", "==", ALICE_UID).get()
  );
  await assertSucceeds(
    docRef.update({
      week: 2,
      naira: 22500,
      academicPrep: 55,
      updatedAt: new Date(),
    })
  );
});

test("Authenticated user cannot read or modify another user's career", async () => {
  const now = new Date();
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context
      .firestore()
      .collection("users")
      .doc(ALICE_UID)
      .collection("careers")
      .doc("career_1")
      .set(validCareerPayload(ALICE_UID, now));
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const aliceCareerRef = bobDb.collection("users").doc(ALICE_UID).collection("careers").doc("career_1");
  await assertFails(aliceCareerRef.get());
  await assertFails(aliceCareerRef.update({ naira: 0, updatedAt: new Date() }));
});

test("Shadow update with ghost field or invalid CGPA is rejected", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();
  const docRef = aliceDb.collection("users").doc(ALICE_UID).collection("careers").doc("career_1");
  await assertSucceeds(docRef.set(validCareerPayload(ALICE_UID, now)));

  // Ghost field
  await assertFails(docRef.update({ isAdmin: true, updatedAt: new Date() }));
  // Invalid CGPA > 5.0
  await assertFails(docRef.update({ cgpa: 6.5, updatedAt: new Date() }));
});

test("Authenticated owner can create and query hall_of_fame alumni records", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const now = new Date();
  const recordRef = aliceDb.collection("users").doc(ALICE_UID).collection("hall_of_fame").doc("rec_1");

  await assertSucceeds(recordRef.set(validAlumniPayload(ALICE_UID, now)));
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("hall_of_fame").where("userId", "==", ALICE_UID).get()
  );
});
